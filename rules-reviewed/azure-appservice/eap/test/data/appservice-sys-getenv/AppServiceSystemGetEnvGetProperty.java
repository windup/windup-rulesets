// Contains code that should match for the following tests:
//
// eap-to-azure-appservice-environment-variables-001

public class AppServiceSystemGetEnvGetProperty {

    public static void main(String[] args) {
        int PORT = -1;

        // Attempt to read a port number from the environment
        try {
            PORT = Integer.parseInt(System.getenv("PORT"));
        } catch (Exception ex) {
        }

        // Attempt to read the port number from a System property
        try {
            PORT = Integer.parseInt(System.getProperty("PORT"));
        } catch (Exception ex) {
        }

        if (PORT < 0) {
            PORT = 8080;
        }

        System.out.println(String.format("Found port: %d", PORT));
    }

}
