package examples.servlets;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import commonj.timers.TimerManager;
import commonj.timers.TimerListener;
import commonj.timers.Timer;

/**
* TimerServlet demonstrates a simple use of commonj timers
*/
public class TimerServlet extends HttpServlet {

/**
  * A very simple implementation of the service method,
  * which schedules a commonj timer.
  */
  public void service(HttpServletRequest req, HttpServletResponse res)
    throws IOException
  {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    try {
      InitialContext ic = new InitialContext();
      TimerManager tm = (TimerManager)ic.lookup
        ("java:comp/env/tm/default");
      // Execute timer every 10 seconds starting immediately
      tm.schedule (new MyTimerListener(), 0, 10*1000);
      out.println("<h4>Timer scheduled!</h4>");
    } catch (NamingException ne) {
      ne.printStackTrace();
      out.println("<h4>Timer schedule failed!</h4>");
    }
  }

  private static class MyTimerListener implements TimerListener {
    public void timerExpired(Timer timer) {
      System.out.println("timer expired called on " + timer);
      // some useful work here ...
      // let's just cancel the timer
      System.out.println("cancelling timer ...");
      timer.cancel();
    }
  }
}