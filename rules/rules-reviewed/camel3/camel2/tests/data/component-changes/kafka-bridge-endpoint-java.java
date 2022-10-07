import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConfiguration;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    public void configure() {

        from("direct:bridgeEndpoint")
                .to("kafka:mytopic?brokers=localhost&bridgeEndpoint=false")
                .to("netty4-http:localhost:9898/foo?matchOnUriPrefix=true&bridgeEndpoint=false")
                .to("mock:result");

        // Set options directly on the underlying configuration
        KafkaConfiguration kafkaConfiguration = new KafkaConfiguration();
        kafkaConfiguration.setBridgeEndpoint(false);
    }
}