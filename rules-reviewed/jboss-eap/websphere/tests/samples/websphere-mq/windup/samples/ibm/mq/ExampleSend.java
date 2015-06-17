package windup.samples.ibm.mq;

import java.io.IOException;

import com.ibm.mq.jms.MQJMSException;
import com.ibm.mq.jms.MQMessage;
import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueSender;
import com.ibm.mq.jms.MQQueueSession;
import com.ibm.mq.jms.MQSession;
import javax.naming.NamingException;


public class ExampleSend
{

    private PeerUtilities utils;
    private MQQueueConnection connection;
    private MQQueueSession session;
    private MQQueueSender sender;


    public static void main(String[] args)
            throws NamingException, MQJMSException, IOException
    {
        ExampleSend sender = new ExampleSend();
        sender.connect();
        String message = "ignored";
        while( message.length() > 0 )
        {
            byte[] input = new byte[40];
            System.out.print("Your message: ");
            System.in.read(input);
            message = (new String(input, 0, input.length)).trim();
            if( message.length() > 0 )
                sender.sendMessage(message);
        }
        sender.disconnect();
    }


    private ExampleSend()
    {
        utils = new PeerUtilities();
    }


    private void connect() throws NamingException, MQJMSException
    {
        connection = utils.getConnection();
        session
                = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        sender = session.createSender(utils.getQueue());
        System.out.println("Sender started.");
    }


    private void sendMessage(String text) throws MQJMSException
    {
        MQMessage message = session.createTextMessage(text);
        sender.send(message);
            System.out.println(
                    "Message sent: "
                    + text
                    + " of ID: "
                    + message.getJMSMessageID());
    }


    private void disconnect() throws MQJMSException
    {
        sender.close();
        session.close();
        connection.close();
        System.out.println("Stopped sender.");
    }

}
