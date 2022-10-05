// Contains code that should match for the following tests:
//
// java-ee-jaxrpc-00000-test
//

import java.util.Map;
import javax.xml.rpc.handler.Handler;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;

public class JaxRpcGenericHandlerTest extends GenericHandler
{
    protected HandlerInfo info = null;

    public void init(HandlerInfo arg)
    {
        info = arg;
    }

    public void destroy()
    {
    }

    public boolean handleRequest(MessageContext context)
    {
        return true;
    }

    public boolean handleResponse(MessageContext context)
    {
        return true;
    }
}