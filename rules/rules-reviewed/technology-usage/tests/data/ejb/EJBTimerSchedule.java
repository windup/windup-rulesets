import javax.ejb.Schedule;
import javax.ejb.Stateless;

@Stateless
public class EJBTimerSchedule {

    @Schedule(dayOfMonth = "Last", hour = "23")
    public void onEvent() {
    }
}
