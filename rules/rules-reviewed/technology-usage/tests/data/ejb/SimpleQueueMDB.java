import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(
        name = "SimpleQueueMDB",
        activationConfig = {@ActivationConfigProperty(
                propertyName = "destinationType",
                propertyValue = "javax.jms.Queue"
        ), @ActivationConfigProperty(
                propertyName = "destination",
                propertyValue = "queue/SimpleQueue"
        ), @ActivationConfigProperty(
                propertyName = "acknowledgeMode",
                propertyValue = "Auto-acknowledge"
        )}
)
public class SimpleQueueMDB implements MessageListener {
    public void onMessage(Message message) {
    }
}
