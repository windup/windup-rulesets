// Contains code that should match for the following tests:
//
// weblogic-01000-test
//
import weblogic.time.common.Schedulable;
import weblogic.time.common.Triggerable;

class TriggerableTest implements Schedulable, Triggerable
{
    public long schedule(long time)
    {
        return time + 7000;
    }

    public void trigger()
    {
        System.out.println("trigger pulled");
    }
}
