package windup.samples.ibm.mq;

import java.io.IOException;

import com.ibm.mq.jms.MQJMSException;
import com.ibm.mq.jms.MQMessage;
import com.ibm.mq.jms.MQSession;
import com.ibm.mq.jms.MQConnection;
import com.ibm.mq.jms.MQTopicConnection;
import com.ibm.mq.jms.MQTopicPublisher;
import com.ibm.mq.jms.MQTopicSession;
import javax.naming.NamingException;


public class ExamplePublish
{

    private TopicCF utils;
    private MQConnection connection2;
    private MQTopicConnection connection;
    private MQTopicConnection connection;
    private MQTopicSession session;
    private MQTopicPublisher publisher;


    public static void main(String[] args)
            throws NamingException, MQJMSException, IOException
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


    private void connect() throws NamingException, MQJMSException
    {
        connection = utils.getConnection();
        session
                = connection.createTopicSession(false, MQSession.AUTO_ACKNOWLEDGE);
        publisher = session.createPublisher(utils.getTopic());
        System.out.println("Publisher started.");
    }


    private void sendMessage(String text) throws MQJMSException
    {
        MQMessage message = session.createTextMessage(text);
        publisher.publish(message);
        System.out.println(
                "Published message <"
                + text
                + "> with ID <"
                + message.getJMSMessageID()
                + ">");
    }


    private void disconnect() throws MQJMSException
    {
        publisher.close();
        session.close();
        connection.close();
        System.out.println("Publisher stopped.");
    }

}
