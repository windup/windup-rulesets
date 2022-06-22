import java.io.FileInputStream;
import java.security.KeyStore;

public class Certificates {

    public static void main(String[] args) {
        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        keyStore.load(
                new FileInputStream("/opt/java/lib/security/client.jks"),
                "somePassword".toCharArray());

        System.out.println(keyStore.getType());
    }
}
