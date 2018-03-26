import weblogic.javaee.TransactionTimeoutSeconds;
import weblogic.ejbgen.ActivationConfigProperties;
import weblogic.ejbgen.ActivationConfigProperty;
import weblogic.ejbgen.MessageDriven;
import weblogic.ejb.GenericMessageDrivenBean;

@TransactionTimeoutSeconds(value=50)
@ActivationConfigProperties(
        {
                @ActivationConfigProperty(
                        name="connectionFactoryJndiName", value="JNDINameOfMDBSourceCF"
                )
        }
)
@MessageDriven(ejbName = "MyMessageDrivenBean",
        destinationJndiName = "MyMessageDrivenBeanJndiName",
        destinationType = "javax.jms.Queue")
public class MyMessageDrivenBean 
        extends GenericMessageDrivenBean
{
    private String property;
}