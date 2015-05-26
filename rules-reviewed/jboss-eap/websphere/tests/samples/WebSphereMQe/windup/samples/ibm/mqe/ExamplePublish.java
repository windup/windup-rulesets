package windup.samples.ibm.mq;

import java.io.IOException;

import com.ibm.mqe.jms.MQeJMSException;
import com.ibm.mqe.jms.MQeMessage;
import com.ibm.mqe.jms.MQeSession;
import com.ibm.mqe.jms.MQeConnection;
import com.ibm.mqe.jms.MQeTopicConnection;
import com.ibm.mqe.jms.MQeTopicPublisher;
import com.ibm.mqe.jms.MQeTopicSession;
import javax.naming.NamingException;


public class ExamplePublish
{

    private TopicCF utils;
    private MQeTopicConnection connection;
    private MQeTopicSession session;
    private MQeTopicPublisher publisher;


    public static void main(String[] args)
            throws NamingException, MQeJMSException, IOException
    {
        ExamplePublish publisher = new ExamplePublish();
        publisher.connect();
        String message = "ignored";
        while( message.length() > 0 )
        {
            byte[] input = new byte[40];
            System.out.print("Enter a message: ");
            System.in.read(input);
            message = (new String(input, 0, input.length)).trim();
            if( message.length() > 0 )
                publisher.sendMessage(message);
        }
        publisher.disconnect();
    }


    private ExamplePublish()
    {
        utils = new TopicCF();
    }


    private void connect() throws NamingException, MQeJMSException
    {
        connection = utils.getConnection();
        session
                = connection.createTopicSession(false, MQeSession.AUTO_ACKNOWLEDGE);
        publisher = session.createPublisher(utils.getTopic());
        System.out.println("Publisher started.");
    }


    private void sendMessage(String text) throws MQeJMSException
    {
        MQeMessage message = session.createTextMessage(text);
        publisher.publish(message);
        System.out.println(
                "Published message <"
                + text
                + "> with ID <"
                + message.getJMSMessageID()
                + ">");
    }


    private void disconnect() throws MQeJMSException
    {
        publisher.close();
        session.close();
        connection.close();
        System.out.println("Publisher stopped.");
    }

}
