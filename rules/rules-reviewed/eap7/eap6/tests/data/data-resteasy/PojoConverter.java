import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.spi.StringConverter;
import javax.ws.rs.ext.Provider;

@Provider
public static class PojoConverter implements StringConverter<Pojo>
{
    public POJO fromString(String str)
    {
        System.out.println("FROM STRNG: " + str);
        POJO pojo = new POJO();
        pojo.setName(str);
        return pojo;
    }

    public String toString(POJO value)
    {
        return value.getName();
    }
}

public static class POJO
{
   private String name;

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }
}