import org.hibernate.cache.RegionFactory;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cache.spi.AbstractRegionFactory;
import org.hibernate.testing.cache.CachingRegionFactory;
import org.hibernate.cache.spi.support.RegionFactoryTemplate;

public class Hibernate515300500 {

    public void aMethod(String queryString) {
        queryString.toLowerCase();
    }
}