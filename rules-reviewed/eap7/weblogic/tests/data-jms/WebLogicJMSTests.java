// Contains code that should match for the following tests:
//
// weblogic-04000-test
// weblogic-05000-test
// weblogic-06000-test
// weblogic-07000-test
// weblogic-08000-test
//

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import oracle.jms.AQjmsConnection;
import oracle.jms.AQjmsConnectionFactory;
import oracle.jms.AQjmsTopicConnectionFactory;
import oracle.jms.AQjmsQueueConnectionFactory;
import oracle.jms.AQjmsConsumer;
import oracle.jms.AQjmsDestination;
import oracle.jms.AQjmsSession;
import oracle.jms.AQjmsQueueBrowser;
import oracle.jms.AQjmsProducer;

import oracle.jms.AQjmsTextMessage;
import oracle.jms.AQjmsStreamMessage;
import oracle.jms.AQjmsObjectMessage;
import oracle.jms.AQjmsMapMessage;
import oracle.jms.AQjmsBytesMessage;
import oracle.jms.AQjmsMessage;

class WebLogicJMSTests
{
    public static void jmsSession()
    {
        AQjmsTopicConnectionFactory topicConnectionFactory = null;
        AQjmsQueueConnectionFactory queueConnectionFactory = null;
        AQjmsConnection topicConnection = null;
        TopicSession topicSession = null;
        TopicSession jmsSession;
        TopicSubscriber subscriber;
        Topic shipped_orders;
        int port = 5521;
        AQjmsAgent remoteAgent;

        /* create connection and session */
        topicConnectionFactory = AQjmsFactory.getTopicConnectionFactory("HOSTNAME", "SESSIONID", port, "oci8");
        topicConnection = topicConnectionFactory.createTopicConnection("jmstopic", "jmstopic");
        jmsSession = topicConnection.createTopicSession(true, Session.CLIENT_ACKNOWLEDGE);

        shipped_orders = ((AQjmsSession) jmsSession).getTopic("E", "TOPIC_NAME");

        remoteAgent = new AQjmsAgent("AGENT_NAME", "WS.TOPIC_NAME", null);
        subscriber = ((AQjmsSession) jmsSession).createRemoteSubscriber(shipped_orders, remoteAgent, null);
    }

    private static Context getContext() throws NamingException
    {
        Properties environment = new Properties();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        environment.put(Context.PROVIDER_URL, "t3://localhost:7001");
        Context context = new InitialContext(environment);

        return context;
    }

    public void messageDestination(QueueSession session)
    {
        Destination destination;
        destination = (AQjmsDestination) session.createDestination("", "");
    }

    public void messageProducer(QueueSession session)
    {
        AQjmsProducer producer;
        producer = (AQjmsProducer) session.createPublisher(null);
    }

    public void messageConsumer(QueueSession session)
    {
        AQjmsConsumer producer;
        producer = (AQjmsConsumer) session.createConsumer(null);
    }

    public void messages(QueueSession session)
    {
        AQjmsObjectMessage message = null;
        AQjmsTextMessage message = session.createMessage();
        AQjmsStreamMessage message = null;
        AQjmsMapMessage message = null;
        AQjmsBytesMessage message = null;
        AQjmsMessage message = null;
    }

    public void queueBrowser(QueueSession session)
    {
        AQjmsQueueBrowser browser;
        Queue queue;
        AQjmsObjectMessage message;
        Enumeration messages;

        queue = ((AQjmsSession) session).getQueue("E", "QUEUE_NAME");
        browser = session.createBrowser(queue, "ID");

        for (messages = browser.elements(); message.hasMoreElements();)
        {
            obj_message = (ObjectMessage) message.nextElement();
        }
    }
}