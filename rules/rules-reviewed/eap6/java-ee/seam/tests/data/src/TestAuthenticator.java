package org.jboss.seam.example.restbay.resteasy;


import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

@Name("testAuthenticator")
@Scope(ScopeType.EVENT)
public class TestAuthenticator
{

   @In
   private Identity identity;

   @In
   private Credentials credentials;

   @Logger
   private Log log;

   @Transactional
   public boolean authenticate()
   {
      // Tests that the SFSB can be obtained in both ContextualHttpRequests (authentication and web service invocation)
      TestEjbLocal ejb = (TestEjbLocal) Component.getInstance("securedEjb", ScopeType.EVENT);
      ejb.foo();
      
      log.debug("Authenticating username/password: " + credentials.getUsername() + "/" + credentials.getPassword());
      if (credentials.getUsername().equals(credentials.getPassword())) {
         log.info("Authenticated {0}", credentials.getUsername());
         
         if (credentials.getUsername().equals("admin")) {
            identity.addRole("admin");
            log.info("Admin rights granted for {0}", credentials.getUsername());
         }
         log.debug("Authentication valid");
         return true;
      } else {
         log.debug("Authentication invalid");
         return false;
      }
   }
}