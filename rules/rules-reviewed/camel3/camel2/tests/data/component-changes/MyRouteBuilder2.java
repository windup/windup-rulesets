import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConfiguration;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder2 extends RouteBuilder {

    public void configure() {

        from("direct:circularTopicDetection")
                .to("kafka:mytopic?brokers=localhost&circularTopicDetection=false")
                .to("netty4-http:localhost:9898/foo?matchOnUriPrefix=true&circularTopicDetection=false")
                .to("mock:result");

        // Set options directly on the underlying configuration
        KafkaConfiguration kafkaConfiguration = new KafkaConfiguration();
        kafkaConfiguration.setCircularTopicDetection(false);
    }
}
