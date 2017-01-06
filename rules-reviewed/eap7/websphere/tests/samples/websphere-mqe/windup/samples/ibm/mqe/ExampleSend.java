package windup.samples.ibm.mq;

import java.io.IOException;

import com.ibm.mqe.jms.MQeJMSException;
import com.ibm.mqe.jms.MQeMessage;
import com.ibm.mqe.jms.MQeQueueConnection;
import com.ibm.mqe.jms.MQeQueueSender;
import com.ibm.mqe.jms.MQeQueueSession;
import com.ibm.mqe.jms.MQeSession;
import javax.naming.NamingException;


public class ExampleSend
{

    private PeerUtilities utils;
    private MQeQueueConnection connection;
    private MQeQueueSession session;
    private MQeQueueSender sender;


    public static void main(String[] args)
            throws NamingException, MQeJMSException, IOException
    {
        ExampleSend sender = new ExampleSend();
        sender.connect();
        String message = "ignored";
        while( message.length() > 0 )
        {
            byte[] input = new byte[40];
            System.out.print("Enter a message: ");
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


    private void connect() throws NamingException, MQeJMSException
    {
        connection = utils.getConnection();
        session
                = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        sender = session.createSender(utils.getQueue());
        System.out.println("Sender started.");
    }


    private void sendMessage(String text) throws MQeJMSException
    {
        MQeMessage message = session.createTextMessage(text);
        sender.send(message);
            System.out.println(
                    "Message sent: "
                    + text
                    + " of ID: "
                    + message.getJMSMessageID());
    }

    private void disconnect() throws MQeJMSException
    {
        sender.close();
        session.close();
        connection.close();
        System.out.println("Sender stopped.");
    }

}
