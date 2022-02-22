import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("scheduledAnnotationExample")
public class ScheduledAnnotationExample {


    @Scheduled(fixedRate = 1000)
    public void scheduleFixedRateTask() {
        System.out.println("Fixed rate task - " + System.currentTimeMillis() / 1000);
    }


}
