package com.jboss.windup.test;

import java.util.concurrent.Executor;

public class Main implements Executor {
    public void execute(Runnable r) {
        r.run();
    }
}
