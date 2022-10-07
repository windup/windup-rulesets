package camelinaction;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.management.event.CamelContextStartedEvent;
import org.apache.camel.management.event.ExchangeCompletedEvent;

@Singleton
public class HelloConfiguration {

    @Produces
    @Named("properties")
    PropertiesComponent propertiesComponent() {
        PropertiesComponent component = new PropertiesComponent();
        // load properties file form the classpath
        component.setLocation("classpath:hello.properties");
        return component;
    }

    void onContextStarted(@Observes CamelContextStartedEvent event) {
        System.out.println("***************************************");
        System.out.println("* Camel started " + event.getContext().getName());
        System.out.println("***************************************");
    }
}