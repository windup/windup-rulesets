// Contains code that should match for the following tests:
//
// weblogic-webapp-07000-test
// weblogic-webapp-08000-test
//
import java.io.IOException;

import weblogic.security.Security;
import weblogic.security.spi.WLSUser;
import weblogic.security.spi.WLSGroup;

class SampleWebLogicSecurity
{
    private void doSubjectStuff(PrintWriter out, HttpServletRequest request, HttpServletResponse response)
    {
        Subject subject = Security.getCurrentSubject();
        StringBuffer groups = new StringBuffer();
        String user = null;
        boolean first = true;
        for (Principal p : subject.getPrincipals())
        {
            if (p instanceof WLSGroup)
            {
                if (first)
                {
                    first = false;
                }
                else
                {
                    groups.append(", ");
                }
                groups.append(p.getName());
            }
            else if (p instanceof WLSUser)
            {
                user = p.getName();
            }
        }

        out.printf("<p>RemoteUser: %s, User: %s, belongs to: %s</p>", request.getUserPrincipal().getName(), user, groups);
    }
}
