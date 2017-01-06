package tests.samples.websphere.mq;

import com.ibm.msg.client.jms.JmsXATopicConnectionFactory;

import com.ibm.mq.jms.MQTopicConnectionFactory;
import com.ibm.mq.jms.MQXATopicConnectionFactory;
import com.ibm.websphere.sib.api.jms.JmsTopicConnectionFactory;
import com.ibm.websphere.sib.api.jms.JmsQueueConnectionFactory;
import com.ibm.msg.client.jms.JmsXATopicConnectionFactory;
import com.ibm.msg.client.jms.JmsXATopicConnectionFactory;
//import com.ibm.msg.client.jms.JmsQueueConnectionFactory;

public class AllClasses2
{
    public void a()
    {
        //new TopicConnectionFactory();
        //new XATopicConnectionFactory();
        //new MQTopicConnectionFactory();
        new MQXATopicConnectionFactory();
        new JmsTopicConnectionFactory();
        new JmsXATopicConnectionFactory();
    }

}
