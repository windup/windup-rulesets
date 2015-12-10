package org.jboss.seam.example.booking;

import static org.jboss.seam.ScopeType.SESSION;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jbpm.JbpmContext;

@Stateless
@Name("authenticator")
public class AuthenticatorAction 
    implements Authenticator
{
    @PersistenceContext 
    private EntityManager em;

    @In(required=false)   
    @Out(required=false, scope = SESSION)
    private User user;
   
    public boolean authenticate()
    {
	List results = em.createQuery("select u from User u where u.username=#{identity.username} and u.password=#{identity.password}")
                         .getResultList();
      
	if (results.isEmpty()) {
	    return false;
	} else {
	    user = (User) results.get(0);
	    return true;
	}
    }
    
}
