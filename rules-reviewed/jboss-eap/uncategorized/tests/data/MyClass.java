package environment.dependent;

import javax.naming.InitialContext;
import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.JMXConnectorFactory;

public class MyClass{
   public MyClass(){
   }

   public void myMethod() {
	Class.forName("environment.dependent.MyClass");
        Class.forName("environment.dependent.OtherClass");
        InitialContext context = new InitialContext();
        context.lookup("exampleString");
        ObjectName objectName = new ObjectName("someObjectName");
        JMXServiceURL url = new JMXServiceURL("Some service URL");
        JMXConnectorFactory.connect(url);
   }

}
