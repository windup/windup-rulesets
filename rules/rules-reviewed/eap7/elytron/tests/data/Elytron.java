import org.jboss.naming.remote.client.InitialContextFactory;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.util.Hashtable;

public class Elytron {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        properties.put(Context.PROVIDER_URL,"http-remoting://127.0.0.1:8080");
        properties.put(Context.SECURITY_PRINCIPAL, "bob");
        properties.put(Context.SECURITY_CREDENTIALS, "secret");
        InitialContext context = new InitialContext(properties);
    }
}