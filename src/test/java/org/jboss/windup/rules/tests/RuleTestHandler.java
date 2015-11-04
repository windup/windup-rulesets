package org.jboss.windup.rules.tests;

import static org.joox.JOOX.$;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.windup.config.exception.ConfigurationException;
import org.jboss.windup.config.parser.ElementHandler;
import org.jboss.windup.config.parser.NamespaceElementHandler;
import org.jboss.windup.config.parser.ParserContext;
import org.jboss.windup.util.exception.WindupException;
import org.w3c.dom.Element;

/**
 *
 * This describes all of the metadata needed to run a test on a particular set of rules. Example usage:
 *
 * The rulePath element is optional. If it is not specified, the default will be "../", which will run all rules in the parent directory of this test.
 *
 * <pre>
 * &lt;ruletest xmlns=&quot;http://windup.jboss.org/schema/jboss-ruletest&quot;&gt;
 *  &lt;testDataPath&gt;data&lt;/testDataPath&gt;
 *  &lt;rulePath&gt;directory&lt;/rulePath&gt;
 *  &lt;ruleset&gt;
 *      &lt;rules&gt;
 *          &lt;rule&gt;
 *              &lt;when&gt;
 *                  &lt;not&gt;
 *                      &lt;classification-exists classification=&quot;JOnAS Web Descriptor&quot;/&gt;
 *                  &lt;/not&gt;
 *              &lt;/when&gt;
 *              &lt;perform&gt;
 *                  &lt;fail message=&quot;JOnAS Web Descriptor is missing&quot;/&gt;
 *              &lt;/perform&gt;
 *          &lt;/rule&gt;
 *      &lt;/rules&gt;
 *   &lt;/ruleset&gt;
 * &lt;/ruletest&gt;
 * </pre>
 *
 * @author jsightler
 *
 */
@NamespaceElementHandler(elementName = RuleTestHandler.RULETEST, namespace = "http://windup.jboss.org/schema/jboss-ruletest")
public class RuleTestHandler implements ElementHandler<RuleTest>
{
    public static final String RULETEST = "ruletest";
    public static final String TEST_DATA_PATH = "testDataPath";
    public static final String RULE_PATH = "rulePath";
    public static final String SOURCE_MODE = "sourceMode";
    public static final String RULESET = "ruleset";

    @Override
    public RuleTest processElement(ParserContext context, Element element) throws ConfigurationException
    {
        RuleTest ruleTest = new RuleTest();

        List<Element> children = $(element).children().get();
        for (Element child : children)
        {
            if (child.getNodeName().equals(TEST_DATA_PATH))
            {
                ruleTest.setTestDataPath(child.getTextContent().trim());
            }
            else if (child.getNodeName().equals(RULE_PATH))
            {
                ruleTest.addRulePath(child.getTextContent().trim());
            }
            else if(child.getNodeName().equals(SOURCE_MODE))
            {
                ruleTest.setSourceMode(Boolean.valueOf(child.getTextContent()));
            }
            else if (child.getNodeName().equals(RULESET))
            {
                ruleTest.addRulePath(child.getTextContent().trim());
            } else {
                context.processElement(child);
            }
        }

        if (StringUtils.isEmpty(ruleTest.getTestDataPath()))
        {
            throw new WindupException("Failed to parse, as the '" + TEST_DATA_PATH + "' element is required, and cannot be empty!");
        }

        return ruleTest;
    }
}
