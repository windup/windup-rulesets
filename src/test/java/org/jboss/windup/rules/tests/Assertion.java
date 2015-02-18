package org.jboss.windup.rules.tests;

import org.jboss.windup.config.GraphRewrite;
import org.jboss.windup.config.operation.GraphOperation;
import org.ocpsoft.rewrite.context.EvaluationContext;

/**
 * Throws a {@link WindupAssertionException} indicating that execution should stop.
 *
 */
public class Assertion extends GraphOperation
{
    private String message;

    private Assertion(String message)
    {
        this.message = message;
    }

    /**
     * Indicates that message that should be thrown with the {@link WindupAssertionException}.
     */
    public static Assertion fail(String message)
    {
        return new Assertion(message);
    }

    @Override
    public void perform(GraphRewrite event, EvaluationContext context)
    {
        String message = this.message == null ? "Assertion failed!" : "Assertion failed: " + this.message;
        throw new WindupAssertionException(message);
    }
}
