import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import weblogic.work.ExecuteThread;
import commonj.work.WorkManager;
import commonj.work.Work;
import commonj.work.WorkEvent;
import commonj.work.WorkException;
import commonj.work.WorkListener;

public class HelloWorldServlet extends HttpServlet {

   public void service(HttpServletRequest req, HttpServletResponse res)
      throws IOException
   {
      res.setContentType("text/html");
      PrintWriter out = res.getWriter();


      try {
         InitialContext ic = new InitialContext();
         System.out.println("## [servlet] executing in: " +
            ((ExecuteThread)Thread.currentThread()).getWorkManager()
            .getName());
         WorkManager wm = (WorkManager)ic.lookup
            ("java:comp/env/foo-servlet");
         System.out.println("## got Java EE work manager !!!!");
         wm.schedule(new Work(){
         public void run() {
         ExecuteThread th = (ExecuteThread) Thread.currentThread();
         System.out.println("## [servlet] self-tuning workmanager: " +
           th.getWorkManager().getName());
         }
         public void release() {}

         public boolean isDaemon() {return false;}
         });
} 
catch (NamingException ne) {
         ne.printStackTrace();} 

catch (WorkException e) {
         e.printStackTrace();
}

out.println("<h4>Hello World!</h4>");
// Do not close the output stream - allow the servlet engine to close it
// to enable better performance.
System.out.println("finished execution");}

}