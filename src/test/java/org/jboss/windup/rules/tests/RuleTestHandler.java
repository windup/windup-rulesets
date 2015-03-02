package org.jboss.windup.rules.tests;

import static org.joox.JOOX.$;

import java.util.List;

import org.jboss.windup.config.exception.ConfigurationException;
import org.jboss.windup.config.parser.ElementHandler;
import org.jboss.windup.config.parser.NamespaceElementHandler;
import org.jboss.windup.config.parser.ParserContext;
import org.w3c.dom.Element;

/**
 *
 * This describes all of the metadata needed to run a test on a particular set of rules. Example usage:
 *
 * The rulePath element is optional. If it is not specified, the default will be "../", which will run all rules in the parent directory of this test.
 *
 * <pre>
 * &lt;ruletest xmlns=&quot;http://windup.jboss.org/v1/xml&quot;&gt;
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
@NamespaceElementHandler(elementName = "ruletest", namespace = "http://windup.jboss.org/v1/xml")
public class RuleTestHandler implements ElementHandler<RuleTest>
{
    @Override
    public RuleTest processElement(ParserContext context, Element element) throws ConfigurationException
    {
        RuleTest ruleTest = new RuleTest();

        List<Element> children = $(element).children().get();
        for (Element child : children)
        {
            if (child.getNodeName().equals("testDataPath"))
            {
                ruleTest.setTestDataPath(child.getTextContent().trim());
            }
            else if (child.getNodeName().equals("rulePath"))
            {
                ruleTest.addRulePath(child.getTextContent().trim());
            }
            else
            {
                context.processElement(child);
            }
        }

        return ruleTest;
    }
}
