package examples.activation; 

import java.rmi.*;
import java.rmi.activation.*;

public class ExtendsActivatable
        extends Activatable 
        implements MyRemoteInterface
{

    public ExtendsActivatable(ActivationID id, MarshalledObject data) 
        throws RemoteException
    {
        super(id, 0);
    }
        
    public Object remoteMethod(Object obj) {
        return obj;
    }
}