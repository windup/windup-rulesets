package windup.samples.ibm.mqe;

import java.util.Properties;
import weblogic.net.http.HttpsURLConnection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class JmsContextFinder
{
    private static final String FACTORY
            = "com.ibm.mq.jms.context.WMQInitialContextFactory";
    private static final String MQ_URL = "localhost:1414/SYSTEM.DEF.SVRCONN";
    private static final String CONSTANT = com.ibm.msg.client.wmq.WMQConstants.WMQ_MESSAGE_RETENTION;

    private HttpsURLConnection foo;


    protected Context getInitialContext() throws NamingException
    {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
        props.put(Context.PROVIDER_URL, MQ_URL);
        return new InitialContext(props);
    }

}
