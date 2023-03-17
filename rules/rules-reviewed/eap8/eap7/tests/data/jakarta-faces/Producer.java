import jakarta.faces.context.FacesContext;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.context.RequestScoped;

public class Producer {
    @Produces
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
} 
