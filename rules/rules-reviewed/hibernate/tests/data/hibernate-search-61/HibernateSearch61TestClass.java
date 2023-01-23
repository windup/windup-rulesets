import org.hibernate.search.backend.elasticsearch.client.spi.ElasticsearchHttpClientConfigurationContext;
import org.hibernate.search.backend.elasticsearch.client.spi.ElasticsearchHttpClientConfigurer;
import org.hibernate.search.backend.elasticsearch.util.spi.URLEncodedString;
import org.hibernate.search.engine.backend.common.spi.FieldPaths;
import org.hibernate.search.engine.backend.document.model.dsl.spi.IndexSchemaObjectFieldNodeBuilder;
import org.hibernate.search.engine.backend.document.model.dsl.spi.IndexSchemaObjectNodeBuilder;
import org.hibernate.search.engine.backend.document.model.dsl.spi.IndexSchemaRootNodeBuilder;
import org.hibernate.search.engine.backend.index.spi.IndexManagerImplementor;
import org.hibernate.search.engine.backend.work.execution.spi.IndexIndexingPlan;
import org.hibernate.search.engine.backend.work.execution.spi.IndexIndexingPlanExecutionReport;
import org.hibernate.search.engine.common.timing.spi.Deadline;
import org.hibernate.search.engine.cfg.spi.ConfigurationPropertySource;

public class HibernateSearch61TestClass {

    public static void main(String[] args) {
        ConfigurationPropertySource configurationPropertySource;
        ElasticsearchHttpClientConfigurer elasticsearchHttpClientConfigurer;
        ElasticsearchHttpClientConfigurationContext elasticsearchHttpClientConfigurationContext;
        Deadline deadline;
        IndexIndexingPlanExecutionReport indexIndexingPlanExecutionReport;
        URLEncodedString.fromJSon("{hey:1}");
        FieldPaths.absolutize("1", "2", "3");
        IndexManagerImplementor indexManagerImplementor;
        indexManagerImplementor.createIndexingPlan(null, null, null);

        IndexIndexingPlan indexIndexingPlan = null;
        indexIndexingPlan.executeAndReport();

        IndexSchemaObjectNodeBuilder indexSchemaObjectNodeBuilder;
        IndexSchemaObjectFieldNodeBuilder indexSchemaObjectFieldNodeBuilder;
        IndexSchemaRootNodeBuilder indexSchemaRootNodeBuilder;

    }
}
