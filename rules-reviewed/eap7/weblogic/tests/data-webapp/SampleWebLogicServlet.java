// Contains code that should match for the following tests:
//
// weblogic-webapp-02000-test
// weblogic-webapp-03000-test
//
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weblogic.servlet.annotation.WLInitParam;
import weblogic.servlet.annotation.WLServlet;

/**
 * This is an example of a servlet that uses the propietary
 * @WLServlet and @WLInitParam annotations.
 *
 * This is not a fully functional class. Its sole purpose is
 * to demonstrate Windup rule addon processing.
 *
 * @author Windup-Team
 */
@WLServlet (
    name = "catalog",
    runAs = "SuperEditor"
    initParams = {
        @WLInitParam (name="catalog", value="spring"),
        @WLInitParam (name="language", value="English")
     },
     mapping = {"/catalog/*"}
)
public class SampleWebLogicServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        // noop
    }

}
