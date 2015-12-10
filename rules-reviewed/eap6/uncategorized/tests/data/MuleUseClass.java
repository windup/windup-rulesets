
import org.mule.api.MuleMessage;
import org.mule.api.routing.filter.Filter;

public class MuleUseClass implements Filter
{

    @Override
    public boolean accept(MuleMessage message) {
        // do something
    }

}
