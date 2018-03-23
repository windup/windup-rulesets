import weblogic.javaee.TransactionTimeoutSeconds;
import weblogic.ejbgen.ActivationConfigProperties;
import weblogic.ejbgen.ActivationConfigProperty;

@TransactionTimeoutSeconds(value=50)
@ActivationConfigProperties(
        {
                @ActivationConfigProperty(
                        name="connectionFactoryJndiName", value="JNDINameOfMDBSourceCF"
                )
        }
)
public class MyEJB 
{
    private String property;
}