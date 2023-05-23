// Contains code that should match for the following tests:
//
// weblogic-11000-test
//
import weblogic.i18n.logging.NonCatalogLogger;

class NonCatalogLoggerTest
{
    private static NonCatalogLogger LOG = new NonCatalogLogger("myLoggerName");

    public void log()
    {
        LOG.alert("logged!");
    }
}
