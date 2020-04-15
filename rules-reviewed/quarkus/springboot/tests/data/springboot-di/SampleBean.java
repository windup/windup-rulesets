package com.example.demodiintegrationmetrics;

import org.springframework.stereotype.Component;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * SampleBean
 */
@Component
public class SampleBean {
    private final Counter counter;

    public SampleBean(MeterRegistry registry) {
        this.counter = registry.counter("received.messages");
    }

    @Timed("handleMessage")
    public void handleMessage(String message) {
        this.counter.increment();
        // handle message implementation
    }
    
}