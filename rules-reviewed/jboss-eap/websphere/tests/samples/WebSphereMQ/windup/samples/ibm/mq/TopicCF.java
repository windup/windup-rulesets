package windup.samples.ibm.mq;

import com.ibm.mq.jms.MQJMSException;
import com.ibm.mq.jms.MQTopic;
import com.ibm.mq.jms.MQTopicConnection;
import com.ibm.mq.jms.MQTopicConnectionFactory;
import javax.naming.Context;
import javax.naming.NamingException;


public class TopicCF extends JmsContextFinder
{

    private static final String TCF_NAME = "SampleTCF";
    private static final String TOPIC_NAME = "SampleTopic";


    public MQTopicConnection getConnection()
            throws NamingException, MQJMSException
    {
        Context context = getInitialContext();
        MQTopicConnectionFactory qcf
                = (MQTopicConnectionFactory) context.lookup(TCF_NAME);
        return qcf.createTopicConnection();
    }


    public MQTopic getTopic() throws NamingException
    {
        Context context = getInitialContext();
        return (MQTopic) context.lookup(TOPIC_NAME);
    }

}
