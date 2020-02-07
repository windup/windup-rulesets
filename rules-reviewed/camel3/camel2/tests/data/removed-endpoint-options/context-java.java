import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConfiguration;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    public void configure() {
        from("direct:circularTopicDetection")
                .to("kafka:mytopic?brokers=localhost\"&amp;circularTopicDetection=false")
                .to("netty4-http:localhost:9898/foo?matchOnUriPrefix=true&amp;circularTopicDetection=false")
                .to("mock:result");

        from("direct:bridgeEndpoint")
                .to("kafka:mytopic?brokers=localhost\"&amp;bridgeEndpoint=false")
                .to("netty4-http:localhost:9898/foo?matchOnUriPrefix=true&amp;bridgeEndpoint=false")
                .to("mock:result");

        from("direct:bridgeEndpoint")
                .to("kafka:mytopic?brokers=localhost\"&amp;foo=false")
                .to("netty4-http:localhost:9898/foo?matchOnUriPrefix=true&amp;bridgeEndpoint=false")
                .to("mock:result");

        // Set options directly on the underlying configuration
        KafkaConfiguration kafkaConfiguration = new KafkaConfiguration();
        kafkaConfiguration.setBridgeEndpoint(false);

        KafkaConfiguration kafkaConfiguration = new KafkaConfiguration();
        kafkaConfiguration.setCircularTopicDetection(false);
    }
}