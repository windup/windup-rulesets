package quarkus.hibernate

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

ruleSet("hibernate-to-quarkus-groovy")
        .addSourceTechnology(new TechnologyReference("hibernate", null))
        .addTargetTechnology(new TechnologyReference("quarkus", null))
        .addRule()
        .when(SourceMode.isDisabled(),
                Query.fromType(FileModel)
                .withProperty(FileModel.IS_DIRECTORY, Boolean.TRUE)
                .withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.REGEX, ".*/org/hibernate/envers\$"))
        .perform(new AbstractIterationOperation<FileModel>() {
            void perform(GraphRewrite event, EvaluationContext context, FileModel payload) {
                final String sourceBasePath = payload.getFilePath().replaceAll("/org/hibernate/envers\$", "")
                final String dependencyJarName = sourceBasePath.substring(sourceBasePath.lastIndexOf("/") + 1)
                WindupConfigurationModel windupConfigurationModel = WindupConfigurationService.getConfigurationModel(event.getGraphContext())
                boolean packageComesFromAnalyzedApplication = false
                windupConfigurationModel.getInputPaths().each {
                    if (!packageComesFromAnalyzedApplication && it.filePath.endsWith(dependencyJarName)) packageComesFromAnalyzedApplication = true
                }
                if (!packageComesFromAnalyzedApplication) return
                final String targetFolderPath = sourceBasePath +"/io/quarkus/hibernate/envers"
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
                ((Hint) Hint.titled("Replace the 'hibernate-envers' dependency with Quarkus 'quarkus-hibernate-envers' extension")
                    .withText("""A folder path related to a package from the `org.hibernate:hibernate-envers` dependency has been found.  
                                    Replace the `org.hibernate:hibernate-envers` dependency with the Quarkus dependency `io.quarkus:quarkus-hibernate-envers` in the application's dependencies management system (Maven, Gradle).  
                                    Further information in the link below.""")
                    .withIssueCategory(mandatoryIssueCategory)
                    .with(Link.to("Quarkus - Guide", "https://quarkus.io/guides/hibernate-orm#envers"))
                    .withEffort(1)
                ).performParameterized(event, context, folderLocationModel)
            }
        })
        .withId("quarkus-hibernate-envers-groovy-00000")
        .addRule()
        .when(SourceMode.isDisabled(),
                Query.fromType(FileModel)
                .withProperty(FileModel.IS_DIRECTORY, Boolean.TRUE)
                .withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.REGEX, ".*/org/hibernate/reactive/adaptor/impl\$"))
        .perform(new AbstractIterationOperation<FileModel>() {
            void perform(GraphRewrite event, EvaluationContext context, FileModel payload) {
                final String sourceBasePath = payload.getFilePath().replaceAll("/org/hibernate/reactive/adaptor/impl\$", "")
                final String dependencyJarName = sourceBasePath.substring(sourceBasePath.lastIndexOf("/") + 1)
                WindupConfigurationModel windupConfigurationModel = WindupConfigurationService.getConfigurationModel(event.getGraphContext())
                boolean packageComesFromAnalyzedApplication = false
                windupConfigurationModel.getInputPaths().each {
                    if (!packageComesFromAnalyzedApplication && it.filePath.endsWith(dependencyJarName)) packageComesFromAnalyzedApplication = true
                }
                if (!packageComesFromAnalyzedApplication) return
                final String targetFolderPath = sourceBasePath +"/io/quarkus/hibernate/reactive/runtime"
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
                ((Hint) Hint.titled("Replace the 'hibernate-reactive-core' dependency with Quarkus 'quarkus-hibernate-reactive' extension")
                    .withText("""A folder path related to a package from the `org.hibernate.reactive:hibernate-reactive-core` dependency has been found.  
                                    Replace the `org.hibernate.reactive:hibernate-reactive-core` dependency with the Quarkus dependency `io.quarkus:quarkus-hibernate-reactive` in the application's dependencies management system (Maven, Gradle).  
                                    Further information in the link below.""")
                    .withIssueCategory(mandatoryIssueCategory)
                    .with(Link.to("Quarkus - Quickstart", "https://github.com/quarkusio/quarkus-quickstarts/tree/master/hibernate-reactive-quickstart"))
                    .withEffort(1)
                ).performParameterized(event, context, folderLocationModel)
            }
        })
        .withId("quarkus-hibernate-reactive-groovy-00000")
        .addRule()
        .when(SourceMode.isDisabled(),
                Query.fromType(FileModel)
                .withProperty(FileModel.IS_DIRECTORY, Boolean.TRUE)
                .withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.REGEX, ".*/org/hibernate/search/backend/elasticsearch/aws/cfg\$"))
        .perform(new AbstractIterationOperation<FileModel>() {
            void perform(GraphRewrite event, EvaluationContext context, FileModel payload) {
                final String sourceBasePath = payload.getFilePath().replaceAll("/org/hibernate/search/backend/elasticsearch/aws/cfg\$", "")
                final String dependencyJarName = sourceBasePath.substring(sourceBasePath.lastIndexOf("/") + 1)
                WindupConfigurationModel windupConfigurationModel = WindupConfigurationService.getConfigurationModel(event.getGraphContext())
                boolean packageComesFromAnalyzedApplication = false
                windupConfigurationModel.getInputPaths().each {
                    if (!packageComesFromAnalyzedApplication && it.filePath.endsWith(dependencyJarName)) packageComesFromAnalyzedApplication = true
                }
                if (!packageComesFromAnalyzedApplication) return
                final String targetFolderPath = sourceBasePath +"/io/quarkus/hibernate/search/orm/elasticsearch/aws/runtime"
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
                ((Hint) Hint.titled("Replace the 'hibernate-search-backend-elasticsearch-aws' dependency with Quarkus 'quarkus-hibernate-search-orm-elasticsearch-aws' extension")
                    .withText("""A folder path related to a package from the `org.hibernate.search:hibernate-search-backend-elasticsearch-aws` dependency has been found.  
                                    Replace the `org.hibernate.search:hibernate-search-backend-elasticsearch-aws` dependency with the Quarkus dependency `io.quarkus:quarkus-hibernate-search-orm-elasticsearch-aws` in the application's dependencies management system (Maven, Gradle).  
                                    Further information in the link below.""")
                    .withIssueCategory(mandatoryIssueCategory)
                    .with(Link.to("Quarkus - Guide", "https://quarkus.io/guides/hibernate-search-orm-elasticsearch"))
                    .withEffort(1)
                ).performParameterized(event, context, folderLocationModel)
            }
        })
        .withId("quarkus-hibernate-search-orm-elasticsearch-aws-groovy-00000")
