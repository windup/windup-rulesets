import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.util.component.*;

import org.apache.camel.impl.FileWatcherReloadStrategy;
import org.apache.camel.impl.CamelPostProcessorHelper;
import org.apache.camel.impl.TypedProcessorFactory;
import org.apache.camel.impl.WebSpherePackageScanClassResolver;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {


    public void configure() {
        rest("/say")
                .get("/hello").to("direct:hello")
                .get("/bye").consumes("application/json")
                .post("/bye");

        from("direct:xslt-copy-all")
            .to("xslt:xslt/copy-all.xsl")
            .to("file:target/messages/others")
            .to("log:foo?logMask=true")
            .to("seda:next")
            .to("direct-vm:bar");
            .to("mock:result");

        from("ref:endpoint1")
            .marshal().zip()
            .marshal().zipDeflater()
            .to("browse:orderReceived")
            .to("direct:order")
            .to("seda:next")
            .to("language:simple:classpath:org/apache/camel/component/language/mysimplescript.txt")
            .to("timer:bye");

        from("dataset:foo")
            .to("direct-vm:bar");
            .unmarshal().gzip().process(new UnZippedMessageProcessor());

        from("scheduler://foo?delay=60s")
            .to("seda:next")
            .to("stub:smtp://somehost.foo.com?user=windup");

        from("vm:bar?concurrentConsumers=5")
            .to("validator:org/apache/camel/component/validator/schema.xsd?headerName=headerToValidate&amp;failOnNullHeader=false");

        from("activemq:MyQueue").choice()
            .when().xpath("in:header('foo') = 'bar'").to("activemq:x")
            .when().xpath("in:body() = '<two/>'").to("activemq:y")
            .otherwise().to("activemq:MyQueue");
    }
}
