package windup.samples.ibm.mq;

import java.io.IOException;

import com.ibm.mq.jms.MQJMSException;
import com.ibm.mq.jms.MQMessage;
import com.ibm.mq.jms.MQMessageListener;
import com.ibm.mq.jms.MQSession;
import com.ibm.mq.jms.MQTextMessage;
import com.ibm.mq.jms.MQTopicConnection;
import com.ibm.mq.jms.MQTopicSession;
import com.ibm.mq.jms.MQTopicSubscriber;
import javax.naming.NamingException;


public class ExampleSubscrib implements MQMessageListener
{

    private TopicCF utils;
    private MQTopicConnection connection;
    private MQTopicSession session;
    private MQTopicSubscriber subscriber;


    public static void main(String[] args)
            throws NamingException, MQJMSException, IOException
    {
        ExampleSubscrib subscriber = new ExampleSubscrib();
        subscriber.connect();
        System.in.read();
        subscriber.disconnect();
    }


    private ExampleSubscrib()
    {
        utils = new TopicCF();
    }


    private void connect() throws NamingException, MQJMSException
    {
        connection = utils.getConnection();
        session
                = connection.createTopicSession(false, MQSession.AUTO_ACKNOWLEDGE);
        subscriber = session.createSubscriber(utils.getTopic());
        subscriber.setMessageListener(this);
        connection.start();
    }


    public void onMessage(MQMessage message)
    {
        try
        {
            MQTextMessage tMessage = (MQTextMessage) message;
            String text;
            text = tMessage.getText();
            System.out.println(
                    "Message received: "
                    + text
                    + " of ID: "
                    + message.getJMSMessageID());
        }
        catch( MQJMSException e )
        {
            e.printStackTrace();
        }
    }


    private void disconnect() throws MQJMSException
    {
        subscriber.close();
        session.close();
        connection.stop();
        connection.close();
        System.out.println("Stopped subscriber.");
    }

}
