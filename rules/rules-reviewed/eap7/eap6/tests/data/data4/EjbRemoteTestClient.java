package com.example.myapp;

public class EjbRemoteTestClient
{


    void doLookup()
    {

        Properties jndiProps = new Properties();
        jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        jndiProps.put(Context.PROVIDER_URL,"remote://localhost:4447"); // this is url to migrate
        jndiProps.put(Context.PROVIDER_URL,"http-remoting://1.2.3.4:8080"); // this is correct url already migrated
        // create a context passing these properties
        Context ctx = new InitialContext(jndiProps);
        // lookup
        ctx.lookup("foo/bar");

    }
}
