package sample;

import jakarta.enterprise.inject.Produces;


public class HelloService {
    @Produces
    String createHelloMessage(String name) {
        return "Hello " + name + "!";
    }

}
