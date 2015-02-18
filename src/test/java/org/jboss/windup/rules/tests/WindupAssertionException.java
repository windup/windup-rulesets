package org.jboss.windup.rules.tests;

import org.jboss.windup.util.exception.WindupException;

/**
 * Thrown to indicate that a test has found an error. This will immediately halt the execution of that particular test case.
 * 
 * @author jsightler
 *
 */
public class WindupAssertionException extends WindupException
{
    private static final long serialVersionUID = 1L;

    public WindupAssertionException()
    {
        super();
    }

    public WindupAssertionException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public WindupAssertionException(String message)
    {
        super(message);
    }
}
