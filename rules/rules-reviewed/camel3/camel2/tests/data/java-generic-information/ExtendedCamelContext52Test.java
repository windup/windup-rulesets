package camel2.org.apache.camel.component.log;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.util.jndi.JndiTest;
import org.junit.Assert;
import org.junit.Test;

public class ExtendedCamelContext52Test {
    private static boolean listenerFired;

    @Test
    public void testLogMask() throws Exception {
        listenerFired = false;
        CamelContext context = createCamelContext();
        MockEndpoint mock = context.getEndpoint("mock:foo", MockEndpoint.class);
        mock.expectedMessageCount(1);
        context.addLogListener((exchange, camelLogger, message) -> {
            Assert.assertEquals("Exchange[ExchangePattern: InOnly, BodyType: String, Body: hello]", message);
            listenerFired = true;
            return message + " - modified by listener";
        });
        context.start();
        context.setProcessorFactory(new MyFactory());
        context.createProducerTemplate().sendBody("direct:foo", "hello");
        mock.assertIsSatisfied();
        Assert.assertEquals(true, listenerFired);
        context.stop();
    }

    protected CamelContext createCamelContext() throws Exception {
        JndiRegistry registry = new JndiRegistry(JndiTest.createInitialContext());
        CamelContext context = new DefaultCamelContext(registry);
        context.addRoutes(createRouteBuilder());
        return context;
    }

}
