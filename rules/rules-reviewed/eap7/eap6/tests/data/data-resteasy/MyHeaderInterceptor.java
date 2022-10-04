import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.spi.interception.MessageBodyWriterInterceptor;
import org.jboss.resteasy.spi.interception.MessageBodyReaderInterceptor;
import org.jboss.resteasy.spi.interception.MessageBodyWriterContext;
import org.jboss.resteasy.spi.interception.MessageBodyReaderContext;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.interception.InterceptorRegistry;
import org.jboss.resteasy.core.interception.InterceptorRegistryListener;

import org.jboss.resteasy.core.interception.ClientExecutionContextImpl;
import org.jboss.resteasy.spi.interception.ClientExecutionContext;
import org.jboss.resteasy.spi.interception.ClientExecutionInterceptor;
import org.jboss.resteasy.core.interception.InterceptorRegistryListener;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;

@Provider
@ServerInterceptor
public class MyHeaderDecorator implements MessageBodyWriterInterceptor, MessageBodyReaderInterceptor, MessagBodyWriter {

    public void write(MessageBodyWriterContext context) throws IOException, WebApplicationException
    {
       context.getHeaders().add("My-Header", "custom");
       context.proceed();
    }
    
    public void read(MessageBodyReaderContext context) throws IOException, WebApplicationException
    {
       //todo
    }
}
