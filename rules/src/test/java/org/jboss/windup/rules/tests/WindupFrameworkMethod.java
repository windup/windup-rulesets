package org.jboss.windup.rules.tests;

import org.junit.runners.model.FrameworkMethod;

import java.lang.reflect.Method;

public class WindupFrameworkMethod extends FrameworkMethod {
    
    private final String ruleToTest;

    public WindupFrameworkMethod(Method method, String ruleToTest) {
        super(method);
        this.ruleToTest = ruleToTest;
    }

    @Override
    public String getName() {
        return String.format("%s with %s", super.getName(), ruleToTest);
    }
}
