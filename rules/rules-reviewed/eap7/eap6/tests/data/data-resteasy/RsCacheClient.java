import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.cache.CacheFactory;
import org.jboss.resteasy.client.cache.LightweightBrowserCache;

import org.jboss.resteasy.spi.ForbiddenException;
import org.jboss.resteasy.spi.MethodNotAllowedException ; 
import org.jboss.resteasy.spi.NotAcceptableException ;
import org.jboss.resteasy.spi.NotFoundException; 
import org.jboss.resteasy.spi.UnauthorizedException;
import org.jboss.resteasy.spi.UnsupportedMediaTypeException;
import org.jboss.resteasy.plugins.providers.jaxb.JAXBContextWrapper;
import org.jboss.resteasy.core.ResteasyHttpServletResponseWrapper;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInputImpl;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import Acme.Serve.Serve.AcmeSession;
import org.jboss.resteasy.core.ResteasyHttpServletRequestWrapper;
import org.jboss.resteasy.core.ResteasyHttpServletResponseWrapper;
import org.jboss.resteasy.auth.oauth.OAuthValidator;
import org.jboss.resteasy.plugins.delegates.ServerCookie;
import org.jboss.resteasy.spi.Registry;

public class RsCacheClient {


    @org.jboss.resteasy.annotations.cache.ServerCached
    public void someMethod() {
        
    }
    
    public static void main(String[] args) throws Exception
    {
          RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
          OrderServiceClient proxy = ProxyFactory.create(OrderServiceClient.class, generateBaseUrl());
    
          // This line enables caching
          LightweightBrowserCache cache = CacheFactory.makeCacheable(proxy);
          
          JAXBContextWrapper.createValidator(validator);
          ResteasyHttpServletResponseWrapper httpServletRespWrapper = new ResteasyHttpServletResponseWrapper();
          httpServletRespWrapper.encodeRedirectUrl("/test");
          httpServletRespWrapper.encodeUrl("/test");
          MultipartFormDataInputImpl.getFormData();
          MultipartFormDataInput.getFormData();
          AcmeSession.getSessionContext();
          ResteasyHttpServletRequestWrapper wrapper = new ResteasyHttpServletRequestWrapper();
          wrapper.isRequestedSessionIdFromUrl("/test");
          ResteasyHttpServletResponseWrapper wrapper1 = ResteasyHttpServletResponseWrapper();
          wrapper1.setStatus(false);
          OAuthValidator validator = new OAuthValidator();
          validator.validateMessage(oauthmessage, oauthaccessor);
          ServerCookie cookie = new ServerCookie();
          cookie.checkName("test");
          cookie.maybeQuote(numerro, stringbuffer, str);
        
}