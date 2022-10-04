package camel2.org.apache.camel.component.kura;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.camel.model.RoutesDefinition;
import org.apache.camel.CamelContext;

public abstract class LoadRoute50Test extends RouteBuilder implements BundleActivator {

    protected CamelContext camelContext;


    // Lifecycle

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        try {
            this.bundleContext = bundleContext;
            log.debug("Initializing bundle {}.", bundleContext.getBundle().getBundleId());
            camelContext = createCamelContext();

            camelContext.addRoutes(this);
            ConfigurationAdmin configurationAdmin = requiredService(ConfigurationAdmin.class);
            Configuration camelKuraConfig = configurationAdmin.getConfiguration(camelXmlRoutesPid());
            if (camelKuraConfig != null && camelKuraConfig.getProperties() != null) {
                Object routePropertyValue = camelKuraConfig.getProperties().get(camelXmlRoutesProperty());
                if (routePropertyValue != null) {
                    InputStream routesXml = new ByteArrayInputStream(routePropertyValue.toString().getBytes());
                    RoutesDefinition loadedRoutes = camelContext.loadRoutesDefinition(routesXml);
                    camelContext.loadRestsDefinition(routesXml);
                    camelContext.addRouteDefinitions(loadedRoutes.getRoutes());
                }
            }

            beforeStart(camelContext);
            log.debug("About to start Camel Kura router: {}", getClass().getName());
            camelContext.start();
            producerTemplate = camelContext.createProducerTemplate();
            consumerTemplate = camelContext.createConsumerTemplate();
            log.debug("Bundle {} started.", bundleContext.getBundle().getBundleId());
        } catch (Throwable e) {
            String errorMessage = "Problem when starting Kura module " + getClass().getName() + ":";
            log.warn(errorMessage, e);

            // Print error to the Kura console.
            System.err.println(errorMessage);
            e.printStackTrace();

            throw e;
        }
    }
}