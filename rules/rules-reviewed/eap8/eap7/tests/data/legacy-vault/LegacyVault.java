import org.x.Annotation;

public class LegacyVault {

    @Annotation(value="${VAULT::someInfo}")
    public String aMethod() {
        return "Hello World!";
    }
}