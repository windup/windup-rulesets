package org.hornetq.jms.example;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.InitialContext;

import org.hornetq.api.core.management.HornetQServerControl;
import org.hornetq.api.core.management.ObjectNameBuilder;
import org.hornetq.common.example.HornetQExample;

/**
 * An example that shows how to kick off a client connected to HornetQ by using JMX.
 *
 * @author <a href="mailto:jmesnil@redhat.com">Jeff Mesnil</a>
 */
public class ClientKickoffExample extends HornetQExample
{
    private static final String JMX_URL = "service:jmx:rmi:///jndi/rmi://localhost:3000/jmxrmi";

    public static void main(final String[] args)
    {
        new ClientKickoffExample().run(args);
    }

    @Override
    public boolean runExample() throws Exception
    {
        QueueConnection connection = null;
        InitialContext initialContext = null;
        try
        {
            // Step 1. Create an initial context to perform the JNDI lookup.
            initialContext = getContext(0);

            QueueConnectionFactory cf = (QueueConnectionFactory) initialContext.lookup("/ConnectionFactory");

            connection = cf.createQueueConnection();

            final AtomicReference<JMSException> exception = new AtomicReference<JMSException>();
            connection.setExceptionListener(new ExceptionListener()
            {
                @Override
                public void onException(final JMSException e)
                {
                    exception.set(e);
                }
            });

            connection.start();

            ObjectName on = ObjectNameBuilder.DEFAULT.getHornetQServerObjectName();
            JMXConnector connector = JMXConnectorFactory.connect(new JMXServiceURL(JMX_URL), new HashMap<String, String>());
            MBeanServerConnection mbsc = connector.getMBeanServerConnection();
            HornetQServerControl serverControl = MBeanServerInvocationHandler.newProxyInstance(mbsc,
                        on,
                        HornetQServerControl.class,
                        false);

            System.out.println("List of remote addresses connected to the server:");
            System.out.println("----------------------------------");
            String[] remoteAddresses = serverControl.listRemoteAddresses();
            for (String remoteAddress : remoteAddresses)
            {
                System.out.println(remoteAddress);
            }
            System.out.println("----------------------------------");

            serverControl.closeConnectionsForAddress(remoteAddresses[0]);

            Thread.sleep(1000);

            System.err.println("\nException received from the server:");
            System.err.println("----------------------------------");
            exception.get().printStackTrace();
            System.err.println("----------------------------------");

            return true;
        }
        finally
        {
            // Step 10. Be sure to close the resources!
            if (initialContext != null)
            {
                initialContext.close();
            }
            if (connection != null)
            {
                connection.close();
            }
        }
    }

}
