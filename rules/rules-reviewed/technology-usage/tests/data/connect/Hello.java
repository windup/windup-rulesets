package org.windup.examples.technology.connect;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends java.rmi.Remote
{
    String sayHello() throws RemoteException;
}
