// Contains code that should match for the following tests:
//
// weblogic-catchall-01000-test
//
import weblogic.i18n.logging.NonCatalogLogger;
import com.tangosol.io.ReadBuffer;
import com.bea.BeaImport;
import bea.jolt.JoltService;
import oracle.sql.OracleConnection;
import oracle.somepackage.ClassFactory;
import com.oracle.ProprietaryOracleClass;
import com.weblogic.ProprietaryWeblogicClass;


class NonCatalogLoggerTest
{
    private static NonCatalogLogger LOG = new NonCatalogLogger("myLoggerName");

    public void log()
    {
        LOG.alert("logged!");
    }

    private class MyOracleConnection implements OracleConnection {


    }
}
