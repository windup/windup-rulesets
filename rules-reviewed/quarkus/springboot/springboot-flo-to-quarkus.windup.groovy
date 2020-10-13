package quarkus.springboot

import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.operation.Iteration
import org.jboss.windup.config.phase.PostMigrationRulesPhase
import org.jboss.windup.project.condition.Artifact
import org.jboss.windup.project.condition.Project
import org.jboss.windup.rules.files.condition.File
import org.jboss.windup.rules.files.condition.FileContent
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.HasHint
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.rules.apps.java.condition.SourceMode
import org.jboss.windup.rules.files.condition.FileContentFileName
import org.ocpsoft.rewrite.config.And
import org.ocpsoft.rewrite.config.Not

final IssueCategory potentialIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.POTENTIAL)
final Link guideLink = Link.to("Quarkus - Guides", "https://quarkus.io/guides/")

ruleSet("springboot-flo-to-quarkus-groovy")
        .addSourceTechnology(new TechnologyReference("springboot", null))
        .addTargetTechnology(new TechnologyReference("quarkus", null))
        .setPhase(PostMigrationRulesPhase.class)
        .addRule()
        .when(
            //FileContent.matches("{.*}{flo-editor}{.*}").andNot(File.inFileNamed("{*}.(java|properties|tag|xml|txt)"))
            FileContent.matches("{.*}").and(File.inFileNamed("springboot-flo-test.component.html"))
        )
        .perform(
        Iteration.over("file")
                .perform(((Hint) Hint.titled("Spring Flo is not supported by Quarkus")
                .withText("""
                                Spring Flo Javascript library has been detected.

                        This is used in conjunction with Spring Cloud Data Flow which is not supported in Quarkus
                                """)
                .withIssueCategory(mandatoryIssueCategory)
                .with(guideLink)
                .withEffort(13)))
                .endIteration()
        )
        .withId("springboot-flo-to-quarkus-groovy-00000")
