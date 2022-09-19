import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

@javax.annotation.security.DeclareRoles()
@javax.annotation.security.RolesAllowed()
public class SecuredServlet extends HttpServlet {
    public void init() throws ServletException {
          // Do required initialization
          message = "Hello World";
       }

       public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

          // Set response content type
          response.setContentType("text/html");

          // Actual logic goes here.
          PrintWriter out = response.getWriter();
          out.println("<h1>" + message + "</h1>");
       }

       public void destroy() {
          // do nothing.
       }
}
