import javax.ejb.Stateful;

@Stateful
public class TransactionalBean {
    
    public String aTransactionalMethod(String str) {
        return "Hello " + str;
    }
}