import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.CompositeRegistry;
import org.apache.camel.impl.PropertyPlaceholderDelegateRegistry;
import org.apache.camel.util.component.*;
import org.apache.camel.processor.loadbalancer.SimpleLoadBalancerSupport;

import org.apache.camel.impl.FileWatcherReloadStrategy;
import org.apache.camel.impl.CamelPostProcessorHelper;
import org.apache.camel.impl.TypedProcessorFactory;
import org.apache.camel.impl.WebSpherePackageScanClassResolver;

import org.apache.camel.support.ReloadStrategySupport;
import org.apache.camel.management.JmxSystemPropertyKeys;

import org.apache.camel.language.Bean;
import org.apache.camel.language.Simple;
import org.apache.camel.language.SpEL;

import org.apache.camel.InvokeOnHeader;
import org.apache.camel.Constant;
import org.apache.camel.InvokeOnHeaders;

import org.apache.camel.builder.xml.XPathBuilder;
import org.apache.camel.language.XPath;
import org.apache.camel.builder.xml.InvalidXPathExpression;

import org.apache.camel.processor.validation.PredicateValidationException;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {
    rest("/say")
                .get("/hello").to("direct:hello")
                .get("/bye").consumes("application/json")
                .post("/bye");

    public void configure() {
        from("direct:xslt-copy-all")
            .to("xslt:xslt/copy-all.xsl")
            .to("file:target/messages/others")
            .to("log:foo?logMask=true")
            .to("mock:result");

        from("ref:endpoint1")
            .marshal().zip()
            .to("browse:orderReceived")
            .to("bean:bye");

        from("dataset:foo")
            .to("direct-vm:bar");
            .unmarshal().gzip().process(new UnZippedMessageProcessor());

        from("scheduler://foo?delay=60s")
            .to("seda:next")
            .to("stub:smtp://somehost.foo.com?user=windup");

            from("vm:bar?concurrentConsumers=5")
            .to("validator:org/apache/camel/component/validator/schema.xsd?headerName=headerToValidate&amp;failOnNullHeader=false");
    }
}
    @Override
    public void configure(CustomRouteBuilder configuration) throws Exception{
        if (configuration.getIntercepted() != null) {
            configuration.getIntercepted().includeRoutes(configuration);
        } else {
            camelContext.addRoutes(configuration);
        }
        LOG.debug("Custom route builder configured: {}", configuration);
    }

