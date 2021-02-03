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
import org.ocpsoft.rewrite.context.EvaluationContext

final IssueCategory potentialIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.POTENTIAL)

ruleSet("quarkus1-11-resteasy-jackson-groovy")
        .addSourceTechnology(new TechnologyReference("quarkus1", "(,10]"))
        .addTargetTechnology(new TechnologyReference("quarkus1", "[11,)")) 
        .addRule()
        .when(SourceMode.isDisabled(),
                Query.fromType(FileModel)
                .withProperty(FileModel.IS_DIRECTORY, Boolean.TRUE)
                .withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.REGEX, ".*/io/quarkus/jackson\$"))
        .perform(new AbstractIterationOperation<FileModel>() {
            void perform(GraphRewrite event, EvaluationContext context, FileModel payload) {
                //ensure that the dependency being searched for is present in the current application
                //rather than any application in the MTA project
                final String sourceBasePath = payload.getFilePath().replace("/io/quarkus/jackson", "")
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
                ((Hint) Hint.titled("The Jackson ObjectMapper ignores unknown properties")
                    .withText("""The default `ObjectMapper` obtained via CDI and consumed by the Quarkus extensions now ignores unknown properties 
                            (by disabling the `DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES` feature).  
                            Refer to the guide below for more details about how to return to the previous behaviour.""")
                    .withIssueCategory(potentialIssueCategory)
                    .with(Link.to("Quarkus - Writing JSON REST Services", "https://quarkus.io/guides/rest-json#jackson"))
                    .with(Link.to("Quarkus - Migraton Guide 1.11", "https://github.com/quarkusio/quarkus/wiki/Migration-Guide-1.11"))
                    .withEffort(1)
                ).performParameterized(event, context, folderLocationModel)
            }
        })
        .withId("quarkus1-11-resteasy-jackson-groovy-00000")
