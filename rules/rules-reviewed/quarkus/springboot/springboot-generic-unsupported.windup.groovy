package quarkus.springboot

import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.operation.Iteration
import org.jboss.windup.project.condition.Artifact
import org.jboss.windup.project.condition.Project
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.rules.apps.java.condition.SourceMode
import org.ocpsoft.rewrite.config.And

final IssueCategory mandatoryIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY)
final Link guideLink = Link.to("Quarkus - Guides", "https://quarkus.io/guides/")

ruleSet("springboot-generic-unsupported-groovy")
        .addSourceTechnology(new TechnologyReference("springboot", null))
        .addTargetTechnology(new TechnologyReference("quarkus", null))
        .addRule()
        .when(Project.dependsOnArtifact(Artifact.withGroupId("{group}").andArtifactId("{artifact}")).as("dependency"))
        .perform(
                Iteration.over("dependency")
                    .perform(
                        ((Hint) Hint.titled("Spring component {artifact} requires investigation")
                        .withText("""
                        Spring `{group}:{artifact}` component is not supported by Quarkus.  
                        Code relying on this dependency cannot currently be run in Quarkus.  
                        Consider rewrite the code that relies on this dependency leveraging the Quarkus Guides linked below.
                        """)
                        .withIssueCategory(mandatoryIssueCategory)
                        .with(guideLink)
                        .withEffort(13))
                    )
                .endIteration()
        )
        .where("group").matches("(org.spring)(.*)?")
        .where("artifact").matches("spring-aop|spring-boot-starter-aop|spring-batch-core|spring-boot-starter-batch|spring-cloud-dataflow-core|spring-cloud-skipper|spring-integration-core|spring-boot-starter-integration|spring-ldap-core|spring-boot-starter-data-ldap|spring-mobile-device|spring-boot-starter-mobile|org.springframework.roo.bootstrap|spring-statemachine-core|spring-test-htmlunit|spring-webflow")
        .withId("springboot-generic-unsupported-groovy-00000")
