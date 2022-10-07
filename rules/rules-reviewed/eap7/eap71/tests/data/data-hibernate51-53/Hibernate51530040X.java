import org.hibernate.engine.spi.SessionFactoryImplementor;

public class Hibernate51530040X {

    public void aMethod(String regionName, SessionFactoryImplementor sessionFactoryImplementor) {
        sessionFactoryImplementor.getQueryCache();
        sessionFactoryImplementor.getQueryCache(regionName);
        sessionFactoryImplementor.getUpdateTimestampsCache();
        sessionFactoryImplementor.getSecondLevelCacheRegion(regionName);
        sessionFactoryImplementor.getSecondLevelCacheRegionAccessStrategy(regionName);
        sessionFactoryImplementor.getNaturalIdCacheRegion(regionName);
        sessionFactoryImplementor.getNaturalIdCacheRegionAccessStrategy(regionName);
        sessionFactoryImplementor.getAllSecondLevelCacheRegions();
    }
}