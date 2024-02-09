package sample;

import jakarta.ejb.Stateless;

@Stateless
public class HelloEJB {
    
    String createHelloMessage(String name) {
        return "Hello " + name + "!";
    }

}