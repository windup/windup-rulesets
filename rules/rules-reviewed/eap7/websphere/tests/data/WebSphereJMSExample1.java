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
import com.ibm.jms.JMSBytesMessage;
import com.ibm.jms.JMSMapMessage;
import com.ibm.jms.JMSMessage;
import com.ibm.jms.JMSObjectMessage;
import com.ibm.jms.JMSStreamMessage;
import com.ibm.jms.JMSTextMessage;

/**
 * Testing detection of com.ibm.websphere.sib.api.jms.JmsConnectionFactory
 */
public class WebSphereJMSExample
{
    public static void main(String[] args) throws IOException, JMSException
    {
        JmsConnectionFactory fact = JmsFactoryFactory.getInstance();
        JmsQueueConnectionFactory connectionFact = fact.createConnectionFactory();

        connFact.setBusName("busName");
        connFact.setProviderEndpoints("providerEndpoints");
        connFact.setTargetTransportChain("transporationChain");

        Connection connection = connectionFact.createConnection();
    }

}
