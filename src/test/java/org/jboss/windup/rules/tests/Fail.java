package org.jboss.windup.rules.tests;

import org.jboss.windup.config.GraphRewrite;
import org.jboss.windup.config.operation.GraphOperation;
import org.ocpsoft.rewrite.config.Rule;
import org.ocpsoft.rewrite.context.EvaluationContext;

/**
 * Throws a {@link WindupAssertionException} indicating that execution should stop.
 *
 */
public class Fail extends GraphOperation
{
    private String message;

    private Fail(String message)
    {
        this.message = message;
    }

    /**
     * Indicates that message that should be thrown with the {@link WindupAssertionException}.
     */
    public static Fail fail(String message)
    {
        return new Fail(message);
    }

    @Override
    public void perform(GraphRewrite event, EvaluationContext context)
    {
        Rule rule = (Rule) context.get(Rule.class);
        String prefix = "(Rule: " + rule.getId() + ") Assertion failed";

        String message = this.message == null ? prefix + "!" : prefix + ": " + this.message;
        throw new WindupAssertionException(message);
    }
}
