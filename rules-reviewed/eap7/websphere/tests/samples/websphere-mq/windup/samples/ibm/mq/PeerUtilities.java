package windup.samples.ibm.mq;

import com.ibm.mq.jms.MQJMSException;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import javax.naming.Context;
import javax.naming.NamingException;


public class PeerUtilities extends MQJmsUtils
{

    private static final String MY_QCF_NAME = "MyQueueConnFctory";
    private static final String QUEUE = "MyQueue";


    public MQQueueConnection getConnection()
            throws NamingException, MQJMSException
    {
        Context context = getInitialContext();
        MQQueueConnectionFactory qcf
                = (MQQueueConnectionFactory) context.lookup(MY_QCF_NAME);
        return qcf.createQueueConnection();
    }


    public MQQueue getQueue() throws NamingException
    {
        ;
        return (MQQueue) getInitialContext().context.lookup(QUEUE);
    }

}
