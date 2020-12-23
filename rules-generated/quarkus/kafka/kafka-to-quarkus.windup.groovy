package quarkus.kafka

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

final IssueCategory mandatoryIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY)

ruleSet("kafka-to-quarkus-groovy")
        .addSourceTechnology(new TechnologyReference("kafka", null))
        .addTargetTechnology(new TechnologyReference("quarkus", null))
        .addRule()
        .when(SourceMode.isDisabled(),
                Query.fromType(FileModel)
                .withProperty(FileModel.IS_DIRECTORY, Boolean.TRUE)
                .withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.REGEX, ".*/org/apache/kafka/clients/admin\$"))
        .perform(new AbstractIterationOperation<FileModel>() {
            void perform(GraphRewrite event, EvaluationContext context, FileModel payload) {
                final String sourceBasePath = payload.getFilePath().replace("/org/apache/kafka/clients/admin", "")
                final String dependencyJarName = sourceBasePath.substring(sourceBasePath.lastIndexOf("/") + 1)
                WindupConfigurationModel windupConfigurationModel = WindupConfigurationService.getConfigurationModel(event.getGraphContext())
                boolean packageComesFromAnalyzedApplication = false
                windupConfigurationModel.getInputPaths().each {
                    if (!packageComesFromAnalyzedApplication && it.filePath.endsWith(dependencyJarName)) packageComesFromAnalyzedApplication = true
                }
                if (!packageComesFromAnalyzedApplication) return
                final String targetFolderPath = sourceBasePath +"/io/quarkus/kafka/client/health"
                final boolean foundQuarkusExtensionFolder = Query.fromType(FileModel)
                        .withProperty(FileModel.IS_DIRECTORY, Boolean.TRUE)
                        .withProperty(FileModel.FILE_PATH, targetFolderPath).as("target_folder").evaluate(event, context)
                if (foundQuarkusExtensionFolder) return
                final GraphService<FileLocationModel> fileLocationService = new GraphService<>(event.getGraphContext(), FileLocationModel.class)
                final FileLocationModel folderLocationModel = fileLocationService.create()
                folderLocationModel.setFile(payload)
                folderLocationModel.setColumnNumber(1)
                folderLocationModel.setLineNumber(1)
                folderLocationModel.setLength(1)
                folderLocationModel.setSourceSnippit("Folder Match")
                ((Hint) Hint.titled("Replace the 'kafka-clients' dependency with Quarkus 'quarkus-kafka-client' extension")
                    .withText("""A folder path related to a package from the `org.apache.kafka:kafka-clients` dependency has been found.  
                                    Replace the `org.apache.kafka:kafka-clients` dependency with the Quarkus dependency `io.quarkus:quarkus-kafka-client` in the application's dependencies management system (Maven, Gradle).  
                                    Further information in the link below.""")
                    .withIssueCategory(mandatoryIssueCategory)
                    .with(Link.to("Quarkus - Guide", "https://quarkus.io/guides/kafka"))
                    .withEffort(1)
                ).performParameterized(event, context, folderLocationModel)
            }
        })
        .withId("quarkus-kafka-client-groovy-00000")
        .addRule()
        .when(SourceMode.isDisabled(),
                Query.fromType(FileModel)
                .withProperty(FileModel.IS_DIRECTORY, Boolean.TRUE)
                .withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.REGEX, ".*/org/apache/kafka/streams\$"))
        .perform(new AbstractIterationOperation<FileModel>() {
            void perform(GraphRewrite event, EvaluationContext context, FileModel payload) {
                final String sourceBasePath = payload.getFilePath().replace("/org/apache/kafka/streams", "")
                final String dependencyJarName = sourceBasePath.substring(sourceBasePath.lastIndexOf("/") + 1)
                WindupConfigurationModel windupConfigurationModel = WindupConfigurationService.getConfigurationModel(event.getGraphContext())
                boolean packageComesFromAnalyzedApplication = false
                windupConfigurationModel.getInputPaths().each {
                    if (!packageComesFromAnalyzedApplication && it.filePath.endsWith(dependencyJarName)) packageComesFromAnalyzedApplication = true
                }
                if (!packageComesFromAnalyzedApplication) return
                final String targetFolderPath = sourceBasePath +"/io/quarkus/kafka/streams/runtime"
                final boolean foundQuarkusExtensionFolder = Query.fromType(FileModel)
                        .withProperty(FileModel.IS_DIRECTORY, Boolean.TRUE)
                        .withProperty(FileModel.FILE_PATH, targetFolderPath).as("target_folder").evaluate(event, context)
                if (foundQuarkusExtensionFolder) return
                final GraphService<FileLocationModel> fileLocationService = new GraphService<>(event.getGraphContext(), FileLocationModel.class)
                final FileLocationModel folderLocationModel = fileLocationService.create()
                folderLocationModel.setFile(payload)
                folderLocationModel.setColumnNumber(1)
                folderLocationModel.setLineNumber(1)
                folderLocationModel.setLength(1)
                folderLocationModel.setSourceSnippit("Folder Match")
                ((Hint) Hint.titled("Replace the 'kafka-streams' dependency with Quarkus 'quarkus-kafka-streams' extension")
                    .withText("""A folder path related to a package from the `org.apache.kafka:kafka-streams` dependency has been found.  
                                    Replace the `org.apache.kafka:kafka-streams` dependency with the Quarkus dependency `io.quarkus:quarkus-kafka-streams` in the application's dependencies management system (Maven, Gradle).  
                                    Further information in the link below.""")
                    .withIssueCategory(mandatoryIssueCategory)
                    .with(Link.to("Quarkus - Guide", "https://quarkus.io/guides/kafka-streams"))
                    .withEffort(1)
                ).performParameterized(event, context, folderLocationModel)
            }
        })
        .withId("quarkus-kafka-streams-groovy-00000")
