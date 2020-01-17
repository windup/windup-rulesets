import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    public void configure() {
        from("direct:xslt-copy-all")
            .to("xslt:xslt/copy-all.xsl")
            .to("file:target/messages/others")
            .to("log:foo?logMask=true")
            .to("mock:result");

        from("ref:endpoint1")
            .to("browse:orderReceived")
            .to("controlbus:route?routeId=mainRoute&action=stop&async=true")
            .to("bean:bye");

        from("dataset:foo")
            .to("direct-vm:bar");
            .from("scheduler://foo?delay=60s")
            .to("seda:next")
            .to("stub:smtp://somehost.foo.com?user=windup");

            from("vm:bar?concurrentConsumers=5")
            .to("validator:org/apache/camel/component/validator/schema.xsd?headerName=headerToValidate&amp;failOnNullHeader=false");
    }
}
