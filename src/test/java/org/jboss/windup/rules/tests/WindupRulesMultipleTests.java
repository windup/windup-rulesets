package org.jboss.windup.rules.tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

import javax.inject.Inject;

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
import org.jboss.windup.config.phase.RulePhase;
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
import org.jboss.windup.reporting.model.rule.RuleProviderModel;
import org.jboss.windup.reporting.ruleexecution.RuleExecutionInformation;
import org.jboss.windup.reporting.service.rule.ExecutionPhaseService;
import org.jboss.windup.reporting.service.rule.RuleExecutionService;
import org.jboss.windup.reporting.service.rule.RuleProviderService;
import org.jboss.windup.rules.apps.java.config.SourceModeOption;
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

import org.jboss.windup.reporting.ruleexecution.RuleExecutionResultsListener;

@RunWith(ParameterizedArquillianRunner.class)
public class WindupRulesMultipleTests {

    private static final Logger LOG = Logger.getLogger(WindupRulesMultipleTests.class);

    private static String RUN_TEST_MATCHING = "runTestsMatching";

    @Inject
    private Imported<RuleLifecycleListener> listeners;

    @Inject
    private RuleLoader ruleLoader;

    Map<String, ExecutionPhaseModel> phaseModelMap;

    GraphContext graphContext;

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
                    .forEach(files -> rulesetToTest.add(files));
        FileVisit.visit(rulesReviewed, predicate).stream()
                    .filter(file -> testToExecutePattern.matcher(file.toString()).find())
                    .map(file -> new File[] { file, rulesReviewed }).forEach(files -> rulesetToTest.add(files));

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
        File[] files = testRulesPath.getNextRule();
        Assert.assertTrue("No test file found",files !=null);

        LOG.info(String.format("Testing execution of rule %s%n", files[0].getName()));
        visit(files[0], files[1]);
    }

    @Test
    public void conventionRule()
    {
        testRulesPath.resetIterator();
        File[] files = rulesPath.getNextRule();
        LOG.info(String.format("Testing convention of rule %s%n", files[0].getName()));
        examine(files[0], files[1]);
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
            try (GraphContext context = factory.create(outputPath, true))
            {
                // load the ruletest file
                RuleTest ruleTest = parser.processDocument(ruleTestFile.toURI());
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



                List<RuleExecutionModel> masterExecList = new ArrayList<>();


                for (Rule rule : ruleTestConfiguration.getRules()) {

                    Iterable<RuleExecutionModel> execInfoList = this.ruleExecutionService.findAllByProperty(RuleExecutionModel.RULE_ID,rule.getId());

                    Iterator execIter = execInfoList.iterator();
                    while(execIter.hasNext())
                    {
                        masterExecList.add((RuleExecutionModel) execIter.next());
                    }

                }

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

            }




        }
        catch (Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected error: " + e.getMessage());
        }


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
        String rulePath = ruleFile.getPath();
        Path p = ruleFile.toPath();
        Path absoluteRulePath = p.toAbsolutePath();
        boolean foundMatchingTestFile = false;
        boolean foundMatchingTestRuleId = true;



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
                    foundMatchingTestFile = true;


                    break;
                }

            }



        }
        Assert.assertTrue("Test file matching rule",foundMatchingTestFile);
    }
}
