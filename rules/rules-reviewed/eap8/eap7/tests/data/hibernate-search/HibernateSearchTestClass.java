import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.AnalyzerDefs;
import org.hibernate.search.annotations.AnalyzerDiscriminator;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.CacheFromIndex;
import org.hibernate.search.annotations.CalendarBridge;
import org.hibernate.search.annotations.CharFilterDef;
import org.hibernate.search.annotations.ClassBridge;
import org.hibernate.search.annotations.ClassBridges;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.DynamicBoost;
import org.hibernate.search.annotations.Facet;
import org.hibernate.search.annotations.Facets;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Key;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import org.hibernate.search.annotations.Normalizer;
import org.hibernate.search.annotations.NormalizerDef;
import org.hibernate.search.annotations.NormalizerDefs;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.NumericFields;
import org.hibernate.search.annotations.ProvidedId;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.SortableFields;
import org.hibernate.search.annotations.Spatial;
import org.hibernate.search.annotations.Spatials;
import org.hibernate.search.annotations.TikaBridge;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.hibernate.search.cfg.Environment;
import org.hibernate.search.elasticsearch.ElasticsearchProjectionConstants;
import org.hibernate.search.elasticsearch.cfg.ElasticsearchEnvironment;
import org.hibernate.search.engine.BoostStrategy;
import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.engine.impl.FacetHandling;
import org.hibernate.search.exception.ErrorHandler;
import org.hibernate.search.exception.SearchException;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.store.IndexShardingStrategy;
import org.hibernate.search.store.ShardIdentifierProvider;
import org.hibernate.search.store.ShardIdentifierProviderTemplate;

import java.util.Calendar;
import java.util.Date;

@Analyzer
@AnalyzerDef(name = "name")
@AnalyzerDefs(value = HibernateSearchTestClass.class)
@AnalyzerDiscriminator(impl = HibernateSearchTestClass.class)
@Boost(value = 0.12f)
@CacheFromIndex
@CharFilterDef()
@ClassBridge()
@ClassBridges
@ContainedIn
@FullTextFilterDef(name = "filter", impl = HibernateSearchTestClass.class)
@FullTextFilterDefs()
@Indexed
@Normalizer
@NormalizerDef()
@NormalizerDefs()
@TikaBridge
@TokenFilterDef()
@TokenizerDef()
public class HibernateSearchTestClass implements BoostStrategy {

    @CalendarBridge(resolution = Resolution.DAY)
    Calendar calendar;

    @DateBridge(resolution = Resolution.DAY)
    @DynamicBoost(impl = HibernateSearchTestClass.class)
    Date date;

    @DocumentId
    @IndexedEmbedded
    @NumericField
    @NumericFields()
    Integer id;

    @Facet
    @Facets(value = "value")
    @ProvidedId
    @SortableField
    @SortableFields()
    String field1;

    @Field
    @Fields()
    @Spatial
    @Spatials()
    @Factory
    String field2;

    @FieldBridge
    @Latitude
    @Longitude
    String field3;

    @Key
    public String method1() {
        FullTextEntityManager fullTextEntityManager;
        FullTextSession fullTextSession;

        FullTextQuery fullTextQuery;
        org.hibernate.search.jpa.FullTextQuery fullTextQuery1;

        org.apache.lucene.search.Query q;

        Sort sort;
        SortField sortField;

        ProjectionConstants projectionConstants;
        ElasticsearchProjectionConstants elasticsearchProjectionConstants;

        FacetHandling facetHandling;

        ErrorHandler errorHandler;

        SearchException e;

        IndexShardingStrategy indexShardingStrategy;
        ShardIdentifierProvider shardIdentifierProvider;
        ShardIdentifierProviderTemplate shardIdentifierProviderTemplate;

        SearchFactory searchFactory;

        MassIndexer massIndexer;
        org.hibernate.search.jsr352.massindexing.MassIndexingJob massIndexingJob;

        return "";
    }

    Environment environment;

    ElasticsearchEnvironment elasticsearchEnvironment;
}

