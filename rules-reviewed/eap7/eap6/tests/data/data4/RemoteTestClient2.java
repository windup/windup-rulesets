package com.example.ejb.remoting.client;


import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class RemoteEJBClient
{
    private final Context context;

    public RemoteEJBClient() throws NamingException
    {
        context = initializeEJBClientContext();
    }

    private void initializeEJBClientContext()
    {
        Properties properties = new Properties();

        properties.put("endpoint.name", "client-endpoint");

        properties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");

        properties.put("remote.connections", "default");
        properties.put("remote.connection.default.host", "localhost");
        properties.put("remote.connection.default.port", "4447"); // this is port number to migrate
        properties.put("remote.connection.default.port", "8080"); // this is correct port number
        properties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");

        PropertiesBasedEJBClientConfiguration configuration =
                new PropertiesBasedEJBClientConfiguration(properties);

        final ContextSelector<EJBClientContext> ejbClientContextSelector =
                new ConfigBasedEJBClientContextSelector(configuration);

        final ContextSelector<EJBClientContext> previousSelector =
                EJBClientContext.setSelector(ejbClientContextSelector);
    }

}
