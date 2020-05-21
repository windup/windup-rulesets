package org.mattho.camel.playground;

import org.apache.camel.builder.RouteBuilder;

public class MultipleInputsRoutesBuilder extends RouteBuilder {
    public void configure() {
        from("direct:inputA").from("direct:inputB").from("direct:inputC")
                .to("mock:end");
        from("direct:inputA","direct:inputB")
                .to("mock:end2");
    }
}