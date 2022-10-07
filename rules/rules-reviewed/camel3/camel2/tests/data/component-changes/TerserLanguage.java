package winduprules;

import org.apache.camel.builder.RouteBuilder;
import static org.apache.camel.component.hl7.HL7.terser;

import org.apache.camel.component.shiro.security.ShiroSecurityTokenInjector;


public class TerserLanguage extends RouteBuilder {

    public void configure() {
        from("direct:test1")
                .setHeader("PATIENT_ID",terser("QRD-8(0)-1"))
                .to("mock:test1");

    }

}