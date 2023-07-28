import org.jboss.naming.remote.client.InitialContextFactory;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.util.Hashtable;

public class Elytron {
    public static void main(String[] args) {
        InitialContextFactory initialContextFactory = new InitialContextFactory() {
            @Override
            public Context getInitialContext(Hashtable<?, ?> hashtable) throws NamingException {
                return null;
            }
        }
    }
}