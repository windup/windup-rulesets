
import java.lang.SecurityManager



public class AWTSecurityManagement {
    public boolean checkAWTSecurityPermissions()
    {
        SecurityManager security = System.getSecurityManager();

        return security.checkAwtEventQueueAccess() &&
                security.checkSystemClipboardAccess() &&
                security.checkTopLevelWindow(new Object());
    }
}