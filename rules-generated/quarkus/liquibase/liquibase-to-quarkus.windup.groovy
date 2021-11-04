package quarkus.liquibase

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

ruleSet("liquibase-to-quarkus-groovy")
        .addSourceTechnology(new TechnologyReference("liquibase", null))
        .addTargetTechnology(new TechnologyReference("quarkus", null))
        .addRule()
        .when(SourceMode.isDisabled(),
                Query.fromType(FileModel)
                .withProperty(FileModel.IS_DIRECTORY, Boolean.TRUE)
                .withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.REGEX, ".*/liquibase\$"))
        .perform(new AbstractIterationOperation<FileModel>() {
            void perform(GraphRewrite event, EvaluationContext context, FileModel payload) {
                final String sourceBasePath = payload.getFilePath().replaceAll("/liquibase\$", "")
                final String dependencyJarName = sourceBasePath.substring(sourceBasePath.lastIndexOf("/") + 1)
                WindupConfigurationModel windupConfigurationModel = WindupConfigurationService.getConfigurationModel(event.getGraphContext())
                boolean packageComesFromAnalyzedApplication = false
                windupConfigurationModel.getInputPaths().each {
                    if (!packageComesFromAnalyzedApplication && it.filePath.endsWith(dependencyJarName)) packageComesFromAnalyzedApplication = true
                }
                if (!packageComesFromAnalyzedApplication) return
                final String targetFolderPath = sourceBasePath +"/io/quarkus/liquibase"
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
                ((Hint) Hint.titled("Replace the 'liquibase-core' dependency with Quarkus 'quarkus-liquibase' extension")
                    .withText("""A folder path related to a package from the `org.liquibase:liquibase-core` dependency has been found.  
                                    Replace the `org.liquibase:liquibase-core` dependency with the Quarkus dependency `io.quarkus:quarkus-liquibase` in the application's dependencies management system (Maven, Gradle).  
                                    Further information in the link below.""")
                    .withIssueCategory(mandatoryIssueCategory)
                    .with(Link.to("Quarkus - Guide", "https://quarkus.io/guides/liquibase"))
                    .withEffort(1)
                ).performParameterized(event, context, folderLocationModel)
            }
        })
        .withId("quarkus-liquibase-groovy-00000")
        .addRule()
        .when(SourceMode.isDisabled(),
                Query.fromType(FileModel)
                .withProperty(FileModel.IS_DIRECTORY, Boolean.TRUE)
                .withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.REGEX, ".*/liquibase/ext/mongodb/change\$"))
        .perform(new AbstractIterationOperation<FileModel>() {
            void perform(GraphRewrite event, EvaluationContext context, FileModel payload) {
                final String sourceBasePath = payload.getFilePath().replaceAll("/liquibase/ext/mongodb/change\$", "")
                final String dependencyJarName = sourceBasePath.substring(sourceBasePath.lastIndexOf("/") + 1)
                WindupConfigurationModel windupConfigurationModel = WindupConfigurationService.getConfigurationModel(event.getGraphContext())
                boolean packageComesFromAnalyzedApplication = false
                windupConfigurationModel.getInputPaths().each {
                    if (!packageComesFromAnalyzedApplication && it.filePath.endsWith(dependencyJarName)) packageComesFromAnalyzedApplication = true
                }
                if (!packageComesFromAnalyzedApplication) return
                final String targetFolderPath = sourceBasePath +"/io/quarkus/liquibase/mongodb"
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
                ((Hint) Hint.titled("Replace the 'liquibase-mongodb' dependency with Quarkus 'quarkus-liquibase-mongodb' extension")
                    .withText("""A folder path related to a package from the `org.liquibase.ext:liquibase-mongodb` dependency has been found.  
                                    Replace the `org.liquibase.ext:liquibase-mongodb` dependency with the Quarkus dependency `io.quarkus:quarkus-liquibase-mongodb` in the application's dependencies management system (Maven, Gradle).  
                                    No specific guide is available.""")
                    .withIssueCategory(mandatoryIssueCategory)
                    .with(Link.to("Quarkus - Guide", "https://quarkus.io/guides/"))
                    .withEffort(1)
                ).performParameterized(event, context, folderLocationModel)
            }
        })
        .withId("quarkus-liquibase-mongodb-groovy-00000")
