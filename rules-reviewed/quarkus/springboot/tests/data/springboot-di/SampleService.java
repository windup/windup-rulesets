package com.example.demodiintegrationmetrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.annotation.Counted;

/**
 * SampleService
 */
@Service
public class SampleService {
    @Autowired
    SampleBean sampleBean;
    
    private final Counter counter;

    @Counted("counter")
    public void execute() {
        counter.increment();
    }

	public SampleService(MeterRegistry meterRegistry) {
        counter = meterRegistry.counter("name", "");
    }

}