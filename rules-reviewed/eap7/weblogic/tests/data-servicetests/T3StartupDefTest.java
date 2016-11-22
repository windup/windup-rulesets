// Contains code that should match for the following tests:
//
// weblogic-services-01000-test
//

import java.util.Hashtable;

import weblogic.common.T3ServicesDef;
import weblogic.common.T3StartupDef;

public class T3StartupDefTest implements T3StartupDef
{
    public void setServices(T3ServicesDef services)
    {
        return;
    }

    public String startup(String name, Hashtable ht)
    {
        return null;
    }
}
