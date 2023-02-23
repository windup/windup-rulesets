import org.jboss.resteasy.annotations.Suspend;
import org.jboss.resteasy.core.ResourceInvoker;
import org.jboss.resteasy.core.ResourceLocatorInvoker;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.spi.AsynchronousResponse;

@Path("/")
public class SimpleResource
{

   @GET
   @Path("basic")
   @Produces("text/plain")
   public void getBasic(final @Suspend(10000) AsynchronousResponse response) throws Exception
   {
      Thread t = new Thread()
      {
         @Override
         public void run()
         {
            try
            {
               Response jaxrs = Response.ok("basic").type(MediaType.TEXT_PLAIN).build();
               response.setResponse(jaxrs);
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
         }
      };
      t.start();
   }