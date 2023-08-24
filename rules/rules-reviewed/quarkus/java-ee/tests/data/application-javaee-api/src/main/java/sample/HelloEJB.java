package sample;

import javax.ejb.Stateless;

@Stateless
public class HelloEJB {
    
    String createHelloMessage(String name) {
        return "Hello " + name + "!";
    }

}