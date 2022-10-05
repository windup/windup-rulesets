import com.ibm.websphere.security.WSSecurityHelper;

public class TestWebSphereUnavailableAPIsWSSecurityHelper {
	public static void main(String[] args) {
		String result1 = WSSecurityHelper.getFirstCaller();
		String result2 = WSSecurityHelper.getFirstServer();
		String result3 = WSSecurityHelper.getCallerList();
		String result4 = WSSecurityHelper.getServerList();
		String result5 = WSSecurityHelper.getPropagationAttributes();
		String result6 = WSSecurityHelper.addPropagationAttribute();
		String result7 = WSSecurityHelper.convertCookieStringToBytes();
		String result8 = WSSecurityHelper.revokeSSOCookiesForPortlets();
	}
}
