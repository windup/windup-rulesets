package windup.samples.ibm.mqe;

import com.ibm.mqe.jms.MQeJMSException;
import com.ibm.mqe.jms.MQeQueue;
import com.ibm.mqe.jms.MQeQueueConnection;
import com.ibm.mqe.jms.MQeQueueConnectionFactory;
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
