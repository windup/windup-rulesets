package sample;

import javax.enterprise.context.Dependent;

@Dependent
public class HelloService {

    String createHelloMessage(String name) {
        return "Hello " + name + "!";
    }

}
