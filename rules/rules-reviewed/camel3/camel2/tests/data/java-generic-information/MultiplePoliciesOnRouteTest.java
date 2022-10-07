package org.apache.camel.routepolicy.quartz2;

import java.util.Date;

import org.apache.camel.ServiceStatus;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.quartz2.QuartzComponent;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.ThrottlingInflightRoutePolicy;
import org.apache.camel.spi.RoutePolicy;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @version
 */
public class MultiplePoliciesOnRouteTest extends CamelTestSupport {
    private String url = "seda:foo?concurrentConsumers=20";
    private int size = 100;

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry registry = new JndiRegistry(createJndiContext());
        registry.bind("startPolicy", createRouteStartPolicy());
        registry.bind("throttlePolicy", createThrottlePolicy());
        return registry;
    }

    private RoutePolicy createThrottlePolicy() {
        ThrottlingInflightRoutePolicy policy = new ThrottlingInflightRoutePolicy();
        policy.setMaxInflightExchanges(10);
        return policy;
    }

    @Test
    public void testMultiplePoliciesOnRoute() throws Exception {
        MockEndpoint success = context.getEndpoint("mock:success", MockEndpoint.class);
        success.expectedMinimumMessageCount(size - 10);

        context.getComponent("quartz2", QuartzComponent.class).setPropertiesFile("org/apache/camel/routepolicy/quartz2/myquartz.properties");
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                from(url)
                        .routeId("test")
                        .routePolicyRef("startPolicy, throttlePolicy")
                        .to("log:foo?groupSize=10")
                        .to("mock:success");
            }
        });
        context.start();

        assertTrue(context.getRouteStatus("test") == ServiceStatus.Started);
        for (int i = 0; i < size; i++) {
            template.sendBody(url, "Message " + i);
            Thread.sleep(3);
        }

        context.getComponent("quartz2", QuartzComponent.class).stop();
        success.assertIsSatisfied();
    }

}
