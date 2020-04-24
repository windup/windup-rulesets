package com.example.demometricsactuator;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;

/**
 * SampleService
 */
@Service
public class SampleService {
    
    private final Counter counter;

    @Timed("timer")
    @Counted("counter")
    public void execute() {
        counter.increment();
    }

	public SampleService(MeterRegistry meterRegistry) {
        counter = meterRegistry.counter("name", "");
    }

}