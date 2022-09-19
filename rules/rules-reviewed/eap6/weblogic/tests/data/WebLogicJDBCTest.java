// Contains code that should match for the following tests:
//
// weblogic-09000-test
// weblogic-10000-test
//

import weblogic.jdbc.vendor.oracle.OracleThinClob;


class WebLogicJDBCTest
{
    public void connectAndDoThings()
    {
        OracleThinClob clob = null;
        clob.getCharacterOutputStream();
    }
}
