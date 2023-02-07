import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.interceptors.encoding.*;

/**
 * JUnit4 Test class which makes a request to the RESTful helloworld-rs web service.
 *
 * @author bmincey (Blaine Mincey)
 */
public class JaxRsClient {
    /**
     * Request URLs pulled from system properties in pom.xml
     */
    private static String XML_URL;
    private static String JSON_URL;

    /**
     * Property names used to pull values from system properties in pom.xml
     */
    private static final String XML_PROPERTY = "xmlUrl";
    private static final String JSON_PROPERTY = "jsonUrl";

    /**
     * Responses of the RESTful web service
     */
    private static final String XML_RESPONSE = "<xml><result>Hello World!</result></xml>";
    private static final String JSON_RESPONSE = "{\"result\":\"Hello World!\"}";

    /**
     * Method executes BEFORE the test method. Values are read from system properties that can be modified in the pom.xml.
     */

    public static void main(String[] args) {
        JaxRsClient.XML_URL = System.getProperty(JaxRsClient.XML_PROPERTY);
        JaxRsClient.JSON_URL = System.getProperty(JaxRsClient.JSON_PROPERTY);
        new JaxRsClient().test();
    }

    /**
     * Test method which executes the runRequest method that calls the RESTful helloworld-rs web service.
     */
    void test() {
        String response = runRequest(JaxRsClient.XML_URL, MediaType.APPLICATION_XML_TYPE);

        if (!JaxRsClient.XML_RESPONSE.equals(response)) {
            throw new RuntimeException("Response is wrong:\nXML Response:" + response + "\nshould be: " + XML_RESPONSE);
        }
        response = runRequest(JaxRsClient.JSON_URL, MediaType.APPLICATION_JSON_TYPE);

        if (!JaxRsClient.JSON_RESPONSE.equals(response)) {
            throw new RuntimeException("Response is wrong:\nJSON Response:" + response + "\nshould be: " + JSON_RESPONSE);
        }

    }

    /**
     * The purpose of this method is to run the external REST request.
     *
     * @param url       The url of the RESTful service
     * @param mediaType The mediatype of the RESTful service
     */
    private String runRequest(String url, MediaType mediaType) {
        String result = null;

        System.out.println("===============================================");
        System.out.println("URL: " + url);
        System.out.println("MediaType: " + mediaType.toString());


        // Using the RESTEasy libraries, initiate a client request
        ResteasyClient client = new ResteasyClientBuilder().build();
        final ResteasyClient clientGZIP = new ResteasyClientBuilder().register(new org.jboss.resteasy.plugins.interceptors.encoding.GZIPDecodingInterceptor(256)).build();
        

        // Set url as target
        ResteasyWebTarget target = client.target(url);

        // Be sure to set the mediatype of the request
        target.request(mediaType);

        // Request has been made, now let's get the response
        Response response = target.request().get();
        result = response.readEntity(String.class);
        response.close();

        // Check the HTTP status of the request
        // HTTP 200 indicates the request is OK
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed request with HTTP status: " + response.getStatus());
        }

        // We have a good response, let's now read it
        System.out.println("\n*** Response from Server ***\n");
        System.out.println(result);
        System.out.println("\n===============================================");

        return result;
    }

}
