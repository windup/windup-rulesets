import weblogic.application.ApplicationLifecycleListener;
import weblogic.application.ApplicationLifecycleEvent;

public class MyWeblogicAppListener extends ApplicationLifecycleListener
{

    public void preStart(ApplicationLifecycleEvent evt)
    {
        System.out.println("MyWeblogicAppListener(preStart) -- we should always see you..");
    } // preStart

    public void postStart(ApplicationLifecycleEvent evt)
    {
        System.out.println("MyWeblogicAppListener(postStart) -- we should always see you..");

    } // postStart

    public void preStop(ApplicationLifecycleEvent evt)
    {
        System.out.println("MyWeblogicAppListener(preStop) -- we should always see you..");
    } // preStop

    public void postStop(ApplicationLifecycleEvent evt)
    {
        System.out.println("MyWeblogicAppListener(postStop) -- we should always see you..");
    } // postStop

    public static void main(String[] args)
    {
        System.out.println("MyWeblogicAppListener(main): in main .. we should never see you..");
    } // main

}