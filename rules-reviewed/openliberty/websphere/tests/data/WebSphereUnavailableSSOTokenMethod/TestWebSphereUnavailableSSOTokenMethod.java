import com.ibm.websphere.security.WSSecurityHelper;

public class TestWebSphereUnavailableSSOTokenMethod {
	public static void main(String[] args) {
		WSSecurityHelper.getLTPACookieFromSSOToken();
	}
}
