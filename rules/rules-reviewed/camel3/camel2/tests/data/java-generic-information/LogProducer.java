package camel2.org.apache.camel.component.log;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.util.AsyncProcessorConverterHelper;

/**
 * Log producer.
 */
public class LogProducer extends DefaultFoobarProducer {

    private final AsyncProcessor logger;

    public LogProducer(Endpoint endpoint, Processor logger) {
        super(endpoint);
        this.logger = AsyncProcessorConverterHelper.convert(logger);
    }

    public boolean process(Exchange exchange, AsyncCallback callback) {
        return logger.process(exchange, callback);
    }

    public Processor getLogger() {
        return logger;
    }
}
