package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class FileCopierWithCamel {

    public static void main(String args[]) throws Exception {
        // create CamelContext
        CamelContext context = new DefaultCamelContext();

        if(!context.isHandleFault()) {
            context.setHandleFault(true);
        }
        // add our route to the CamelContext
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                from("file:data/inbox?noop=true").to("file:data/outbox");
            }
        });

        context.start();
        context.suspendRoute("id");

        Thread.sleep(10000);
        context.resumeRoute("id");

        // stop the CamelContext
        context.stop();
    }

}