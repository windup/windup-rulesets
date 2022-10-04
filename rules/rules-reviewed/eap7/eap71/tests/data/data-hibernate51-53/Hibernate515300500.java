import org.hibernate.cache.RegionFactory;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cache.spi.AbstractRegionFactory;
import org.hibernate.testing.cache.CachingRegionFactory;
import org.hibernate.cache.spi.support.RegionFactoryTemplate;
import org.hibernate.cache.ehcache.EhCacheRegionFactory;
import org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory;
import org.hibernate.cache.infinispan.InfinispanRegionFactory
import org.hibernate.cache.infinispan.JndiInfinispanRegionFactory;
import org.hibernate.cache.internal.NoCachingRegionFactory;

public class Hibernate515300500 {

    public void aMethod(String queryString) {
        queryString.toLowerCase();
    }
}