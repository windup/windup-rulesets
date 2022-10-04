import com.ibm.websphere.security.WSSecurityHelper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestWebSphereUnavailableSSOCookieMethod {
	public static void main(String[] args) {
		HttpServletRequest request = new HttpServletRequest();
		HttpServletResponse response = new HttpServletResponse();
		WSSecurityHelper.revokeSSOCookies(request, response);
	}
}
