// Contains code that should match for the following tests:
//
// weblogic-webapp-04000-test
//

import javax.servlet.Filter;

import weblogic.servlet.annotation.WLFilter;
import weblogic.servlet.annotation.WLInitParam;

/**
 * This is an example of a servlet that uses the propietary
 * @WLFilter and @WLInitParam annotations.
 *
 * This is not a fully functional class. Its sole purpose is
 * to demonstrate Windup rule addon processing.
 *
 * @author Windup-Team
 */
@WLFilter (
    name = "MyHintsRuleFilter",
    initParams = {
        @WLInitParam (name="catalog", value="spring"),
        @WLInitParam (name="language", value="English")
     },
     mapping = {"/catalog/*"}
)
public class SampleWebLogicFilter implements Filter {
    // no-op
}
