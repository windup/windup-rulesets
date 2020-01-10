import java.rmi.RemoteException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class SingletonBean implements SessionBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(SingletonBean.class.getName());

	@PostConstruct
	public void start() {
		LOGGER.info(">>>>>>>> STARTING [" + this.getClass().getSimpleName() + "]");
	}

	// not called by EAP
	@PreDestroy
	public void stop() {
		LOGGER.info(">>>>>>>> DESTROYING [" + this.getClass().getSimpleName() + "]");

	}

	@Override
	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ejbRemove() throws EJBException, RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ejbActivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ejbPassivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub

	}
}

