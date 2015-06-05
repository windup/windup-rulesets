// Contains code that should match for the following tests:
//
// weblogic-webservices-01000-test
// weblogic-webservices-03000-test

import weblogic.wsee.jaxrpc.ServiceImpl;

import weblogic.wsee.connection.transport.http.HttpTransportInfo;

public class ServiceImplSubclassTest extends ServiceImpl
{
    public void foo() {
        HttpTransportInfo info = new HttpTransportInfo();
        info.setUsername("foo");
    }
}
