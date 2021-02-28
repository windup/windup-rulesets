import jdk.internal.org.objectweb.asm.TypeReference
import org.apache.commons.lang3.StringUtils
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.rules.files.condition.FileContent


final IssueCategory potentialIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.POTENTIAL)
final Link guideLink = Link.to("Quarkus - Migration Guide 1.12", "https://github.com/quarkusio/quarkus/wiki/Migration-Guide-1.12#quarkus-maven-plugin")

ruleSet("quarkus1-12-maven-plugin-groovy")
        .addSourceTechnology(new TechnologyReference("quarkus1", versionRange="(,11]"))
        .addTargetTechnology(new TechnologyReference("quarkus1", versionRange="[12,)"))
        .addRule()
        .when(
            FileContent.matches("quarkus-maven-plugin").inFileNamed("pom.xml")
        )
        .perform(
            ((Hint) Hint.titled("Quarkus Maven Plugin")
            .withText('''We cleaned up a few things in the Quarkus Maven Plugin.
            Make sure the quarkus-maven-plugin section of the pom.xml of your project looks like:

            <plugin>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-maven-plugin</artifactId>
            <version>\${quarkus-plugin.version}</version>
            <extensions>true</extensions>
             <executions>
               <execution>
                 <goals>
                  <goal>build</goal>
                  <goal>generate-code</goal> 
                  <goal>generate-code-tests</goal>
                 </goals>
               </execution>
             </executions>
            </plugin>
            ''')
            .withIssueCategory(potentialIssueCategory)
            .with(guideLink)
            .withEffort(1))
        )
        .withId("quarkus1-12-maven-plugin-groovy-00000")
