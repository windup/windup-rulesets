import org.apache.camel.builder.RouteBuilder;

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
            .to("controlbus:route?routeId=mainRoute&action=stop&async=true")
            .to("language:simple:classpath:org/apache/camel/component/language/mysimplescript.txt")
            .to("bean:bye");

        from("dataset:foo")
            .to("direct-vm:bar");
            .unmarshal().gzip().process(new UnZippedMessageProcessor());

        from("scheduler://foo?delay=60s")
            .to("seda:next")
            .to("stub:smtp://somehost.foo.com?user=windup");

            from("vm:bar?concurrentConsumers=5")

        from("scheduler://foo?delay=60s")
            .to("seda:next")
            .to("stub:smtp://somehost.foo.com?user=windup");

        from("vm:bar?concurrentConsumers=5")
            .to("validator:org/apache/camel/component/validator/schema.xsd?headerName=headerToValidate&amp;failOnNullHeader=false");

        from("ctivemq:MyQueue").choice()
            .when().xpath("in:header('foo') = 'bar'").to("activemq:x")
            .when().xpath("in:body() = '<two/>'").to("activemq:y")
            .otherwise().to("activemq:MyQueue");
    }
}
