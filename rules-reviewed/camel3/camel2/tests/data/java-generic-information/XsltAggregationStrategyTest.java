package camel2.org.apache.camel.util.toolbox;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.util.toolbox.XsltAggregationStrategy;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Unit test for the {@link XsltAggregationStrategy}.
 * <p>
 * Need to use Saxon to get a predictable result: We cannot rely on the JDK's XSLT processor as it can vary across
 * platforms and JDK versions. Also, Xalan does not handle node-set properties well.
 */
public class XsltAggregationStrategyTest extends CamelTestSupport {

    @Test
    public void testXsltAggregationDefaultProperty() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:transformed");
        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived("<?xml version=\"1.0\" encoding=\"UTF-8\"?><item>ABC</item>");

        context.startRoute("route1");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testXsltAggregationUserProperty() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:transformed");
        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived("<?xml version=\"1.0\" encoding=\"UTF-8\"?><item>ABC</item>");

        context.startRoute("route2");

        assertMockEndpointsSatisfied();
    }

    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:src/test/resources/org/apache/camel/util/toolbox?noop=true&sortBy=file:name&antInclude=*.xml")
                        .routeId("route1").noAutoStartup()
                        .aggregate(new XsltAggregationStrategy("org/apache/camel/util/toolbox/aggregate.xsl")
                                .withSaxon())
                        .constant(true)
                        .completionFromBatchConsumer()
                        .log("after aggregate body: ${body}")
                        .to("mock:transformed");

                from("file:src/test/resources/org/apache/camel/util/toolbox?noop=true&sortBy=file:name&antInclude=*.xml")
                        .routeId("route2").noAutoStartup()
                        .aggregate(new XsltAggregationStrategy("org/apache/camel/util/toolbox/aggregate-user-property.xsl")
                                .withSaxon().withPropertyName("user-property"))
                        .constant(true)
                        .completionFromBatchConsumer()
                        .log("after aggregate body: ${body}")
                        .to("mock:transformed");
            }
        };
    }
}