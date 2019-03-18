package com.jboss.windup.test;

import javax.security.jacc.PolicyConfigurationFactory;

public class MyJaccProviderService {
    public PolicyConfigurationFactory getPolicyConfigurationFactory() {
        PolicyConfigurationFactory pcf = null;
        try {
            pcf = PolicyConfigurationFactory.getPolicyConfigurationFactory();
        } catch (Exception e) {
            return null;
        } 
        return pcf;
    }
}