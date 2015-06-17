package tests.samples.websphere.mqe;

import com.ibm.mqe.jms.MQeBytesMessage;
import com.ibm.mqe.jms.MQeConnection;
import com.ibm.mqe.jms.MQeConnectionFactory;
import com.ibm.mqe.jms.MQeConnectionMetaData;
import com.ibm.mqe.jms.MQeDestination;
import com.ibm.mqe.jms.MQeJMSJNDIQueue;
import com.ibm.mqe.jms.MQeMapMessage;
import com.ibm.mqe.jms.MQeMessageConsumer;
import com.ibm.mqe.jms.MQeMessageProducer;
import com.ibm.mqe.jms.MQeObjectMessage;
import com.ibm.mqe.jms.MQeQueue;
import com.ibm.mqe.jms.MQeQueueBrowser;
import com.ibm.mqe.jms.MQeQueueConnection;
import com.ibm.mqe.jms.MQeQueueConnectionFactory;
import com.ibm.mqe.jms.MQeQueueReceiver;
import com.ibm.mqe.jms.MQeQueueSender;
import com.ibm.mqe.jms.MQeQueueSession;
import com.ibm.mqe.jms.MQeSession;
import com.ibm.mqe.jms.MQeStreamMessage;
import com.ibm.mqe.jms.MQeTemporaryQueue;
import com.ibm.mqe.jms.MQeTemporaryTopic;
import com.ibm.mqe.jms.MQeTextMessage;
import com.ibm.mqe.jms.MQeTopic;
import com.ibm.mqe.jms.MQeTopicConnection;
import com.ibm.mqe.jms.MQeTopicConnectionFactory;
import com.ibm.mqe.jms.MQeTopicPublisher;
import com.ibm.mqe.jms.MQeTopicSession;
import com.ibm.mqe.jms.MQeTopicSubscriber;
import com.ibm.mqe.jms.MQeXAConnection;
import com.ibm.mqe.jms.MQeXAConnectionFactory;
import com.ibm.mqe.jms.MQeXAQueueConnection;
import com.ibm.mqe.jms.MQeXAQueueConnectionFactory;
import com.ibm.mqe.jms.MQeXAQueueSession;
import com.ibm.mqe.jms.MQeXASession;
import com.ibm.mqe.jms.MQeXATopicConnection;
import com.ibm.mqe.jms.MQeXATopicConnectionFactory;
import com.ibm.mqe.jms.MQeXATopicSession;
import com.ibm.mqe.adapters.MQeDiskFieldsAdapter;
import com.ibm.mqe.administration.MQeQueueAdminMsg;
import com.ibm.mqe.jms.MQeJMSMsgFieldNames;


public class AllClasses
{

    private int a = com.ibm.mqe.jms.MQeJMSMsgFieldNames.MQe_JMS_PROPERTIES;


    public void doSomething()
    {
        new MQeConnection();
        new MQeQueueConnection();
        new MQeConnectionFactory();
        new MQeQueueConnectionFactory();
        new MQeConnectionMetaData();
        new MQeDestination();
        new MQeJMSJNDIQueue();
        new MQeQueue();
        new MQeQueue();
        new MQeTemporaryQueue();
        new MQeTemporaryQueue();
        new MQeBytesMessage();
        new MQeMapMessage();
        new MQeObjectMessage();
        new MQeStreamMessage();
        new MQeTextMessage();
        new MQeMessageConsumer();
        new MQeQueueReceiver();
        new MQeMessageProducer();
        new MQeQueueSender();
        new MQeQueueBrowser();
        new MQeSession();
        new MQeQueueSession();
        new MQeQueueConnectionFactory();
        new MQeXAQueueConnectionFactory();
        new MQeTopicConnectionFactory();
        new MQeXATopicConnectionFactory();
        new MQeXAConnectionFactory();
        new MQeDestination();
        new MQeQueue();
        new MQeTemporaryQueue();
        new MQeTopic();
        new MQeTemporaryTopic();
        new MQeConnection();
        new MQeQueueConnection();
        new MQeXAQueueConnection();
        new MQeTopicConnection();
        new MQeXATopicConnection();
        new MQeXAConnection();
        new MQeConnectionMetaData();
        new MQeMessageConsumer();
        new MQeQueueReceiver();
        new MQeTopicSubscriber();
        new MQeMessageProducer();
        new MQeQueueSender();
        new MQeTopicPublisher();
        new MQeQueueBrowser();
        new MQeSession();
        new MQeQueueSession();
        new MQeTopicSession();
        new MQeXASession();
        new MQeXAQueueSession();
        new MQeXATopicSession();
        new MQeQueueAdminMsg();

        // INHERITANCE
        new MQeDiskFieldsAdapter()
        {
        };
    }


    public static class MyAdapter extends MQeDiskFieldsAdapter
    {
    }


    public static class MyMQeQueueAdminMsg extends MQeQueueAdminMsg
    {
    }

}
