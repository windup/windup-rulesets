package sample;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;

@Dependent
public class HelloService {
    @Produces
    String createHelloMessage(String name) {
        return "Hello " + name + "!";
    }

}
