package winduprules;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.shiro.security.ShiroSecurityPolicy;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.config.Ini;

import org.apache.camel.component.shiro.security.ShiroSecurityTokenInjector;


public class ShiroRouteBuilder extends RouteBuilder {

    String tagXPATH = "//cheesesites/italy/cheese";
    boolean secureTagContent = true;

    private byte[] passPhrase = {
            (byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B,
            (byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F,
            (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13,
            (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17};

    public void configure() {
        Ini ini = new Ini();

        ShiroSecurityPolicy securityPolicy = new ShiroSecurityPolicy("src/test/resources/securityconfig.ini");
        ShiroSecurityPolicy securityPolicy2 = new ShiroSecurityPolicy(ini);
        ShiroSecurityPolicy securityPolicy3 = new ShiroSecurityPolicy(new Ini(ini));
        ShiroSecurityPolicy securityPolicy4 = new ShiroSecurityPolicy();
        ShiroSecurityPolicy securityPolicy5 = new ShiroSecurityPolicy(ini,passPhrase);
        ShiroSecurityTokenInjector injector = new ShiroSecurityTokenInjector();

        onException(UnknownAccountException.class, IncorrectCredentialsException.class,
                LockedAccountException.class, AuthenticationException.class).
                to("mock:authenticationException");

        from("direct:secureEndpoint").
                policy(securityPolicy).
                to("log:incoming payload")
                .marshal().secureXML(tagXPATH,secureTagContent)
                .to("direct:success");
    }

    from("direct:hello")
            .transform().simple("Hello ${out.body}");

    from("direct:hello-headers")
            .transform().simple("${out.headers.foo}=bar");

    from("direct:hello-header")
            .transform().simple("${out.headers[foo]}=bar");
}

}