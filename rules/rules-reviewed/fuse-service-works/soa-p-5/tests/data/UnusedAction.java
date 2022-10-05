// Contains code that should match for the following tests:
//
// soa-p-5-01000-test
//
import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.helpers.ConfigTree;

public class UnusedAction extends AbstractActionLifecycle
{
    protected ConfigTree  _config;

    public UnusedAction(ConfigTree config)
    {
        _config = config;
    }

    public Message displayMessage(Message message) {
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println("Body: " + message.getBody().get()) ;
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        return message;
    }
}
