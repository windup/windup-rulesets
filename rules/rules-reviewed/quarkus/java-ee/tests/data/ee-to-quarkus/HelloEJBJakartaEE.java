import jakarta.ejb.Stateless;
import jakarta.ejb.Stateful;

@Stateless
@Stateful
public class HelloEJB {
    
    String createHelloMessage(String name) {
        return "Hello " + name + "!";
    }

}