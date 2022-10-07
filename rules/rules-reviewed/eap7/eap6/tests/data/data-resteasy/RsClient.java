import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.LinkHeader;
import org.jboss.resteasy.client.EntityTypeFactory;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.spi.Link;


public class RsCacheClient {


    public static void main(String[] args) throws Exception
    {
         
          ClientRequest request = new ClientRequest(generateURL("/linkheader/str"));
          request.addLink("previous chapter", "previous", "http://example.com/TheBook/chapter2", null);
          ClientResponse response = request.post();
          LinkHeader header = response.getLinkHeader();
    }
    
}