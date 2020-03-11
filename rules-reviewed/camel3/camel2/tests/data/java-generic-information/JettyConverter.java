
package org.apache.camel.component.jetty;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.FallbackConverter;
import org.apache.camel.spi.TypeConverterRegistry;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;

/**
 * @version
 */
@Converter
public final class JettyConverter {

    private JettyConverter() {
        //Helper class
    }

    @Converter
    public static String toString(Response response) {
        return response.toString();
    }

    @FallbackConverter
    @SuppressWarnings("unchecked")
    public static <T> T convertTo(Class<T> type, Exchange exchange, Object value, TypeConverterRegistry registry) {
        if (value != null) {
            // should not try to convert Request as its not possible
            if (Request.class.isAssignableFrom(value.getClass())) {
                return (T) Void.TYPE;
            }
        }

        return null;
    }

}
