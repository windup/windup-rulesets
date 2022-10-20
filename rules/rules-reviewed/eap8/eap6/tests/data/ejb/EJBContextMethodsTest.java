package data.ejb;


import javax.ejb.EJBContext;
import javax.annotation.Resource;
import java.security.Identity;
import java.util.Properties;
import javax.xml.rpc.handler.MessageContext;


public class EJBContextMethodsTest  {
    
    @Resource
    private EJBContext context;

    // Inject the Session Context
    @Resource
    private SessionContext sessionCtx;

    public String securityCalls(Identity role) {

        try {
            Identity caller = context.getCallerIdentity();
            boolean inRole = context.isCallerInRole(role);
            Properties env = context.getEnvironment();
            MessageContext mCx = sessionCtx.getMessageContext();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}