import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.spi.QueryCache;
import org.hibernate.cache.spi.QueryCacheFactory;
import org.hibernate.cache.spi.UpdateTimestampsCache;
import org.hibernate.cache.internal.StandardQueryCache;

public class Hibernate515300600 implements QueryCacheFactory {

    @Override
    public QueryCache getQueryCache(
            final String regionName,
            final UpdateTimestampsCache updateTimestampsCache,
            final SessionFactoryOptions settings,
            final Properties props) throws HibernateException
    {
        return new StandardQueryCache(settings, props, updateTimestampsCache, regionName);
    }
}
