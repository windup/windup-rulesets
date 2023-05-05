package quarkus.javaee

import org.jboss.windup.ast.java.data.TypeReferenceLocation
import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.Variables
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.operation.Iteration
import org.jboss.windup.config.operation.iteration.AbstractIterationOperation
import org.jboss.windup.graph.model.FileLocationModel
import org.jboss.windup.graph.model.FileReferenceModel
import org.jboss.windup.graph.model.ProjectModel
import org.jboss.windup.graph.model.WindupVertexFrame
import org.jboss.windup.graph.model.resource.FileModel
import org.jboss.windup.project.condition.Artifact
import org.jboss.windup.project.condition.Project
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.rules.apps.java.condition.JavaClass
import org.ocpsoft.rewrite.config.And
import org.ocpsoft.rewrite.context.EvaluationContext

import java.util.stream.Collectors
import java.util.stream.StreamSupport

final IssueCategory mandatoryIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY)
final Link guideLink = Link.to("Quarkus - Guides", "https://quarkus.io/guides/resteasy")

static boolean matchesProject(GraphRewrite event, FileLocationModel payload) {
    final Iterable<? extends WindupVertexFrame> previouslyFound = Optional.ofNullable(Variables.instance(event).findVariable("discard")).orElse(Set.of())
    final Set<ProjectModel> projectModels = StreamSupport.stream(previouslyFound.spliterator(), false)
        .map {
            if (it instanceof FileReferenceModel) return ((FileReferenceModel) it).getFile().getProjectModel()
            else if (it instanceof FileModel) return ((FileModel) it).getProjectModel()
            else return null
        }
        .collect (Collectors.toSet())
    final boolean matchesProject = projectModels.isEmpty() || projectModels.stream().anyMatch{payload.getFile().belongsToProject(it)}
    return matchesProject
}

ruleSet("javaee-api-to-quarkus-groovy")
    .addSourceTechnology(new TechnologyReference("java-ee", null))
    .addTargetTechnology(new TechnologyReference("quarkus", null))
    .addRule()
    .when(
        And.all(
            JavaClass.references("javax.ws.rs.{*}").at(TypeReferenceLocation.IMPORT).as("discard"),
            Project.dependsOnArtifact(Artifact.withGroupId("javax").andArtifactId("javaee-api")).as("dependency")
        )
    )
    .perform(
        Iteration.over("dependency").perform(
            new AbstractIterationOperation<FileLocationModel>() {
                void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
                    if (matchesProject(event, payload)) {
                        ((Hint) Hint.titled("Replace JAX-RS dependency")
                            .withText("""
                            At least one Java class importing from the `javax.ws.rs` package has been found so the dependency `javax:javaee-api` has to be replaced with `io.quarkus:quarkus-resteasy` artifact.  
                            """)
                            .withIssueCategory(mandatoryIssueCategory)
                            .with(guideLink)
                            .withEffort(1)
                        ).performParameterized(event, context, payload)
                    }
                }
            }
        )
        .endIteration()
    )
    .withId("javaee-api-to-quarkus-groovy-00000")
