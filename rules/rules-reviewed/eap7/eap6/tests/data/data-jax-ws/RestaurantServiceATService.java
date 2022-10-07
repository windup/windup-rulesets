import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

@WebServiceClient(name = "RestaurantServiceATService", targetNamespace = "http://www.jboss.org/jboss-jdf/jboss-as-quickstart/wsat/simple/Restaurant")
public class RestaurantServiceATService extends Service {
    private final static URL RESTAURANTSERVICEATSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(RestaurantServiceATService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = RestaurantServiceATService.class.getResource(".");
            url = new URL(baseUrl, "RestaurantServiceAT.wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'RestaurantServiceAT.wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        RESTAURANTSERVICEATSERVICE_WSDL_LOCATION = url;
    }

    public RestaurantServiceATService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RestaurantServiceATService() {
        super(RESTAURANTSERVICEATSERVICE_WSDL_LOCATION, new QName(
                "http://www.jboss.org/jboss-jdf/jboss-as-quickstart/wsat/simple/Restaurant", "RestaurantServiceATService"));
    }

    *
     *
     * @return returns RestaurantServiceAT

    @WebEndpoint(name = "RestaurantServiceAT")
    public RestaurantServiceAT getRestaurantServiceAT() {
        return super.getPort(new QName("http://www.jboss.org/jboss-jdf/jboss-as-quickstart/wsat/simple/Restaurant",
                "RestaurantServiceAT"), RestaurantServiceAT.class);
    }

    *
     *
     * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy. Supported features not in the
     *        <code>features</code> parameter will have their default values.
     * @return returns RestaurantServiceAT

    @WebEndpoint(name = "RestaurantServiceAT")
    public RestaurantServiceAT getRestaurantServiceAT(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.jboss.org/jboss-jdf/jboss-as-quickstart/wsat/simple/Restaurant",
                "RestaurantServiceAT"), RestaurantServiceAT.class, features);
    }

}
