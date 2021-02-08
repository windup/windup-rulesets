import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.operation.iteration.AbstractIterationOperation
import org.jboss.windup.config.query.Query
import org.jboss.windup.config.query.QueryPropertyComparisonType
import org.jboss.windup.graph.model.FileLocationModel
import org.jboss.windup.graph.model.WindupConfigurationModel
import org.jboss.windup.graph.model.resource.FileModel
import org.jboss.windup.graph.service.GraphService
import org.jboss.windup.graph.service.WindupConfigurationService
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.rules.apps.java.condition.SourceMode
import org.jboss.windup.rules.files.condition.FileContent
import org.jboss.windup.rules.files.condition.FileContentMatches
import org.ocpsoft.rewrite.config.Or
import org.ocpsoft.rewrite.context.EvaluationContext

final IssueCategory potentialIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.POTENTIAL)

ruleSet("quarkus1-11-hibernate-elasticsearch-compiled-groovy")
        .addSourceTechnology(new TechnologyReference("quarkus", "(,10]"))
        .addTargetTechnology(new TechnologyReference("quarkus", "[11,)")) 
        .addRule()
        .when(SourceMode.isDisabled(),
                Query.fromType(FileModel)
                .withProperty(FileModel.IS_DIRECTORY, Boolean.TRUE)
                .withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.REGEX, ".*/io/quarkus/hibernate/search/orm/elasticsearch\$")
        )
        .perform(new AbstractIterationOperation<FileModel>() {
            void perform(GraphRewrite event, EvaluationContext context, FileModel payload) {
                //ensure that the dependency being searched for is present in the current application
                //rather than any application in the MTA project
                final String sourceBasePath = payload.getFilePath().replace("/io/quarkus/hibernate/search/orm/elasticsearch", "")
                final String dependencyJarName = sourceBasePath.substring(sourceBasePath.lastIndexOf("/") + 1)
                WindupConfigurationModel windupConfigurationModel = WindupConfigurationService.getConfigurationModel(event.getGraphContext())
                boolean packageComesFromAnalyzedApplication = false
                windupConfigurationModel.getInputPaths().each {
                    if (!packageComesFromAnalyzedApplication && it.filePath.endsWith(dependencyJarName)) packageComesFromAnalyzedApplication = true
                }
                if (!packageComesFromAnalyzedApplication) return
                final GraphService<FileLocationModel> fileLocationService = new GraphService<>(event.getGraphContext(), FileLocationModel.class)
                final FileLocationModel folderLocationModel = fileLocationService.create()
                folderLocationModel.setFile(payload)
                folderLocationModel.setColumnNumber(1)
                folderLocationModel.setLineNumber(1)
                folderLocationModel.setLength(1)
                folderLocationModel.setSourceSnippit("Folder Match")
                ((Hint) Hint.titled("The default required status for Elasticsearch indexes is now yellow")
                    .withText("""The default required status for Elasticsearch indexes is now `yellow`.  
                            If you have specific requirements and need to wait for indexes to be `green` on startup,
                            set `quarkus.hibernate-search.elasticsearch.schema-management.required-status` to `green`.  
                            Refer to the guide below for more details about how to return to the previous behaviour.""")
                    .withIssueCategory(potentialIssueCategory)
                    .with(Link.to("Quarkus - Hibernate Search Guide", "https://quarkus.io/guides/hibernate-search-orm-elasticsearch"))
                    .with(Link.to("Quarkus - Migration Guide 1.11", "https://github.com/quarkusio/quarkus/wiki/Migration-Guide-1.11"))
                    .withEffort(1)
                ).performParameterized(event, context, folderLocationModel)
            }
        })
        .withId("quarkus1-11-hibernate-elasticsearch-compiled-groovy-00000")
        .addRule()
        .when(SourceMode.isDisabled(),
                FileContent.matches("quarkus.hibernate-search-orm.elasticsearch.version").inFileNamed("application.properties")
        )
        .perform(new AbstractIterationOperation<FileLocationModel>() {
            void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
                // ensure that the application.properties being searched for is present
                // in the current application rather than any application in the MTA project
                final String sourceBasePath = payload.getFile().getFilePath().replace("application.properties", "")
                final String dependencyJarName = sourceBasePath.substring(sourceBasePath.lastIndexOf("/") + 1)
                WindupConfigurationModel windupConfigurationModel = WindupConfigurationService.getConfigurationModel(event.getGraphContext())
                boolean propertiesFileComesFromAnalyzedApplication = false
                windupConfigurationModel.getInputPaths().each {
                    if (!propertiesFileComesFromAnalyzedApplication && it.filePath.endsWith(dependencyJarName)) propertiesFileComesFromAnalyzedApplication = true
                }
                if (!propertiesFileComesFromAnalyzedApplication) return
                ((Hint) Hint.titled("The default required status for Elasticsearch indexes is now yellow")
                    .withText("""The default required status for Elasticsearch indexes is now `yellow`.  
                            If you have specific requirements and need to wait for indexes to be `green` on startup,
                            set `quarkus.hibernate-search.elasticsearch.schema-management.required-status` to `green`.  
                            Refer to the guide below for more details about how to return to the previous behaviour.""")
                    .withIssueCategory(potentialIssueCategory)
                    .with(Link.to("Quarkus - Hibernate Search Guide", "https://quarkus.io/guides/hibernate-search-orm-elasticsearch"))
                    .with(Link.to("Quarkus - Migration Guide 1.11", "https://github.com/quarkusio/quarkus/wiki/Migration-Guide-1.11"))
                    .withEffort(1)
                ).performParameterized(event, context, payload)
            }
        })
        .withId("quarkus1-11-hibernate-elasticsearch-compiled-groovy-00010")
