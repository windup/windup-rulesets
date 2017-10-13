import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/yaml")
public class YamlResource {

  @GET
  @Produces("text/x-yaml")
  public MyObject getMyObject() {
    return createMyObject();
  }
  
  @GET
  @Produces("text/yaml")
  public MyObject getMyObject1() {
    return createMyObject();
  }
  
  @GET
  @Produces("application/x-yaml")
  public MyObject getMyObject2() {
    return createMyObject();
  }


  @GET
  @Produces("application/x-java-serialized-object")
  public MyObject getMyObject4() {
    return createMyObject();
  }

  @GET
  @Produces("text/plain")
  public String getResult() {
      return "";
  }

  @GET
  @Produces("application/xml")
  public String getResult() {
      return "";
  }
}