package windup.samples.ibm.mq;

import com.ibm.mqe.jms.MQeJMSException;
import com.ibm.mqe.jms.MQeTopic;
import com.ibm.mqe.jms.MQeTopicConnection;
import com.ibm.mqe.jms.MQeTopicConnectionFactory;
import javax.naming.Context;
import javax.naming.NamingException;


public class TopicCF extends JmsContextFinder
{

    private static final String TOPIC_CF_NAME = "MyTopicCF";
    private static final String TOPIC_NAME = "MyTopic";


    public MQeTopicConnection getConnection()
            throws NamingException, MQeJMSException
    {
        MQeTopicConnectionFactory qcf
                = (MQeTopicConnectionFactory) getInitialContext().context.lookup(TOPIC_CF_NAME);
        return qcf.createTopicConnection();
    }


    public MQeTopic getTopic() throws NamingException
    {
        return (MQeTopic) getInitialContext().context.lookup(TOPIC_NAME);
    }

}
