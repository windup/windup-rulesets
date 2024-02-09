import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.net.URI;
import java.net.URL;
import java.util.zip.ZipFile;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.ws.rs.Path;

@Path
public class HelloImpl extends UnicastRemoteObject implements Hello
{
    private File file = new File("/tmp/log") ;

    public HelloImpl() throws RemoteException {}

    public String sayHello() { return "Hello world!"; }
    
    public void createLocalLog() throws URISyntaxException
    {
        //URI log =
        new URI("file", "localhost", "/tmp", null);
    }

    /*
    * block comment
     */
    public void createLocalLog1() throws IOException
    {
        new URL("file", "localhost", "somefile");
        new ZipFile("/tmp/log");
        new ZipFile("log");
    }

    /**
     * Test method comment
     *
     * @param args
     */
    public static void main(String args[])
    {
        try
        {
            HelloImpl obj = new HelloImpl();
            // test line comment
            Naming.rebind("HelloServer", obj);
        }
        catch (Exception e)
        {
            System.out.println("HelloImpl err: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 