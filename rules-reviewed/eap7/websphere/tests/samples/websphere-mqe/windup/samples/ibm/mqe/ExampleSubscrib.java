package windup.samples.ibm.mq;

import java.io.IOException;

import com.ibm.mqe.jms.MQeJMSException;
import com.ibm.mqe.jms.MQeMessage;
import com.ibm.mqe.jms.MQeMessageListener;
import com.ibm.mqe.jms.MQeSession;
import com.ibm.mqe.jms.MQeTextMessage;
import com.ibm.mqe.jms.MQeTopicConnection;
import com.ibm.mqe.jms.MQeTopicSession;
import com.ibm.mqe.jms.MQeTopicSubscriber;
import javax.naming.NamingException;


public class ExampleSubscrib implements MQeMessageListener
{

    private TopicCF utils;
    private MQeTopicConnection connection;
    private MQeTopicSession session;
    private MQeTopicSubscriber subscriber;


    public static void main(String[] args)
            throws NamingException, MQeJMSException, IOException
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


    private void connect() throws NamingException, MQeJMSException
    {
        connection = utils.getConnection();
        session
                = connection.createTopicSession(false, MQeSession.AUTO_ACKNOWLEDGE);
        subscriber = session.createSubscriber(utils.getTopic());
        subscriber.setMessageListener(this);
        connection.start();
        System.out.println("Started a subscriber.");
    }


    public void onMessage(MQeMessage message)
    {
        try
        {
            MQeTextMessage tMessage = (MQeTextMessage) message;
            String text;
            text = tMessage.getText();
            System.out.println(
                    "Message received: "
                    + text
                    + " of ID: "
                    + message.getJMSMessageID());
        }
        catch( MQeJMSException e )
        {
            e.printStackTrace();
        }
    }


    private void disconnect() throws MQeJMSException
    {
        subscriber.close();
        session.close();
        connection.stop();
        connection.close();
        System.out.println("Stopped subscriber.");
    }

}
