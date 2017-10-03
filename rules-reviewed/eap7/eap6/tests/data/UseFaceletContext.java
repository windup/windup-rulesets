package jsf;

import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;

public class UseFaceletContext {
    public static final String property = "org.jboss.security.ignoreHttpsHost";
    
    public static void main(String[] args) {
        System.out.println(FacesContext.getCurrentInstance().getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY));
        
        System.setProperty(property, "true");
    }
}
