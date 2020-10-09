package quarkus.springboot

import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.operation.Iteration
import org.jboss.windup.config.phase.PostMigrationRulesPhase
import org.jboss.windup.project.condition.Artifact
import org.jboss.windup.project.condition.Project
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.HasHint
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.rules.apps.java.condition.SourceMode
import org.ocpsoft.rewrite.config.And
import org.ocpsoft.rewrite.config.Not

final IssueCategory potentialIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.POTENTIAL)
final Link guideLink = Link.to("Quarkus - Guides", "https://quarkus.io/guides/")

ruleSet("springboot-generic-catchall-groovy")
        .addSourceTechnology(new TechnologyReference("springboot", null))
        .addTargetTechnology(new TechnologyReference("quarkus", null))
        .setPhase(PostMigrationRulesPhase.class)
        .addRule()
        .when(And.all(
                SourceMode.isEnabled(), 
                Project.dependsOnArtifact(Artifact.withGroupId("{group}").andArtifactId("{artifact}")).as("dependency"))
        )
        .perform(
            Iteration.over("dependency")
                .when(Not.any(new HasHint()))
                .perform(((Hint) Hint.titled("Spring component {artifact} requires investigation")
                        .withText("""
                                Check if there is a Quarkus Extensions with Spring API compatibility for the Spring {artifact} component.  
                                If yes, then replace the `{group}:{artifact}` dependency with the Quarkus extension.  
                                If no, consider rewrite the code that relies on this dependency leveraging the Quarkus Guides linked below.
                                """)
                        .withIssueCategory(potentialIssueCategory)
                        .with(guideLink)
                        .withEffort(5)))
            .endIteration()
        )
        .where("group").matches("(org.spring)(.*)?")
        .where("artifact").matches(".*")
        .withId("springboot-generic-catchall-groovy-00000")
