package org.jboss.as.quickstarts.singleton;

import java.rmi.RemoteException;
import javax.ejb.SessionBean;
import javax.ejb.EJBException;

public class AnotherCounter implements SessionBean {
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
