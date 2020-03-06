package camel2.org.apache.camel.component.jt400;

import java.util.Map;

import com.ibm.as400.access.AS400ConnectionPool;
import org.apache.camel.Endpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.util.EndpointHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link org.apache.camel.Component} to provide integration with AS/400 objects.
 * 
 * Current implementation supports working with data queues (*DTAQ) and Program calls (*PGM)
 */
public class Jt400Component extends FoobarEndpointComponent {
    
    /**
     * Name of the connection pool URI option.
     */
    static final String CONNECTION_POOL = "connectionPool";

    /**
     * Logging tool used by this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(Jt400Component.class);

    /**
     * Default connection pool used by the component. Note that this pool is
     * lazily initialized. This is because in a scenario where the user always
     * provides a pool, it would be wasteful for Camel to initialize and keep an
     * idle pool.
     */
    @Metadata(label = "advanced")
    private AS400ConnectionPool connectionPool;

    public Jt400Component() {
        super(Jt400Endpoint.class);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> properties) throws Exception {
        AS400ConnectionPool connectionPool;
        if (properties.containsKey(CONNECTION_POOL)) {
            LOG.trace("AS400ConnectionPool instance specified in the URI - will look it up.");
            
            // We have chosen to handle the connectionPool option ourselves, so
            // we must remove it from the given parameter list (see
            // http://camel.apache.org/writing-components.html)
            String poolId = properties.remove(CONNECTION_POOL).toString();
            connectionPool = EndpointHelper.resolveReferenceParameter(getCamelContext(), poolId, AS400ConnectionPool.class, true);
        } else {
            LOG.trace("No AS400ConnectionPool instance specified in the URI - one will be provided.");
            connectionPool = getConnectionPool();
        }

        String type = remaining.substring(remaining.lastIndexOf(".") + 1).toUpperCase();
        Jt400Endpoint endpoint = new Jt400Endpoint(uri, this, connectionPool);
        setProperties(endpoint, properties);
        endpoint.setType(Jt400Type.valueOf(type));
        return endpoint;
    }
}
