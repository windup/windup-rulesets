// Contains code that should match for the following tests:
//
// weblogic-01300-test
//
import java.util.Hashtable;
import javax.naming.InitialContext;

class JNDITest
{
    public long schedule(long time)
    {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
        env.put("java.naming.provider.url", "t3://localhost:7001");
        InitialContext context = new InitialContext(env);
    }

    public void trigger()
    {
        System.out.println("trigger pulled");
    }
}
