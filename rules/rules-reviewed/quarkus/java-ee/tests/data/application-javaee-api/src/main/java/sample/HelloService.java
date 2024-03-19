package sample;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.ejb.Stateless;

@Dependent
public class HelloService {
    @Produces
    String createHelloMessage(String name) {
        return "Hello " + name + "!";
    }

}
