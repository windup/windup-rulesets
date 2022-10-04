import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(
        name = "SimpleTopicMDB",
        activationConfig = {@ActivationConfigProperty(
                propertyName = "destinationType",
                propertyValue = "javax.jms.Topic"
        ), @ActivationConfigProperty(
                propertyName = "destination",
                propertyValue = "topic/SimpleTopic"
        ), @ActivationConfigProperty(
                propertyName = "acknowledgeMode",
                propertyValue = "Auto-acknowledge"
        )}
)
public class SimpleTopicMDB implements MessageListener {
    public void onMessage(Message rcvMessage) {
    }
}
