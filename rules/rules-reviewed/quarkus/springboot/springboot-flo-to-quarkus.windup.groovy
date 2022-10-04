package quarkus.springboot

import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.rules.files.condition.FileContent

final IssueCategory potentialIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.POTENTIAL)
final Link guideLink = Link.to("Quarkus - Guides", "https://quarkus.io/guides/")

ruleSet("springboot-flo-to-quarkus-groovy")
        .addSourceTechnology(new TechnologyReference("springboot", null))
        .addTargetTechnology(new TechnologyReference("quarkus", null))
        .addRule()
        .when(
            FileContent.matches("{prefixes}flo-editor").inFileNamed("{*}.{extensions}")
        )
        .perform(
            ((Hint) Hint.titled("Spring Flo is not supported by Quarkus")
            .withText("""
                            Spring Flo Javascript library has been detected.  
                            This is used in conjunction with Spring Cloud Data Flow which is not supported in Quarkus  
                            The link below will take to Quarkus Guides where you can check if there are alternatives for your use case.
                            """)
            .withIssueCategory(potentialIssueCategory)
            .with(guideLink)
            .withEffort(13))
        )
        .where("prefixes").matches("(<|')")
        .where("extensions").matches("(html|ts)")
        .withId("springboot-flo-to-quarkus-groovy-00000")
