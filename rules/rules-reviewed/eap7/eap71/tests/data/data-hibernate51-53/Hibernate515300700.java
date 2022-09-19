import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.NaturalIdCacheStatistics;
import org.hibernate.SessionFactory;

public class Hibernate515300700 {

    public void aMethod(SessionFactory sf) {
        SecondLevelCacheStatistics secondLevelCacheStatistics = sf.getStatistics().getSecondLevelCacheStatistics("CACHE_ENTITY");
        secondLevelCacheStatistics.getEntries();
        NaturalIdCacheStatistics naturalIdCacheStatistics = sf.getStatistics().getNaturalIdCacheStatistics("CACHE_PROPERTY");
        naturalIdCacheStatistics.getEntries();
    }
}