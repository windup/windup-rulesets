import java.util.Properties;
import org.hibernate.search.spi.ServiceProvider;
import org.infinispan.Cache;
import org.infinispan.manager.CacheManager;

public class CacheServiceProvider implements ServiceProvider<Cache> {
    private CacheManager manager;
    

    public void start(Properties properties) {
        //read configuration
        manager = new CacheManager(properties);
    }

    public Cache getService() {
        return manager.getCache(DEFAULT);
    }

    void stop() {
        manager.close();
    }
}
