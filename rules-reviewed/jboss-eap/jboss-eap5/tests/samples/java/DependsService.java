//package org.jboss.tutorial.service.bean;

import org.test.OtherServiceManagement;

import javax.ejb.Remote;
import org.jboss.annotation.ejb.Service;
import org.jboss.annotation.ejb.Depends;


@Service(objectName = "jboss:custom=Name")
@Remote(ServiceOneRemote.class)
@Depends({"jboss:service=someService"})
public class DependsService implements ServiceOneRemote, ServiceOneManagement
{
    int attribute;

    @Depends({"jboss.j2ee:service=EJB3,name=org.test.OtherService,type=service"})
    OtherServiceManagement other;


    public void setAttribute(int attribute)
    {
        this.attribute = attribute;
    }


    public int getAttribute()
    {
        return this.attribute;
    }


    public String sayHello()
    {
        return "Hello from service One";
    }


    // Lifecycle methods

    public void create() throws Exception
    {
        System.out.println("ServiceOne - Creating");
    }


    public void start() throws Exception
    {
        System.out.println("ServiceOne - Starting");
    }


    public void stop()
    {
        System.out.println("ServiceOne - Stopping");
    }


    public void destroy()
    {
        System.out.println("ServiceOne - Destroying");
    }

}
