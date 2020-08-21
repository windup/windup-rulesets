package org.springframework.jmx;

import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.annotation.ManagedAttribute;

@ManagedResource(objectName="bean:name=jmxTestBean", description="Test Bean")
public class JMXAnnotationTestBean implements IJmxTestBean {

    private String item;

    @ManagedAttribute(description="Item", currencyTimeLimit=15)
    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

}