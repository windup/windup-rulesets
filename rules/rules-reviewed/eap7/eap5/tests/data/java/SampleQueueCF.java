import javax.jms.QueueConnectionFactory;
import javax.naming.InitialContext;

/**
 *  Sample class for tests.
 */
public class SampleQueueCF
{
    public void someMethod() throws Exception {
        InitialContext ctx = new InitialContext();
        QueueConnectionFactory factory = (QueueConnectionFactory)ctx.lookup("ConnectionFactory");
    }
}
