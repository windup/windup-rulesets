import javax.xml.soap.SOAPElementFactory;
import javax.xml.soap.Name;

public class JakartaSOAP {

    public static void main(String[] args) {
        SOAPElementFactory factory = SOAPElementFactory.newInstance();
        
        Name name = null;
        factory.create(name);
        factory.create("name");
        factory.create("name", "name", "name");
    }
}