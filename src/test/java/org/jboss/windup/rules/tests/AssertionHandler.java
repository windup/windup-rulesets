package org.jboss.windup.rules.tests;

import static org.joox.JOOX.$;

import org.jboss.windup.config.exception.ConfigurationException;
import org.jboss.windup.config.parser.ElementHandler;
import org.jboss.windup.config.parser.NamespaceElementHandler;
import org.jboss.windup.config.parser.ParserContext;
import org.w3c.dom.Element;

/**
 * Creates a {@link Assertion} object with the given message. Example usage:
 *
 * <pre>
 *     &lt;rule&gt;
 *         &lt;when&gt;
 *             [...]
 *         &lt;/when&gt;
 *         &lt;perform&gt;
 *             &lt;assertion message="JOnAS Web Descriptor is missing"/&gt;
 *         &lt;/perform&gt;
 *     &lt;/rule&gt;
 * </pre>
 * 
 * @author jsightler
 *
 */
@NamespaceElementHandler(elementName = AssertionHandler.ELEMENT_NAME, namespace = "http://windup.jboss.org/v1/xml")
public class AssertionHandler implements ElementHandler<Assertion>
{
    static final String ELEMENT_NAME = "assertion";
    private static final String MESSAGE = "message";

    @Override
    public Assertion processElement(ParserContext handlerManager, Element element) throws ConfigurationException
    {
        String message = $(element).attr(MESSAGE);
        return Assertion.fail(message);
    }
}
