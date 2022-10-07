import com.ibm.websphere.management.AdminService;
import com.ibm.ejs.ras.RasHelper;
import com.ibm.websphere.runtime.ServerName;

public class TestServerName {
	public static void main(String[] args) {
		String serverName1 = AdminService.getProcessName();
		String serverName2 = RasHelper.getServerName();
		String serverName3 = ServerName.getDisplayName();
		String serverName4 = ServerName.getFullName();

		System.out.println(serverName1 + serverName2 + serverName3 + serverName4);
	}
}
