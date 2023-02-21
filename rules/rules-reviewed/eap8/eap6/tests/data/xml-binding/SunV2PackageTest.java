package com.jboss.test;

import com.sun.xml.bind.v2.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.util.Map;
import java.util.HashMap;
import java.lang.ClassLoader;
import java.net.URLClassLoader;
import java.net.URL;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;

public class SunV2PackageTest
{
    public void  setProperties() {
        Map properties = new HashMap();
        properties.put("property1", "javax.xml.bind.JAXBContext");
        URL url = null;
        JAXBContext jc = null;
        try {
            url = new URL("http://foo.bar");
        
        URL[] urls = {url};
        ClassLoader cl = new URLClassLoader(urls);
        jc = JAXBContext.newInstance( "org/foo/bar", cl, properties);
        }catch (java.lang.Exception e){

        }
        Marshaller m = jc.createMarshaller();
        m.setAdapter(new NormalizedStringAdapter());
        JAXBContext.createValidator();
    }
}