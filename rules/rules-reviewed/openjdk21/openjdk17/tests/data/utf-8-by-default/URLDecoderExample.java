import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
 
public class URLDecoderExample {
    public static void main(String args[])throws UnsupportedEncodingException
    {
        System.out.println(URLDecoder.decode("is%3Apr+is%3Aclosed", "UTF-8"));
    }
}