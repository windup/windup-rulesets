import java.lang.SecurityManager;
import java.rmi.RMISecurityManager;
import java.security.AccessController;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.DomainCombiner;
import java.security.Policy;
import java.security.PolicySpi;
import java.security.Policy.Parameters;
import javax.security.auth.Subject;
import javax.security.auth.SubjectDomainCombiner;
import java.util.concurrent.Executors;

public class TestSecurityManager implements runnable
{
    public static void main (String[] args)
    {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
          }

          TestSecurityManager tg = new TestSecurityManager();
          tg.func();

          java.util.logging.LogManager.getLogManager().checkAccess();
          
    }
    public void func() {
        try {     
           ThreadGroup pGroup = new ThreadGroup("Parent ThreadGroup");
      
           // create a child ThreadGroup for parent ThreadGroup
           ThreadGroup cGroup = new ThreadGroup(pGroup, "Child ThreadGroup");
  
           Thread t1 = new Thread(pGroup, this);
           System.out.println("Starting " + t1.getName() + "...");
           t1.start();
           t1.checkAccess();
              
           Thread t2 = new Thread(cGroup, this);
           System.out.println("Starting " + t2.getName() + "...");
           t2.start();
              
           // checking the access for these ThreadGroups.
           try {
              pGroup.checkAccess();
              System.out.println(pGroup.getName() + " has access");
              cGroup.checkAccess();
              System.out.println(cGroup.getName() + " has access");
           } catch (SecurityException ex) {
              System.out.println("No access: " + ex.toString());
           }
           // block until the other threads finish
           t1.join();
           t2.join();
        } catch (InterruptedException ex) {
           System.out.println(ex.toString());
        }
     }
  
     // implements run()
     public void run() {
  
        for(int i = 0;i<1000;i++) {
           i++;
        }
        System.out.println(Thread.currentThread().getName() + " finished executing");
     }
     public static <T> Callable<T> privilegedCallable(Callable<T> callable) {
        return Executors.privilegedCallable(callable);
     }
     public static <T> Callable<T> privilegedCallableUsingCurrentClassLoader(Callable<T> callable) {
        return Executors.privilegedCallableUsingCurrentClassLoader(callable);
     }
     public static ThreadFactory privilegedThreadFactory() {
        return Executors.privilegedThreadFactory();
     }
      
     public static void testSubject() throws Exception {
        Subject s = new Subject();
        s.getPrincipals().add(new javax.security.auth.x500.X500Principal("CN=test"));
        s.getPrivateCredentials().add(new String("test"));
        try {
            javax.security.auth.Subject.doAsPrivileged(s, new PrivilegedAction() {
     
                public Object run() {
                    java.util.Iterator i = javax.security.auth.Subject.getSubject(AccessController.getContext()).getPrivateCredentials().iterator();
                    return i.next();
                }
            }, null);
            System.out.println("Test succeeded");
        } catch (Exception e) {
            System.out.println("Test failed");
            e.printStackTrace();
            throw e;
        }
    }
}