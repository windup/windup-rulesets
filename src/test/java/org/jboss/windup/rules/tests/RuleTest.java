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
    private boolean sourceMode = true;

    /**
     * Contains the path to the test data. This should be a relative path, relative to the file that contains the test itself (the *.windup.text.xml
     * file).
     */
    public void setTestDataPath(String testDataPath)
    {
        this.testDataPath = testDataPath;
    }

    /**
     * Contains the path to the test data. This should be a relative path, relative to the file that contains the test itself (the *.windup.text.xml
     * file).
     */
    public String getTestDataPath()
    {
        return testDataPath;
    }

    public List<String> getRulePaths()
    {
        return Collections.unmodifiableList(rulePaths);
    }

    public void addRulePath(String rulePath)
    {
        rulePaths.add(rulePath);
    }

    public void setSourceMode(boolean value) {sourceMode=value;}

    public boolean isSourceMode() { return sourceMode;}
}
