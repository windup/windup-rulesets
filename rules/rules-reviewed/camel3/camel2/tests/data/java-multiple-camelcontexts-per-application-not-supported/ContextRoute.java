package org.apache.camel.cdi.bean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.CdiEventEndpoint;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.cdi.ContextNames;

@ApplicationScoped
@ContextNames("first","second")
public class FirstCamelContextEventConsumingRoute extends RouteBuilder {

    @Inject
    @ContextName("first")
    private CdiEventEndpoint<String> stringCdiEventEndpoint;

    @Override
    public void configure() {
        from(stringCdiEventEndpoint).to("mock:consumeString");
    }
}