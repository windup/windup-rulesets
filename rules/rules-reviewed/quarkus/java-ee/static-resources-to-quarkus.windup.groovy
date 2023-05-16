package quarkus.javaee

import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.operation.iteration.AbstractIterationOperation
import org.jboss.windup.config.query.Query
import org.jboss.windup.config.query.QueryPropertyComparisonType
import org.jboss.windup.graph.model.FileLocationModel
import org.jboss.windup.graph.model.resource.FileModel
import org.jboss.windup.graph.service.FileLocationService
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.ocpsoft.rewrite.context.EvaluationContext

final IssueCategory mandatoryIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY)
final Link guideLink = Link.to("Quarkus - HTTP Reference: Serving Static Resources", "https://quarkus.io/guides/http-reference#serving-static-resources")

ruleSet("static-resources-to-quarkus-groovy")
    .addSourceTechnology(new TechnologyReference("java-ee", null))
    .addTargetTechnology(new TechnologyReference("quarkus", null))
    .addRule()
    .when(
        Query.fromType(FileModel.class)
            .withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.CONTAINS_TOKEN, "webapp")
            .withProperty(FileModel.IS_DIRECTORY, false)
    )
    .perform(
        new AbstractIterationOperation<FileModel>() {
            void perform(GraphRewrite event, EvaluationContext context, FileModel payload) {
                if (payload.getPrettyPath().contains("/WEB-INF/") || payload.getPrettyPath().contains("/META-INF/")) {
                    return
                }
                final FileLocationService fileLocationService = new FileLocationService(event.getGraphContext())
                final FileLocationModel folderLocationModel = fileLocationService.create()
                folderLocationModel.setFile(payload)
                folderLocationModel.setColumnNumber(1)
                folderLocationModel.setLineNumber(1)
                folderLocationModel.setLength(1)
                folderLocationModel.setSourceSnippit("File Match")
                ((Hint) Hint.titled("Static resources")
                    .withText("Static file found within the `webapp` folder path must be moved into the `src/main/resources/META-INF/resources` folder.")
                    .withIssueCategory(mandatoryIssueCategory)
                    .with(guideLink)
                    .withEffort(1)
                ).performParameterized(event, context, folderLocationModel)
            }
        }
    )
    .withId("static-resources-to-quarkus-groovy-00000")
