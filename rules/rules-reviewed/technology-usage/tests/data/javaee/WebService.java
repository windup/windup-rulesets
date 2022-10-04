package com.jboss.windup.test;

import javax.annotation.PostConstruct;
import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.Connection;
import javax.xml.registry.ConnectionFactory;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryService;
import javax.xml.registry.infomodel.Key;
import javax.xml.registry.infomodel.Organization;
import javax.validation.constraints.NotNull;


@WebService()
public class Hello {
    @NotNull
    private String myAttribute;
    
    private String message = new String("Hello, ");

    public void Hello() {}

    @WebMethod()
    public String sayHello(String name) {
        return message + name + ".";
    }
    
    @PostConstruct
    public void postConstruct() {
        JAXBContext jc = JAXBContext.newInstance( "com.acme.foo" );
        ConnectionFactory connectionFactory = ConnectionFactory.newInstance();
        System.out.println("nothing");
    }


    
} 