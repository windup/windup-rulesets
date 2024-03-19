import javax.ejb.Stateless;
import javax.ejb.Stateful;

@Stateless
@Stateful
public class HelloEJB {
    
    String createHelloMessage(String name) {
        return "Hello " + name + "!";
    }

}