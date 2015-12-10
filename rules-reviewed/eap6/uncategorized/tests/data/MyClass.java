package environment.dependent;

import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.JMXConnectorFactory;
import java.util.Hashtable;
import javax.naming.InitialContext;
import java.util.Properties;

public class MyClass{
   public MyClass(){
   }

   public void myMethod() {
	Class.forName("environment.dependent.MyClass");
        Class.forName("environment.dependent.OtherClass");
        Properties environment = new Properties();
	environment.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
	environment.put(Context.PROVIDER_URL, "t3://localhost:7001");
	InitialContext context = new InitialContext(environment);
        context.lookup("exampleString");
        ObjectName objectName = new ObjectName("someObjectName");
        JMXServiceURL url = new JMXServiceURL("Some service URL");
        JMXConnectorFactory.connect(url);
   }

}
