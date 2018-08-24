package org.jboss.windup.rules.tests;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.forge.arquillian.AddonDependencies;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.archive.AddonArchive;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.services.Imported;
import org.jboss.forge.furnace.util.Predicate;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.windup.config.*;
import org.jboss.windup.config.loader.RuleLoader;
import org.jboss.windup.config.loader.RuleLoaderContext;
import org.jboss.windup.config.metadata.RuleMetadataType;
import org.jboss.windup.config.metadata.RuleProviderRegistry;
import org.jboss.windup.config.parser.ParserContext;
import org.jboss.windup.exec.WindupProcessor;
import org.jboss.windup.exec.configuration.WindupConfiguration;
import org.jboss.windup.exec.configuration.options.SourceOption;
import org.jboss.windup.exec.configuration.options.TargetOption;
import org.jboss.windup.graph.GraphContext;
import org.jboss.windup.graph.GraphContextFactory;
import org.jboss.windup.graph.model.ProjectModel;
import org.jboss.windup.graph.model.resource.FileModel;
import org.jboss.windup.reporting.model.rule.ExecutionPhaseModel;
import org.jboss.windup.reporting.model.rule.RuleExecutionModel;
import org.jboss.windup.reporting.ruleexecution.RuleExecutionResultsListener;
import org.jboss.windup.reporting.service.rule.ExecutionPhaseService;
import org.jboss.windup.reporting.service.rule.RuleExecutionService;
import org.jboss.windup.reporting.service.rule.RuleProviderService;
import org.jboss.windup.rules.apps.java.config.SourceModeOption;
import org.jboss.windup.util.exception.WindupException;
import org.jboss.windup.util.file.FileSuffixPredicate;
import org.jboss.windup.util.file.FileVisit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.ocpsoft.logging.Logger;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.Rule;
import org.ocpsoft.rewrite.context.Context;
import org.ocpsoft.rewrite.param.DefaultParameterValueStore;
import org.ocpsoft.rewrite.param.ParameterValueStore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

@RunWith(ParameterizedArquillianRunner.class)
public class WindupRulesMultipleTests {

    private static final Logger LOG = Logger.getLogger(WindupRulesMultipleTests.class);

    private static String RUN_TEST_MATCHING = "runTestsMatching";

    @Inject
    private Imported<RuleLifecycleListener> listeners;

    @Inject
    private RuleLoader ruleLoader;

    Map<String, ExecutionPhaseModel> phaseModelMap;

    RuleProviderService ruleProviderService;
    RuleExecutionService ruleExecutionService;
    ExecutionPhaseService executionPhaseService;

    @Parameterized.Parameters(name = "{index}: Test {0}")
    public static Collection<File[]> data(boolean findingTestFiles)
    {
        String testToExecute = System.getProperty(RUN_TEST_MATCHING);
        if (StringUtils.isBlank(testToExecute))
        {
            testToExecute = "";
        }
        Pattern testToExecutePattern = Pattern.compile(testToExecute);
        FileSuffixPredicate predicate = null;
        if (findingTestFiles)
        {
            predicate = new FileSuffixPredicate("\\.(windup|rhamt)\\.test\\.xml");
        }
        else
        {
            predicate = new FileSuffixPredicate("\\.(windup|rhamt)\\.xml");
        }
        final File directory = new File("rules");
        final File rulesReviewed = new File("rules-reviewed");
        final List<File[]> rulesetToTest = new ArrayList<>();
        FileVisit.visit(directory, predicate).stream()
                    .filter(file -> testToExecutePattern.matcher(file.toString()).find()).map(file -> new File[] { file, directory })
                    .forEach(rulesetToTest::add);
        FileVisit.visit(rulesReviewed, predicate).stream()
                    .filter(file -> testToExecutePattern.matcher(file.toString()).find())
                    .map(file -> new File[] { file, rulesReviewed }).forEach(rulesetToTest::add);

        return rulesetToTest;
    }

    @Deployment
    @AddonDependencies({
            @AddonDependency(name = "org.jboss.windup.exec:windup-exec"),
            @AddonDependency(name = "org.jboss.windup.config:windup-config-xml"),
            @AddonDependency(name = "org.jboss.windup.rules.apps:windup-rules-java"),
            @AddonDependency(name = "org.jboss.windup.rules.apps:windup-rules-java-ee"),
            @AddonDependency(name = "org.jboss.windup.rules.apps:windup-rules-java-project"),
            @AddonDependency(name = "org.jboss.windup.rules.apps:windup-rules-xml"),
            @AddonDependency(name = "org.jboss.windup.reporting:windup-reporting"),
            @AddonDependency(name = "org.jboss.windup.utils:windup-utils"),
            @AddonDependency(name = "org.jboss.forge.furnace.container:cdi")
    })
    public static AddonArchive getDeployment()
    {
        return ShrinkWrap.create(AddonArchive.class)
                .addBeansXML()
                .addPackage(WindupRulesMultipleTests.class.getPackage());
    }

    @Inject
    private Furnace furnace;

    @Inject
    private GraphContextFactory factory;

    @Inject
    private WindupProcessor processor;
    
    @Inject
    private TestRulesPath testRulesPath;

    @Inject
    private RulesPath rulesPath;

    
    @Before
    public void before()
    {
        testRulesPath.setRules(data(true));
        rulesPath.setRules(data(false));
    }

    @Test
    public void executeRule()
    {
        try {
            testRulesPath.resetIterator();

            File[] ruleFiles = rulesPath.getNextRule();
            File[] ruleTestFiles = findMatchingTestFile(ruleFiles[0], ruleFiles[1]);
            Assert.assertNotNull("No test file found", ruleTestFiles);

            LOG.info(String.format("Testing execution of rule %s%n", ruleFiles[0].getName()));
            visit(ruleTestFiles[0], ruleTestFiles[1]);
        }
        catch(AssertionError ae)
        {
            LOG.error(ae.getMessage());
            throw ae;
        }
    }

    @Test
    public void conventionRule()
    {
        try {
            testRulesPath.resetIterator();
            if(!rulesPath.hasNextRule())
            {
                rulesPath.resetIterator();
            }
            File[] files = rulesPath.getNextRule();
            LOG.info(String.format("Testing convention of rule %s%n", files[0].getName()));
            examine(files[0], files[1]);
        }
        catch(AssertionError ae)
        {
            LOG.error(ae.getMessage());
            throw ae;
        }
    }

    public void visit(File ruleTestFile, File directory)
    {
        final RuleLoaderContext ruleLoaderContext = new RuleLoaderContext();
        final ParserContext parser = new ParserContext(furnace, ruleLoaderContext);

        LOG.info("\n==============================================================================================="
                + "\n Running test ruleset: " + ruleTestFile.getPath()
                + "\n==============================================================================================="
                + "\n");

        try
        {
            Map<String, Exception> exceptions;
            Path outputPath = getDefaultPath();
            RuleTest ruleTest = null;
            try (GraphContext context = factory.create(outputPath, true))
            {
                // load the ruletest file
                try {
                    ruleTest = parser.processDocument(ruleTestFile.toURI());
                }
                catch (WindupException we)
                {
                    Assert.fail("XML parse fail on file " + ruleTestFile.toURI() + ":" + we.getMessage());
                    throw we;
                }
                List<Path> rulePaths = new ArrayList<>();
                if (ruleTest.getRulePaths().isEmpty())
                {
                    // The default path is ../, so this is two directories up from the test file iteself.
                    rulePaths.add(ruleTestFile.getParentFile().getParentFile().toPath().normalize().toAbsolutePath());
                }
                else
                {
                    for (String rulePath : ruleTest.getRulePaths())
                    {
                        Path ruleTestDirectory = ruleTestFile.toPath().getParent().normalize();
                        Path path = ruleTestDirectory.resolve(rulePath).normalize().toAbsolutePath();
                        rulePaths.add(path.toAbsolutePath());
                    }
                }

                Configuration ruleTestConfiguration = parser.getBuilder().getConfiguration(ruleLoaderContext);


                for (Rule rule : ruleTestConfiguration.getRules())
                {
                    parser.getBuilder().enhanceRuleMetadata(parser.getBuilder(), rule);
                    if (rule instanceof Context)
                    {
                        ((Context) rule).put(RuleMetadataType.HALT_ON_EXCEPTION, false);
                    }
                }

                // run the assertions from the ruletest file
                GraphRewrite event = new GraphRewrite(context);



                RuleExecutionResultsListener resultsListener = null;

                for (RuleLifecycleListener listener : this.listeners)
                {
                    if (listener instanceof RuleExecutionResultsListener)
                    {
                        resultsListener = (RuleExecutionResultsListener)listener;
                        break;
                    }
                }



                RuleProviderRegistry providerRegistry = ruleLoader.loadConfiguration(ruleLoaderContext);
                ruleProviderService = new RuleProviderService(context);
                ruleExecutionService = new RuleExecutionService(context);
                executionPhaseService = new ExecutionPhaseService(context);
                this.phaseModelMap = new HashMap<>();
                event.getRewriteContext().put(RuleProviderRegistry.class, providerRegistry);
                resultsListener.beforeExecution(event);

                // run windup
                File testDataPath = new File(ruleTestFile.getParentFile(), ruleTest.getTestDataPath());
                Path reportPath = outputPath.resolve("reports");
                runWindup(context, directory, rulePaths, testDataPath, reportPath.toFile(), ruleTest.isSourceMode(), ruleTest.getSource(), ruleTest.getTarget());

                RuleSubset ruleSubset = RuleSubset.create(ruleTestConfiguration);
                ruleSubset.perform(event, createEvalContext(event));
                exceptions = ruleSubset.getExceptions();

                List<RuleExecutionModel> masterExecList = new ArrayList<>();

                List<String> idsList = new ArrayList<>();
                rulePaths.forEach( rulePath -> Optional.ofNullable(getRuleIds(rulePath)).ifPresent(idsList::addAll));

                //Build a list of RuleExecutionModel for each rule we tried to execute
                for (String id : idsList) {

                    Iterable<RuleExecutionModel> execInfoList = this.ruleExecutionService.findAllByProperty(RuleExecutionModel.RULE_ID,id);


                    for (RuleExecutionModel anExecInfoList : execInfoList) {
                        masterExecList.add(anExecInfoList);
                    }

                }

                //Assess failure and execution status of each rule we tried to execute
                for (RuleExecutionModel execInfo: masterExecList)
                {
                    if  (execInfo != null )
                    {
                        if (execInfo.getFailed()) {
                            Assert.fail(execInfo.getRuleId() + ": " + execInfo.getFailureMessage());
                        }
                        else
                        {
                            Assert.assertTrue(execInfo.getRuleId() + ": Ran without failure",true);
                        }

                        if (!execInfo.getExecuted())
                        {
                            Assert.fail(execInfo.getRuleId() + ": " + "Not Executed");
                        }
                        else
                        {
                            Assert.assertTrue(execInfo.getRuleId() + ": Executed",true);
                        }

                    }
                }

                if (exceptions != null && exceptions.size()>0)
                {
                    List<String> failureMessages = new ArrayList<>();
                    exceptions.forEach( (key, value) -> failureMessages.add("Failure: " + key + ": " + value.getMessage()));

                    Assert.fail(StringUtils.join(failureMessages,System.lineSeparator()));
                }

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    private DefaultEvaluationContext createEvalContext(GraphRewrite event)
    {
        final DefaultEvaluationContext evaluationContext = new DefaultEvaluationContext();
        final DefaultParameterValueStore values = new DefaultParameterValueStore();
        evaluationContext.put(ParameterValueStore.class, values);
        return evaluationContext;
    }


    private Path getDefaultPath()
    {
        return FileUtils.getTempDirectory().toPath().resolve("WindupRulesTests").resolve("windupgraph_" + RandomStringUtils.randomAlphanumeric(6));
    }

    private void runWindup(GraphContext context, File baseRuleDirectory, final List<Path> rulePaths, File input, File output, boolean sourceMode, String source, String target) throws IOException
    {
        ProjectModel pm = context.getFramed().addFramedVertex(ProjectModel.class);
        pm.setName("Project: " + input.getAbsolutePath());
        FileModel inputPath = context.getFramed().addFramedVertex(FileModel.class);
        inputPath.setFilePath(input.getCanonicalPath());

        FileUtils.deleteDirectory(output);
        Files.createDirectories(output.toPath());

        pm.setRootFileModel(inputPath);
        WindupConfiguration windupConfiguration = new WindupConfiguration()
                .setGraphContext(context);
        windupConfiguration.addInputPath(Paths.get(inputPath.getFilePath()));
        windupConfiguration.setOutputDirectory(output.toPath());
        windupConfiguration.addDefaultUserRulesDirectory(baseRuleDirectory.toPath());
        windupConfiguration.setOptionValue(SourceModeOption.NAME, sourceMode);
        windupConfiguration.setOptionValue(KeepWorkDirsOption.NAME, true);

        windupConfiguration.setOnline(false);
        if (StringUtils.isNotBlank(source))
            windupConfiguration.setOptionValue(SourceOption.NAME, Collections.singletonList(source));

        if (StringUtils.isNotBlank(target))
            windupConfiguration.setOptionValue(TargetOption.NAME, Collections.singletonList(target));

        final String baseRulesPathNormalized = baseRuleDirectory.toPath().normalize().toAbsolutePath().toString();
        windupConfiguration.setRuleProviderFilter(new Predicate<RuleProvider>()
        {
            @Override
            public boolean accept(RuleProvider type)
            {
                if (type.getMetadata().getOrigin() == null)
                    return true;

                if (!type.getMetadata().getOrigin().contains(baseRulesPathNormalized))
                    return true;

                for (Path acceptedRulePath : rulePaths)
                {
                    if (type.getMetadata().getOrigin().contains(acceptedRulePath.toString()))
                        return true;
                }
                return false;
            }
        });
        processor.execute(windupConfiguration);
    }

    public void examine(File ruleFile, File directory)
    {
        Path p = ruleFile.toPath();
        Path absoluteRulePath = p.toAbsolutePath();
        boolean foundMatchingTestFile = false;
        List<String> failingIds = new ArrayList<>();

        File[] matchingTest = findMatchingTestFile(ruleFile,directory);


        if (matchingTest != null)
        {

            File test = matchingTest[0];
            final RuleLoaderContext ruleLoaderContext = new RuleLoaderContext();
            final ParserContext parser = new ParserContext(furnace, ruleLoaderContext);
            RuleTest ruleTest = parser.processDocument(test.toURI());

            foundMatchingTestFile = true;
            List<String> ids = getRuleIds(absoluteRulePath);
            if (CollectionUtils.isNotEmpty(ids))
            {
                for(String id: ids)
                {
                    boolean foundMatchingTestRuleId = false;
                    for(String testRuleId:ruleTest.getRuleIds())
                    {
                        if (testRuleId.equals(id.concat("-test")))
                        {
                            foundMatchingTestRuleId = true;
                            break;
                        }
                    }
                    if(!foundMatchingTestRuleId)
                    {
                        failingIds.add(id);
                    }
                }
            }
        }
        Assert.assertTrue("No test file matching rule",foundMatchingTestFile);
        Assert.assertEquals("Test rule Ids " + buildListOfFailingTestIds(failingIds) + " not found", 0, failingIds.size());
    }

    private List<String> getRuleIds(Path ruleFilePath)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        List<String> ids = new ArrayList<>();
        Document doc = null;

        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(ruleFilePath.toString());
            doc.getDocumentElement().normalize();

        }
        catch(SAXException se)
        {
            Assert.fail("XML Parse fail on file " + ruleFilePath.toString() + ": " + se.getMessage());
            return null;
        }
        catch(Exception e)
        {
            Assert.fail("XML Parser not available: " + e.getMessage());
            return null;
        }

        if(doc != null)
        {
            NodeList nodeList = doc.getElementsByTagName("rule");
            for (int i = 0; i < nodeList.getLength(); i++)
            {
                Node ruleNode = nodeList.item(i);
                Element ruleElement = (Element) ruleNode;
                ids.add(ruleElement.getAttribute("id"));
            }
        }

        return ids;

    }
    private String buildListOfFailingTestIds(List<String> failingIds)
    {
        List<String> ids = new ArrayList<>();
        failingIds.forEach( id -> ids.add(id + "-test"));
        return StringUtils.join(ids, ",");
    }

    private File[] findMatchingTestFile(File ruleFile, File directory)
    {
        Path p = ruleFile.toPath();
        Path absoluteRulePath = p.toAbsolutePath();



        while (testRulesPath.hasNextRule())
        {
            File [] tests = testRulesPath.getNextRule();
            File test = tests[0];
            final RuleLoaderContext ruleLoaderContext = new RuleLoaderContext();
            final ParserContext parser = new ParserContext(furnace, ruleLoaderContext);
            RuleTest ruleTest = parser.processDocument(test.toURI());

            for (String path : ruleTest.getRulePaths())
            {
                Path ruleTestDirectory = test.toPath().getParent().normalize();
                Path testRulePath = ruleTestDirectory.resolve(path).normalize().toAbsolutePath();
                if(testRulePath.equals(absoluteRulePath))
                {
                   return tests;
                }
            }
        }
        //No matching test file found
        return null;
    }
}
