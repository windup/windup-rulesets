package org.windup.examples.ejb.messagedriven;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;

@MessageDriven(
        name="MyName",
        activationConfig = {
            @ActivationConfigProperty(propertyName = "destination", propertyValue="jms/OtherTopic"),
            @ActivationConfigProperty(propertyName = "destinationType", propertyValue="javax.jms.Topic")
        }
)
public class MessageDrivenBean3 {
	// stub... this is just here to test annotation scanning rulesActivationConfigProperty
}
