package windup.samples.ibm.mq;

import java.io.IOException;

import com.ibm.mq.jms.MQJMSException;
import com.ibm.mq.jms.MQMessage;
import com.ibm.mq.jms.MQMessageListener;
import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueReceiver;
import com.ibm.mq.jms.MQQueueSession;
import com.ibm.mq.jms.MQSession;
import com.ibm.mq.jms.MQTextMessage;
import javax.naming.NamingException;


public class ExampleReceiv implements MQMessageListener
{

    private TopicCF utils;
    private MQQueueConnection connection;
    private MQQueueSession session;
    private MQQueueReceiver receiver;


    public static void main(String[] args)
            throws NamingException, MQJMSException, IOException
    {
        ExampleReceiv receiver = new ExampleReceiv();
        receiver.connect();
        System.in.read();
        receiver.disconnect();
    }


    private ExampleReceiv()
    {
        utils = new TopicCF();
    }


    private void connect() throws NamingException, MQJMSException
    {
        connection = utils.getConnection();
        session
                = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        receiver = session.createReceiver(utils.getQueue());
        receiver.setMessageListener(this);
        connection.start();
        System.out.println("Receiver started.");
    }


    public void onMessage(MQMessage message)
    {
        try
        {
            MQTextMessage tMessage = (MQTextMessage) message;
            String text;
            text = tMessage.getText();
            System.out.println(
                    "Received message <"
                    + text
                    + "> with ID <"
                    + message.getJMSMessageID()
                    + ">");
        }
        catch( MQJMSException e )
        {
            e.printStackTrace();
        }
    }


    private void disconnect() throws MQJMSException
    {
        receiver.close();
        session.close();
        connection.stop();
        connection.close();
        System.out.println("Receiver stopped.");
    }

}
