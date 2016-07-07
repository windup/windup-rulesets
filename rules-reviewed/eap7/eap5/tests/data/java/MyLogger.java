package logger;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Cause;
import org.jboss.logging.LogMessage;
import org.jboss.logging.Message;
import org.jboss.logging.MessageLogger;

import static org.jboss.logging.Logger.Level.FATAL;

@MessageLogger(projectCode = "FOOBAR")
public interface MyLogger extends BasicLogger {
    @LogMessage(level = FATAL)
    @Message(id = 1, value = "Foo bar!")
    void foobar(@Cause Throwable t);
}
