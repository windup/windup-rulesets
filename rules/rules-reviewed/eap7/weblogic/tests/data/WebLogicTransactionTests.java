// Contains code that should match for the following tests:
//
// weblogic-04000-test
// weblogic-05000-test
// weblogic-06000-test
// weblogic-07000-test
// weblogic-08000-test
//

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import weblogic.transaction.ClientTransactionManager;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.TxHelper;

import com.acme.anvil.vo.LogEvent;

class WebLogicTransactionTests
{
	public static void publishLogEvent()
    {
		//get a reference to the transaction manager to suspend the current transaction incase of exception.
		ClientTransactionManager ctm = TransactionHelper.getTransactionHelper().getTransactionManager();

        // also, get it another way, just to show usage of TxHelper
        TransactionManager tm = TxHelper.getTransactionManager();
		Transaction saveTx = null;
		try
        {
			saveTx = ctm.forceSuspend();

			// just to verify that the condition matches on this as well
			tm.forceSuspend();

			try
            {
				Context ic = getContext();
				sender.send(logMsg);
			}
            catch (NamingException e)
            {
				e.printStackTrace();
			}
		}
        finally
        {
			// just to verify that the condition matches on this as well
			tm.forceSuspend();
			ctm.forceResume(saveTx);
		}
	}

	private static Context getContext() throws NamingException {
		Properties environment = new Properties();
		environment.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		environment.put(Context.PROVIDER_URL, "t3://localhost:7001");
		Context context = new InitialContext(environment);

		return context;
	}
}
