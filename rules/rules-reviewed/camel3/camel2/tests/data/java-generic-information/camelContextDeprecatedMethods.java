package camel2.org.apache.camel.component.jackson.converter;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.jackson.JacksonConstants;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class JacksonConversionsSimpleTest extends CamelTestSupport {

    @Override
    public boolean isUseRouteBuilder() {
        return false;
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        // enable jackson type converter by setting this property on CamelContext
        context.getProperties().put(JacksonConstants.ENABLE_TYPE_CONVERTER, "true");
        return context;
    }

}
