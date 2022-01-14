package quarkus.springboot

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

ruleSet("springboot-web-shaded-to-quarkus")
        .addSourceTechnology(new TechnologyReference("springboot", null))
        .addTargetTechnology(new TechnologyReference("quarkus", null))
        .addRule()
        .when(SourceMode.isDisabled(),
                Query.fromType(FileModel)
                        .withProperty(FileModel.IS_DIRECTORY, Boolean.TRUE)
                        .withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.REGEX, ".*/org/springframework/web\$"))
        .perform(new AbstractIterationOperation<FileModel>() {
            void perform(GraphRewrite event, EvaluationContext context, FileModel payload) {
                final String sourceBasePath = payload.getFilePath().replaceAll("/org/springframework/web\$", "")
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
                ((Hint) Hint.titled("Replace the 'spring-web' dependency with Quarkus 'quarkus-spring-web' extension")
                        .withText("""
                                    A folder path related to a package from the `org.springframework:spring-web` dependency has been found.  
                                    Replace the `org.springframework:spring-web` dependency with the Quarkus dependency `io.quarkus:quarkus-spring-web` and either `io.quarkus:quarkus-resteasy-jackson` or `io.quarkus:quarkus-resteasy-reactive-jackson` in the application's dependencies management system (Maven, Gradle).  
                                    Further information in the link below.
                                    """)
                        .withIssueCategory(mandatoryIssueCategory)
                        .with(Link.to("Quarkus - Guide", "https://quarkus.io/guides/spring-web"))
                        .withEffort(1))
            }
        })
        .withId("springboot-web-shaded-to-quarkus-00000")