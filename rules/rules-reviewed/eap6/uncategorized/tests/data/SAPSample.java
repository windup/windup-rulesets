import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;

public class SAPSample
{

    public static void main(String[] args)
    {
        IEnterpriseSession enterpriseSession = null;
        ReportEngines reportEngines = null;
        try
        {
            System.out.println("Connecting...");
            ISessionMgr sessionMgr = CrystalEnterprise.getSessionMgr();
            enterpriseSession = sessionMgr.logon("Administrator",
                        "", "localhost", "secEnterprise");
            reportEngines = (ReportEngines) enterpriseSession
                        .getService("ReportEngines");
            ReportEngine wiRepEngine = (ReportEngine) reportEngines
                        .getService(ReportEngines.ReportEngineType.WI_REPORT_ENGINE);

            IInfoStore infoStore = (IInfoStore) enterpriseSession.getService("InfoStore");
            String query = "select SI_NAME, SI_ID from CI_INFOOBJECTS "
                        + "where SI_KIND = 'Webi' and SI_INSTANCE=0";
            IInfoObjects infoObjects = (IInfoObjects) infoStore.query(query);
            for (Object object : infoObjects)
            {
                IInfoObject infoObject = (IInfoObject) object;
                String path = getInfoObjectPath(infoObject);
                if (path.startsWith("/"))
                {
                    DocumentInstance widoc = wiRepEngine.openDocument(infoObject.getID());
                    String doc = infoObject.getTitle();
                    System.out.println(path + "/" + doc);
                    printDocumentVariables(widoc);
                    widoc.closeDocument();
                }
            }
        }
        catch (SDKException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (reportEngines != null)
                reportEngines.close();
            if (enterpriseSession != null)
                enterpriseSession.logoff();
        }
        System.out.println("Finished!");
    }

    public static void printDocumentVariables(DocumentInstance widoc)
    {
        ReportDictionary dic = widoc.getDictionary();
        VariableExpression[] variables = dic.getVariables();
        for (VariableExpression e : variables)
        {
            String name = e.getFormulaLanguageID();
            String expression = e.getFormula().getValue();
            System.out.println(" " + name + " " + expression);
        }
    }

    public static String getInfoObjectPath(IInfoObject infoObject)
                throws SDKException
    {
        String path = "";
        while (infoObject.getParentID() != 0)
        {
            infoObject = infoObject.getParent();
            path = "/" + infoObject.getTitle() + path;
        }
        return path;
    }
}