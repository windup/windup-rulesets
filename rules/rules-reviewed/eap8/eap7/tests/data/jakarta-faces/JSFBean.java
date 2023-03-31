import jakarta.inject.Inject;
import jakarta.faces.context.FacesContext;
import jakarta.enterprise.inject.Model;

@Model
public class JSFBean {

    @Inject
    FacesContext ctx;

}
