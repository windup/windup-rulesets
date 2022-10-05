import org.apache.camel.builder.RouteBuilder;

public class TwitterStreaming {

    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("twitter-streaming://filter?type=event&twitterStream=#twitterStream&keywords=#cameltest")
                        .transform(body().convertToString()).to("mock:result");
            }
        };
    }

}
