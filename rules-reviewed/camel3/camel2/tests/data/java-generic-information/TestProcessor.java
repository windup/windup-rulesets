package org.mattho.camel.playground;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class TestProcessor implements Processor {

    public void process(Exchange exchange) throws Exception {
        if(exchange.hasOut()) {
            Message m = exchange.getOut();
            m.setHeader("header","value");
            if (m.isFault()) {
                m.setHeader("header", "value");
                m.setFault(false);
            }
        }
    }
}


