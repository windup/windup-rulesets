package camel2.org.apache.camel.component.mail;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.jvnet.mock_javamail.Mailbox;

/**
 * Unit test for idempotent repository.
 */
public class MailIdempotentRepositoryDuplicateTest extends CamelTestSupport {

    MemoryIdempotentRepository myRepo = new MemoryIdempotentRepository();

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("myRepo", myRepo);
        return jndi;
    }

    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() throws Exception {
                from("imap://jones@localhost?password=secret&idempotentRepository=#myRepo&consumer.initialDelay=100&consumer.delay=100").routeId("foo").noAutoStartup()
                        .to("mock:result");
            }
        };
    }
}
