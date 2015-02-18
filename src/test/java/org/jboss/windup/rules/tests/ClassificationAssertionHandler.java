package org.jboss.windup.rules.tests;

import static org.joox.JOOX.$;

import org.apache.commons.lang.StringUtils;
import org.jboss.windup.config.exception.ConfigurationException;
import org.jboss.windup.config.parser.ElementHandler;
import org.jboss.windup.config.parser.NamespaceElementHandler;
import org.jboss.windup.config.parser.ParserContext;
import org.jboss.windup.util.exception.WindupException;
import org.w3c.dom.Element;

/**
 * Creates a {@link ClassificationAssertion} that searches for the given classification text. Example usage:
 *
 * <pre>
 *     &lt;rule&gt;
 *         &lt;when&gt;
 *             &lt;not&gt;
 *                 &lt;classification-assertion classification="JOnAS Web Descriptor" in="filename"/&gt;
 *             &lt;/not&gt;
 *         &lt;/when&gt;
 *         &lt;perform&gt;
 *             [...]
 *         &lt;/perform&gt;
 *     &lt;/rule&gt;
 * </pre>
 * 
 * @author jsightler
 *
 */
@NamespaceElementHandler(elementName = ClassificationAssertionHandler.ELEMENT_NAME, namespace = "http://windup.jboss.org/v1/xml")
public class ClassificationAssertionHandler implements ElementHandler<ClassificationAssertion>
{
    static final String ELEMENT_NAME = "classification-assertion";
    private static final String CLASSIFICATION = "classification";

    @Override
    public ClassificationAssertion processElement(ParserContext handlerManager, Element element) throws ConfigurationException
    {
        String classificationPattern = $(element).attr(CLASSIFICATION);
        String in = $(element).attr("in");

        if (StringUtils.isBlank(classificationPattern))
        {
            throw new WindupException("Error, '" + ELEMENT_NAME + "' element must have a non-empty '" + CLASSIFICATION + "' attribute");
        }

        ClassificationAssertion classificationAssertion = ClassificationAssertion.withClassification(classificationPattern);
        return classificationAssertion.in(in);
    }
}
