// Contains code that should match for the following tests:
//
// weblogic-webservices-04000-test
// weblogic-webservices-05000-test

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.ExcludeDefaultInterceptors;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;

import weblogic.wsee.context.ContextNotFoundException;
import weblogic.wsee.context.WebServiceContext;
import weblogic.wsee.context.WebServiceSession;

@WebService(serviceName ="WebLogicServiceContextTest",targetNamespace ="http://example.com/WebLogicServiceContextTest")
@SOAPBinding(style=SOAPBinding.Style.DOCUMENT,use=SOAPBinding.Use.LITERAL,parameterStyle=SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
@ExcludeDefaultInterceptors
public class WebLogicServiceContextTest
{
    @webMethod()
    public void doFunThings(String token)
    {
        WebServiceSession ses = null;
        try {
            // Get the current session context.
            ses = WebServiceContext.currentContext().getSession();


            // Store the Session ID in log4j MDC map
            HttpSession HttpSes = (HttpSession) ses.getUnderlyingSession();
        }
        catch (ContextNotFoundException e)
        {
            throw new Exception(e);
        }
    }
}
