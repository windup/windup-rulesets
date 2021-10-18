package quarkus.reactive

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

ruleSet("reactive-to-quarkus-groovy")
        .addSourceTechnology(new TechnologyReference("reactive", null))
        .addTargetTechnology(new TechnologyReference("quarkus", null))
        .addRule()
        .when(SourceMode.isDisabled(),
                Query.fromType(FileModel)
                .withProperty(FileModel.IS_DIRECTORY, Boolean.TRUE)
                .withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.REGEX, ".*/io/vertx/mysqlclient\$"))
        .perform(new AbstractIterationOperation<FileModel>() {
            void perform(GraphRewrite event, EvaluationContext context, FileModel payload) {
                final String sourceBasePath = payload.getFilePath().replaceAll("/io/vertx/mysqlclient\$", "")
                final String dependencyJarName = sourceBasePath.substring(sourceBasePath.lastIndexOf("/") + 1)
                WindupConfigurationModel windupConfigurationModel = WindupConfigurationService.getConfigurationModel(event.getGraphContext())
                boolean packageComesFromAnalyzedApplication = false
                windupConfigurationModel.getInputPaths().each {
                    if (!packageComesFromAnalyzedApplication && it.filePath.endsWith(dependencyJarName)) packageComesFromAnalyzedApplication = true
                }
                if (!packageComesFromAnalyzedApplication) return
                final String targetFolderPath = sourceBasePath +"/io/quarkus/reactive/mysql/client/runtime"
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
                ((Hint) Hint.titled("Replace the 'vertx-mysql-client' dependency with Quarkus 'quarkus-reactive-mysql-client' extension")
                    .withText("""A folder path related to a package from the `io.vertx:vertx-mysql-client` dependency has been found.  
                                    Replace the `io.vertx:vertx-mysql-client` dependency with the Quarkus dependency `io.quarkus:quarkus-reactive-mysql-client` in the application's dependencies management system (Maven, Gradle).  
                                    Further information in the link below.""")
                    .withIssueCategory(mandatoryIssueCategory)
                    .with(Link.to("Quarkus - Guide", "https://quarkus.io/guides/reactive-sql-clients"))
                    .withEffort(1)
                ).performParameterized(event, context, folderLocationModel)
            }
        })
        .withId("quarkus-reactive-mysql-client-groovy-00000")
