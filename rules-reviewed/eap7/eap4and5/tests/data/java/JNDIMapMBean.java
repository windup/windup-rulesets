import org.jboss.system.ServiceMBean;
import javax.naming.NamingException;

public interface JNDIMapMBean extends ServiceMBean
{
    public String getJndiName();
    public void setJndiName(String jndiName) throws NamingException;
} 