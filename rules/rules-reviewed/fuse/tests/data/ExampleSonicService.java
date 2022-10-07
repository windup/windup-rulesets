package com.acme.sample;

import com.sonicsw.xq.XQServiceContext;
import com.sonicsw.xq.XQInitContext;
import com.sonicsw.xq.XQParameters;
import com.sonicsw.xq.XQParameterInfo;
import com.sonicsw.xq.XQMessage;
import com.sonicsw.xq.XQPart;
import com.sonicsw.xq.XQLog;
import com.sonicsw.xq.XQServiceException;
import com.sonicsw.xq.XQEnvelope;

import com.sonicsw.xq.XQService;
import com.sonicsw.xq.XQAddressFactory;

/**
 * This is a snippet of java code for testing Sonic ESB windup rules.
 * This is not real code!
 * 
 * @author mnovotny
 *
 */
public class ExampleSonicService implements XQService {
    
    private XQInitContext initContext ;
    
    public ExampleSonicService(XQInitContext context){
        this.initContext = context;
    }
    public void windupTests(){
        
        new XQServiceContext();
        new XQInitContext();
        new XQParameters();
        new XQParameterInfo();
        new XQMessage();
        new XQPart();
        new XQLog();
        new XQServiceException();
        new XQEnvelope();
        
    }
    public void testWindupRules(){
        XQAddressFactory.createEndpointAddress("http:/test.com/myendpoint");
        
        XQParameters params = initContext.getParameters(); 
        Object intruder = params.getParameter("Intruder", XQConstants.PARAM_OBJECT);
        
        XQEnvelope xqenvelope = initContext.createDefaultEnvelope();
        XQMessage message = xqenvelope.getMessage();
        
        String encoding = message.getHeaderValue("Encoding");
        message.setHeaderValue("Encoding", "UTF-8");
        
        List<String> headerNames = message.getHeaderNames();
        int partcount = message.getPartCount();
        XQPart part = message.getPart(0);
        String correlationId = message.getCorrelationId();
        
        if (initContext instanceof  XQServiceContext) {
            XQServiceContext serviceContext = (XQServiceContext) initContext;
            serviceContext.addOutgoing(xqEnvelope);
        }
        
    }
}
