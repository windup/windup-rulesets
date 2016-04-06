package jsf;

import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;

public class UseFaceletContext {
    public static void main(String[] args) {
        System.out.println(FacesContext.getCurrentInstance().getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY));
    }
}
