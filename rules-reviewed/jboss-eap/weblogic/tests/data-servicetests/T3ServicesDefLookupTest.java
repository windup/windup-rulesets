// Contains code that should match for the following tests:
//
// weblogic-services-02000-test
//

import java.util.Hashtable;
import javax.naming.InitialContext;

import weblogic.common.T3ServicesDef;

public class T3ServicesDefLookupTest
{
    public String doSomething()
    {
        InitialContext context = new InitialContext();
        T3ServicesDef t3 = (T3ServicesDef)context.lookup("weblogic.common.T3Services");
    }
}
