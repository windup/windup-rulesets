import javax.ejb.Timeout;
import javax.ejb.Stateless;

@Stateless
public class EJBTimerTimeout {
    
    @Timeout
    public void onEvent() {
    }
}
