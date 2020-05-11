package winduprules;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Main;

import java.util.Date;

public class MainExample {

    private Main main;
    private org.apache.camel.main.Main main2;
    private org.apache.camel.spring.Main main3;

    public static void main(String[] args) throws Exception {
        MainExample example = new MainExample();
        example.boot();
    }

    public void boot() throws Exception {
        main = new Main();
        main2 = new org.apache.camel.main.Main();
        main3 = new org.apache.camel.spring.Main();
        main.getCamelContexts();

        main2.getCamelContexts();
        main.addRouteBuilder(new MyRouteBuilder());
        main3.getCamelContexts();

        System.out.println("Starting Camel. Use ctrl + c to terminate the JVM.\n");
        main.run();
    }
}