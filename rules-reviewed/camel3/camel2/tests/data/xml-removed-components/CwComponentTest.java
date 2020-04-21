package camel2.org.apache.camel.component.aws.cw;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CwComponentTest extends CamelTestSupport {
    private static final Date NOW = new Date();
    private static final Date LATER = new Date(NOW.getTime() + 1);
    private AmazonCloudWatchClient cloudWatchClient = mock(AmazonCloudWatchClient.class);

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry registry = super.createRegistry();
        registry.bind("amazonCwClient", cloudWatchClient);
        registry.bind("now", NOW);
        return registry;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .to("aws-cw://camel.apache.org/test?amazonCwClient=#amazonCwClient&name=testMetric&unit=Count&timestamp=#now");
            }
        };
    }
}
