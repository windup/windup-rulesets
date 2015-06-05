import com.ibm.websphere.sib.api.jms.JmsQueue;
import com.ibm.websphere.sib.api.jms.JmsConnectionFactory;
import com.ibm.websphere.sib.api.jms.JmsQueueConnectionFactory;
import com.ibm.websphere.sib.api.jms.JmsTopicConnectionFactory;
import com.ibm.websphere.sib.api.jms.JmsFactoryFactory;
import com.ibm.websphere.sib.api.jms.JmsTopic;
import com.ibm.websphere.sib.api.jms.JmsMsgProducer;
import com.ibm.websphere.sib.api.jms.JmsMsgConsumer;
import com.ibm.websphere.sib.api.jms.JmsDestination;
import com.ibm.websphere.sib.api.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsConnection;
import com.ibm.msg.client.jms.JmsConnectionMetaData;
import ibm.com.msg.client.jms.JmsDestination;
import ibm.com.msg.client.jms.JmsMessageConsumer;
import ibm.com.msg.client.jms.JmsMessageProducer;
import com.ibm.msg.client.jms.JmsQueue;
import com.ibm.msg.client.jms.JmsQueueBrowser;
import com.ibm.msg.client.jms.JmsQueueConnection;
import com.ibm.msg.client.jms.JmsQueueConnectionFactory;
import com.ibm.msg.client.jms.JmsQueueReceiver;
import com.ibm.msg.client.jms.JmsQueueSender;
import com.ibm.msg.client.jms.JmsQueueSession;
import com.ibm.msg.client.jms.JmsSession;
import com.ibm.msg.client.jms.JmsTemporaryQueue;
import com.ibm.msg.client.jms.JmsTemporaryTopic;
import com.ibm.msg.client.jms.JmsTopic;
import com.ibm.msg.client.jms.JmsTopicConnection;
import com.ibm.msg.client.jms.JmsTopicConnectionFactory;
import com.ibm.msg.client.jms.JmsTopicPublisher;
import com.ibm.msg.client.jms.JmsTopicSession;
import com.ibm.msg.client.jms.JmsTopicSubscriber;
import com.ibm.msg.client.jms.JmsXAConnection;
import com.ibm.msg.client.jms.JmsXAConnectionFactory;
import com.ibm.msg.client.jms.JmsXAQueueConnection;
import com.ibm.msg.client.jms.JmsXAQueueConnectionFactory;
import com.ibm.msg.client.jms.JmsXAQueueSession;
import com.ibm.msg.client.jms.JmsXASession;
import com.ibm.msg.client.jms.JmsXATopicConnection;
import com.ibm.msg.client.jms.JmsXATopicConnectionFactory;
import com.ibm.msg.client.jms.JmsXATopicSession;
import com.ibm.jms.JMSBytesMessage;
import com.ibm.jms.JMSMapMessage;
import com.ibm.jms.JMSMessage;
import com.ibm.jms.JMSObjectMessage;
import com.ibm.jms.JMSStreamMessage;
import com.ibm.jms.JMSTextMessage;

/**
 * Testing detection of com.ibm.websphere.sib.api.jms.JmsConnectionFactory
 */
public class JmsTopicConnectionFactory
{
    public static void main(String[] args) throws IOException, JMSException
    {
        JmsFactoryFactory fact = JmsFactoryFactory.getInstance();
        JmsConnectionFactory connectionFact = fact.createConnectionFactory();

        connFact.setBusName("busName");
        connFact.setProviderEndpoints("providerEndpoints");
        connFact.setTargetTransportChain("transporationChain");

        Connection connection = connectionFact.createConnection();
    }

}
