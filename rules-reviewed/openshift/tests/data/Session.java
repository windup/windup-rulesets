import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletLifeCycleExample extends HttpServlet {

    public void service(HttpServletRequest request, HttpServletResponse response) {
		// Get the session and the counter param attribute
		HttpSession session = request.getSession (true);
		Integer ival = (Integer) 
		session.getAttribute("simplesession.counter");
		if (ival == null) // Initialize the counter
		ival = new Integer (1);
		else // Increment the counter
		ival = new Integer (ival.intValue () + 1);
		// Set the new attribute value in the session
		session.setAttribute("simplesession.counter", ival);
		// deprecated - DO NOT USE
		session.putValue("simplesession.counter", ival);
		// Output the HTML page
		out.print("<HTML><body>");
		out.print("<center> You have hit this page ");
		out.print(ival + " times!");
		out.print("</body></html>");
	} 

}