package org.jboss.windup.rules.tests;

import org.apache.commons.lang3.StringUtils;
import org.jboss.windup.util.file.FileSuffixPredicate;
import org.jboss.windup.util.file.FileVisit;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This tests all the href attribute in ALL the rules every time the tests are executed (PR and nightly builds)
 * Helpful to ensure we have always links that points to something so that links are really helpful for the user
 * Optimized with a very basic cache for not checking more than once the same URL
 * <p>
 * The runTestsMatching is available in this test
 * <p>
 * To change the connection timeout use the standard "-Dsun.net.client.defaultConnectTimeout=<value>" property
 * from https://docs.oracle.com/javase/8/docs/technotes/guides/net/properties.html
 * <p>
 * To quickly see the debug log, execute the test with the property "-Dorg.slf4j.simpleLogger.log.org.jboss.windup=debug"
 */
@RunWith(Parameterized.class)
public class WindupRulesLinksTest {

    private static final Logger LOG = LoggerFactory.getLogger(WindupRulesLinksTest.class);
    private static final String RUN_TEST_MATCHING = "runTestsMatching";
    private static final List<Integer> ACCEPTED_RESPONSE_CODE = Arrays.asList(
            HttpURLConnection.HTTP_OK,
            HttpURLConnection.HTTP_MOVED_PERM,
            HttpURLConnection.HTTP_MOVED_TEMP
    );
    private static final FileSuffixPredicate FILE_SUFFIX_PREDICATE = new FileSuffixPredicate("\\.(windup|rhamt|mta)\\.xml");
    private static final List<File> DIRECTORIES_WITH_RULES = Arrays.asList(
            new File("rules"),
            new File("rules-reviewed"),
            new File("rules-generated"),
            new File("rules-overridden-azure")
    );
    private static final Map<String, Integer> CACHE_ANALYZED_LINKS = new HashMap<>();
    private static int totalRulesetsToBeTested = 0;
    private static int totalAnalyzedRulesets = 0;
    private static int totalAnalyzedRules = 0;
    private static int totalAnalyzedLinks = 0;

    // TODO: see https://issues.redhat.com/browse/WINDUPRULE-913 - find generic solution
    private static final List<String> CERT_FAILURE_LINKS = new ArrayList<>();
    static {
        CERT_FAILURE_LINKS.add("https://oracle.com/technical-resources/articles/java/jaxrs20.html");
        CERT_FAILURE_LINKS.add("https://in.relation.to/2015/05/11/hibernate-search-530-beta-1-with-native-lucene-faceting/");
    }

    private static final int RETRIES = 5;

    @Parameterized.Parameters(name = "{index}: Test {0}")
    public static Iterable<File> data()
    {
        final Pattern testToExecutePattern = Pattern.compile(StringUtils.defaultIfBlank(System.getProperty(RUN_TEST_MATCHING), ""));
        final List<File> rulesetToTest = new ArrayList<>();
        DIRECTORIES_WITH_RULES.forEach(directory ->
                FileVisit.visit(directory, FILE_SUFFIX_PREDICATE).stream()
                        .filter(file -> testToExecutePattern.matcher(file.toString()).find())
                        .forEach(rulesetToTest::add));
        totalRulesetsToBeTested = rulesetToTest.size();
        return rulesetToTest;
    }

    @Parameterized.Parameter
    public File ruleset;
    
    @AfterClass
    public static void printStatistics()
    {
        LOG.info(String.format("Analyzed %d ruleset, %d rules and %d links (%d unique links)", totalAnalyzedRulesets, totalAnalyzedRules, totalAnalyzedLinks, CACHE_ANALYZED_LINKS.keySet().size()));
    }

    @Test
    public void rulesLinksTest()
    {
        LOG.info(String.format("[%d/%d] Ruleset '%s' :: retrieving and testing links (if any)", ++totalAnalyzedRulesets, totalRulesetsToBeTested, ruleset));
        final Path absoluteRulePath = ruleset.toPath().toAbsolutePath();
        final Map<String, List<String>> idsWithLinks = getRuleIdsWithLinks(absoluteRulePath);
        final Map<String, List<String>> failedRulesWithLinks = new HashMap<>();
        final AtomicInteger totalRulesInRuleset = new AtomicInteger(0);
        final AtomicInteger totalLinksInRuleset = new AtomicInteger(0);
        idsWithLinks.forEach((id, links) -> {
            totalRulesInRuleset.incrementAndGet();
            totalLinksInRuleset.addAndGet(links.size());
            List<String> invalidLinks = links.stream().filter(((Predicate<String>) this::isValidLink).negate()).collect(Collectors.toList());
            if(!invalidLinks.isEmpty()) failedRulesWithLinks.put(id, invalidLinks);
            // To quickly see this log, execute the test with the property "-Dorg.slf4j.simpleLogger.log.org.jboss.windup=debug"
            LOG.debug(String.format("Rule '%s' :: %d links tested (%d not valid)", id, links.size(), invalidLinks.size()));
        });
        LOG.info(String.format("Ruleset '%s' :: analyzed %d rules and %d links", ruleset, totalRulesInRuleset.get(), totalLinksInRuleset.get()));
        totalAnalyzedRules += totalRulesInRuleset.get();
        totalAnalyzedLinks += totalLinksInRuleset.get();
        Assert.assertTrue("Rule IDs with broken links: " + buildListOfFailedRulesLinks(failedRulesWithLinks), failedRulesWithLinks.isEmpty());
    }

    private Map<String, List<String>> getRuleIdsWithLinks(Path ruleFilePath)
    {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder;
        final Document doc;
        final Map<String, List<String>> idsWithLinks = new HashMap<>();
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(ruleFilePath.toString());
            doc.getDocumentElement().normalize();
            final NodeList nodeList = doc.getElementsByTagName("rule");
            for (int i = 0; i < nodeList.getLength(); i++)
            {
                final Element ruleElement = (Element) nodeList.item(i);
                final NodeList ruleLinks = ruleElement.getElementsByTagName("link");
                final List<String> links = new ArrayList<>(ruleLinks.getLength());
                IntStream.range(0, ruleLinks.getLength()).forEach(index -> links.add(((Element)ruleLinks.item(index)).getAttribute("href")));
                idsWithLinks.put(ruleElement.getAttribute("id"), links);
            }
        }
        catch(SAXException se)
        {
            Assert.fail("XML Parse fail on file " + ruleFilePath.toString() + ": " + se.getMessage());
        }
        catch(Exception e)
        {
            Assert.fail("XML Parser not available: " + e.getMessage());
        }
        return idsWithLinks;
    }
    
    private boolean isValidLink(final String link)
    {
        if (CERT_FAILURE_LINKS.contains(link))
            return true;

        final long starTime = System.currentTimeMillis();
        int returnCode = 0;
        if (!CACHE_ANALYZED_LINKS.containsKey(link)) {
            for (int retry = 0; !ACCEPTED_RESPONSE_CODE.contains(returnCode) && retry < RETRIES; retry++) {
                if (retry > 0) LOG.warn(String.format("Tentative #%d to connect to %s", retry + 1, link));
                try {
                    final URL url = new URL(link);
                    final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    // property name from https://docs.oracle.com/javase/8/docs/technotes/guides/net/properties.html
                    urlConn.setConnectTimeout(Integer.getInteger("sun.net.client.defaultConnectTimeout", 5000));
                    urlConn.connect();
                    returnCode = urlConn.getResponseCode();
                } catch (IOException e) {
                    LOG.error(String.format("'%s' exception connecting to %s", e.getMessage(), link), e);
                }
            }
            CACHE_ANALYZED_LINKS.put(link, returnCode);
        }
        final boolean validLink = ACCEPTED_RESPONSE_CODE.contains(CACHE_ANALYZED_LINKS.get(link));
        if (validLink)
            LOG.debug(String.format("Response code %d for %s [%dms]", CACHE_ANALYZED_LINKS.get(link), link, System.currentTimeMillis() - starTime));
        else
            LOG.error(String.format("Response code %d for %s [%dms]", CACHE_ANALYZED_LINKS.get(link), link, System.currentTimeMillis() - starTime));
        return validLink;
    }

    private String buildListOfFailedRulesLinks(final Map<String, List<String>> failedRulesWithLinks)
    {
        final StringBuilder result = new StringBuilder();
        failedRulesWithLinks.forEach((id, links) -> 
                result.append(StringUtils.LF)
                .append(id)
                .append(":")
                .append(StringUtils.LF)
                .append(StringUtils.join(links, StringUtils.LF)));
        return result.toString();
    }
}
