package camel2.org.apache.camel.component.mail;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class MapMailMessagesBugRoute extends RouteBuilder {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.addRouteBuilder(new MapMailMessagesBugRoute());
        main.run(args);
    }

    public void configure() {
        // This is for Office365 host. Set your own host/username/password.
        // When setting option mapMailMessage=true (the default) option peek=true fails with the SEEN flag.
        // When setting option mapMailMessage=false option peek=true will work as supposed.
        from("imaps://outlook.office365.com?peek=true&unseen=true&debugMode=true&username=<username>&password=<password>")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        if (true) {
                            throw new IOException("This will cause messages to be marked SEEN even when peek=true.");
                        }
                    }
                })
                .to("log:mail");
    }

}

