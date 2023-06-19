import org.apache.commons.io.input.TeeInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.output.TeeOutputStream;

public class CommonsIO {

    public static void main(String[] args) throws IOException {
        String str = "Hello World.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(str.getBytes());
        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();

        FilterOutputStream teeOutputStream
            = new TeeOutputStream(outputStream1, outputStream2);
        new TeeInputStream(inputStream, teeOutputStream, true).read(new byte[str.length()]);
    }
}
