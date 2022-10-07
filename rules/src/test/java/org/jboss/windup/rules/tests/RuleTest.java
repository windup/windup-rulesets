package org.jboss.windup.rules.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains metadata about the current test. This can include information such as where to find the test data, or any other metadata that would be
 * required for test execution.
 * 
 * @author jsightler
 *
 */
public class RuleTest
{
    private String testDataPath;
    private List<String> rulePaths = new ArrayList<>();
    private List<String> ruleIds = new ArrayList<>();
    private String source;
    private String target;

    /**
     * Gets the source technology to operate on (eg, Glassfish).
     */
    public String getSource()
    {
        return source;
    }

    /**
     * Gets the source technology to operate on (eg, Glassfish).
     */
    public void setSource(String source)
    {
        this.source = source;
    }

    /**
     * Gets the target technology to operate on (eg, eap or eap6).
     */
    public String getTarget()
    {
        return target;
    }

    /**
     * Gets the target technology to operate on (eg, eap or eap6).
     */
    public void setTarget(String target)
    {
        this.target = target;
    }

    /**
     * Contains the path to the test data. This should be a relative path, relative to the file that contains the test itself (the *.windup.text.xml
     * file).
     */
    public String getTestDataPath()
    {
        return testDataPath;
    }

    /**
     * Contains the path to the test data. This should be a relative path, relative to the file that contains the test itself (the *.windup.text.xml
     * file).
     */
    public void setTestDataPath(String testDataPath)
    {
        this.testDataPath = testDataPath;
    }

    /**
     * Contains the paths to the rules that will be executed by this test.
     */
    public List<String> getRulePaths()
    {
        return Collections.unmodifiableList(rulePaths);
    }

    /**
     * Contains the paths to the rules that will be executed by this test.
     */
    public void addRulePath(String rulePath)
    {
        rulePaths.add(rulePath);
    }

    /**
     * Indicates whether or not this particular test should be run on source code.
     *
     * @deprecated
     */
    public boolean isSourceMode()
    {
        return false;
    }

    /**
     * Indicates whether or not this particular test should be run on source code.
     *
     * @deprecated
     */
    public void setSourceMode(boolean value)
    {}

    /**
     * Contains the ids of the rules that will be executed by this test.
     */
    public List<String> getRuleIds()
    {
        return Collections.unmodifiableList(ruleIds);
    }

    /**
     * Contains the ids of the rules that will be executed by this test.
     */
    public void addRuleId(String ruleId)
    {
        ruleIds.add(ruleId);
    }
}
