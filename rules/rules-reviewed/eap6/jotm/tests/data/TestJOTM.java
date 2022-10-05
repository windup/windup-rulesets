package data

import org.objectweb.jotm.*;

public class TestJOTM
{
    TestJOTM(){
        org.objectweb.jotm.jta.rmi.JTATransactionServiceContext jotmJTAcontext =
                new org.objectweb.jotm.jta.rmi.JTATransactionServiceContext();
    }
}