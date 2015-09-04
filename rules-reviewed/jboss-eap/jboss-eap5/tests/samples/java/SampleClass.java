
import javax.jms.QueueConnectionFactory;
import javax.naming.InitialContext;
import javax.persistence.JoinColumn;
import org.jboss.annotation.ejb.Service;
import org.jboss.annotation.ejb.Management;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.Depends;

/**
 *  Sample class for tests.
 */
@org.jboss.ejb3.annotation.Management
@Management
@Depends
@LocalBinding
@Service
public class SampleClass
{
    @JoinColumn("fkSomeEntity")
    private Object someEntity;

    public void someMethod() throws Exception {
        InitialContext ctx = new InitialContext();
        QueueConnectionFactory factory = (QueueConnectionFactory)ctx.lookup("ConnectionFactory");
    }
}
