package com.jboss.windup.test;

import javax.management.j2ee.Management;
import javax.management.j2ee.ManagementHome;
import javax.naming.Context;
import javax.naming.InitialContext;
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
    public static Management getMEJBRemote() {
            Context context = new InitialContext(null);
            ManagementHome home = (ManagementHome) context.lookup("ejb.mgmt.MEJB");
            Management bean = home.create();
            return bean;
    }
}