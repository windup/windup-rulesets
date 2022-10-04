package org.apache.camel.routepolicy.quartz2;

import java.util.Date;
import java.util.Map;

import org.apache.camel.ServiceStatus;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.quartz2.QuartzComponent;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.OutHeaders;
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

    public String outHeaders(String name, int amount, String customer, @OutHeaders Map<String, Object> outHeaders) {
        outHeaders.put("partName", name);
        outHeaders.put("quantity", amount);
        outHeaders.put("customer", customer);
        return "dummy";
    }

    @Test
    public void testMultiplePoliciesOnRoute() throws Exception {
        MockEndpoint success = context.getEndpoint("mock:success", MockEndpoint.class);
        success.expectedMinimumMessageCount(size - 10);
        success.message(0).outBody().contains("foo");
        success.message(0).outBody(String.class).contains("foo");
        success.message(0).outHeaders().contains("foo");
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
