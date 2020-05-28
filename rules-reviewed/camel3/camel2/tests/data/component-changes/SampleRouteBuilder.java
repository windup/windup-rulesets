package winduprules;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
//import org.apache.camel.CamelContext;


import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.interceptor.Tracer;
import org.apache.camel.component.mock.MockEndpoint;


import org.jvnet.mock_javamail.Mailbox;

/**
 * Unit test for idempotent repository.
 */
public class SampleRouteBuilder extends RouteBuilder {

    public void configure() throws Exception {
        Tracer tracer = new Tracer();
        getContext().addInterceptStrategy(tracer);
        getContext().setTracing(true);
        getContext().setDefaultTracer(tracer);

        CamelContext c = getContext();
        c.setTracing(true);

        from("imap://jones@localhost?password=secret&idempotentRepository=#myRepo&consumer.initialDelay=100&consumer.delay=100").routeId("foo").noAutoStartup()
                .to("mock:result");
    }
}
