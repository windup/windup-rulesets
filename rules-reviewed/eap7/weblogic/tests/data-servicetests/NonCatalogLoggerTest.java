// Contains code that should match for the following tests:
//
// weblogic-services-03000-test
//

import weblogic.i18n.logging.NonCatalogLogger;
import weblogic.logging.ConsoleHandler;
import weblogic.i18n.logging.Loggable;

class NonCatalogLoggerTest
{
    private static final NonCatalogLogger LOG = new NonCatalogLogger("ProductCatalogBean");

    public void doThings()
    {
        LOG.info("Called Remove.");
    }
}
