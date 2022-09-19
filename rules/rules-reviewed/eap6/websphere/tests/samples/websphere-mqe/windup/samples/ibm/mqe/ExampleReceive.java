package windup.samples.ibm.mq;

import java.io.IOException;

import com.ibm.mqe.jms.MQeJMSException;
import com.ibm.mqe.jms.MQeMessage;
import com.ibm.mqe.jms.MQeMessageListener;
import com.ibm.mqe.jms.MQeQueueConnection;
import com.ibm.mqe.jms.MQeQueueReceiver;
import com.ibm.mqe.jms.MQeQueueSession;
import com.ibm.mqe.jms.MQeSession;
import com.ibm.mqe.jms.MQeTextMessage;
import javax.naming.NamingException;


public class ExampleReceiv implements MQeMessageListener
{

    private PeerUtilities utils;
    private MQeQueueConnection connection;
    private MQeQueueSession session;
    private MQeQueueReceiver receiver;


    public static void main(String[] args)
            throws NamingException, MQeJMSException, IOException
    {
        ExampleReceiv receiver = new ExampleReceiv();
        receiver.connect();
        System.in.read();
        receiver.disconnect();
    }


    private ExampleReceiv()
    {
        utils = new PeerUtilities();
    }


    private void connect() throws NamingException, MQeJMSException
    {
        connection = utils.getConnection();
        session
                = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        receiver = session.createReceiver(utils.getQueue());
        receiver.setMessageListener(this);
        connection.start();
        System.out.println("Receiver started.");
    }


    public void onMessage(MQeMessage message)
    {
        try
        {
            MQeTextMessage tMessage = (MQeTextMessage) message;
            String text;
            text = tMessage.getText();
            System.out.println(
                    "Received message <"
                    + text
                    + "> with ID <"
                    + message.getJMSMessageID()
                    + ">");
        }
        catch( MQeJMSException e )
        {
            e.printStackTrace();
        }
    }


    private void disconnect() throws MQeJMSException
    {
        receiver.close();
        session.close();
        connection.stop();
        connection.close();
        System.out.println("Receiver stopped.");
    }

}
