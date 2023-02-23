package com.jboss.test;

import com.sun.xml.bind.api.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.Validator;

public class SunApiPackageTest
{
    public void  setProperties() {
        Class[] classes = {com.acme.Foo.class};
        Map properties = new HashMap();
        properties.put("property1", "javax.xml.bind.context.factory");
        JAXBContext jc = JAXBContext.newInstance( classes, properties);
        Marshaller m = jc.createMarshaller();
        m.setProperty("com.sun.xml.bind.*", new java.lang.Object());
    }
}