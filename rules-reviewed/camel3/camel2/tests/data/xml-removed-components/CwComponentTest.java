package camel2.org.apache.camel.component.aws2.cw;

import java.time.Instant;

import org.apache.camel.BindToRegistry;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit;

public class CwComponentTest extends CamelTestSupport {

    @BindToRegistry("now")
    private static final Instant NOW = Instant.now();

    private static final Instant LATER = Instant.ofEpochMilli(NOW.getNano() + 1);

    @BindToRegistry("amazonCwClient")
    private CloudWatchClient cloudWatchClient = new CloudWatchClientMock();

    @EndpointInject("mock:result")
    private MockEndpoint mock;

    @Test
    public void sendMetricFromHeaderValues() throws Exception {
        mock.expectedMessageCount(1);
        Exchange exchange = template.request("direct:start", new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(Cw2Constants.METRIC_NAMESPACE, "camel.apache.org/overriden");
                exchange.getIn().setHeader(Cw2Constants.METRIC_NAME, "OverridenMetric");
                exchange.getIn().setHeader(Cw2Constants.METRIC_VALUE, Double.valueOf(3));
                exchange.getIn().setHeader(Cw2Constants.METRIC_UNIT, StandardUnit.BYTES.toString());
                exchange.getIn().setHeader(Cw2Constants.METRIC_TIMESTAMP, LATER);
            }
        });

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").to("aws2-cw://camel.apache.org/test?amazonCwClient=#amazonCwClient&name=testMetric&unit=BYTES&timestamp=#now").to("mock:result");
            }
        };
    }
}
