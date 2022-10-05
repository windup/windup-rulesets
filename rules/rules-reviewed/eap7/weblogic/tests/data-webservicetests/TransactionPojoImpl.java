import weblogic.jws.Transactional;
import weblogic.jws.WLHttpTransport;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name="TransactionPojoPortType",
        serviceName="TransactionPojoService",
        targetNamespace="http://example.org")
@WLHttpTransport(contextPath="transactionsPojo",
        serviceUri="TransactionPojoService",
        portName="TransactionPojoPort")
public class TransactionPojoImpl {
    @WebMethod()
    @Transactional(value=true)
    public String sayHello(String message) {
        System.out.println("sayHello:" + message);
        return "Here is the message: '" + message + "'";
    }
}