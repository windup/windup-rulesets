// Contains code that should match for the following tests:
//
// weblogic-webservices-02000-test
//
import weblogic.jws.WLHttpTransport;
import weblogic.jws.ServiceClient;
import weblogic.jws.AsyncResponse;
import weblogic.jws.AsyncFailure;

import weblogic.wsee.async.AsyncPreCallContext;
import weblogic.wsee.async.AsyncCallContextFactory;
import weblogic.wsee.async.AsyncPostCallContext;

import javax.jws.WebService;
import javax.jws.WebMethod;

import java.rmi.RemoteException;

@WebService(name="AsyncPreCallContextTestClientPortType",
            serviceName="AsyncPreCallContextTestClientService",
            targetNamespace="http://windup.testcases.org/")
@WLHttpTransport(contextPath="asyncClient",
                    serviceUri="AsyncPreCallContextTestClient",
                    portName="AsyncPreCallContextTestClientServicePort")
public class AsyncPreCallContextTest
{

    @ServiceClient(wsdlLocation="http://localhost:7001/async/AsyncPreCallContextTest?WSDL",
                    serviceName="AsyncPreCallContextTestService", portName="AsyncPreCallContextTest")
    private AsyncPreCallContextTestPortType port;

    @WebMethod
    public void asyncOperation (String symbol, String userName) throws RemoteException
    {
        AsyncPreCallContext apc = AsyncCallContextFactory.getAsyncPreCallContext();
    }

    @AsyncResponse(target="port", operation="getQuote")
    public void onGetQuoteAsyncResponse(AsyncPostCallContext apc, int quote)
    {
    }

    @AsyncFailure(target="port", operation="getQuote")
    public void onGetQuoteAsyncFailure(AsyncPostCallContext apc, Throwable e)
    {
    }
}
