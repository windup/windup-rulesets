package quarkus.springboot

import org.jboss.windup.ast.java.data.TypeReferenceLocation
import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.operation.Iteration
import org.jboss.windup.config.operation.iteration.AbstractIterationOperation
import org.jboss.windup.config.query.Query
import org.jboss.windup.graph.model.resource.FileModel
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.rules.apps.java.condition.JavaClass
import org.jboss.windup.rules.apps.java.scan.ast.JavaTypeReferenceModel
import org.jboss.windup.rules.apps.xml.condition.XmlFile
import org.jboss.windup.rules.apps.xml.model.XmlFileModel
import org.ocpsoft.rewrite.config.And
import org.ocpsoft.rewrite.context.EvaluationContext

final IssueCategory mandatoryIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY)
final Link guideLink = Link.to("Quarkus - Accessing application properties with Spring Boot properties API", "https://quarkus.io/guides/spring-boot-properties")

ruleSet("springboot-properties-to-quarkus-groovy")
        .addSourceTechnology(new TechnologyReference("springboot", null))
        .addTargetTechnology(new TechnologyReference("quarkus", null))
        .addRule()
        .when(JavaClass.references("org.springframework.boot.context.properties.ConfigurationProperties").at(TypeReferenceLocation.ANNOTATION))
        .perform(new AbstractIterationOperation<JavaTypeReferenceModel>() {
            void perform(GraphRewrite event, EvaluationContext context, JavaTypeReferenceModel payload) {
                final FileModel rootFileModel = payload.getFile().getProjectModel().getRootFileModel()
                final String projectFullFilePath = rootFileModel.getFilePath()
                final String projectPrettyFilePath = rootFileModel.getPrettyPath()
                final String pomFileIdentifier = "pom-files-" + projectPrettyFilePath
                final String dependenciesIdentifier = "dependencies-" + projectPrettyFilePath
                boolean pomXmlFileFound = And.all(
                        /* search for the pom.xml file of the project the payload belongs to */
                        Query.fromType(XmlFileModel.class)
                                .withProperty(FileModel.FILE_PATH, projectFullFilePath + "/pom.xml")
                                .as(pomFileIdentifier),
                        /* AND check if the pom.xml found hasn't the 'quarkus-spring-boot-properties' dependency */
                        XmlFile.from(pomFileIdentifier)
                                .matchesXpath("//*[m:dependency and not(m:dependency/m:artifactId/text() = 'quarkus-spring-boot-properties') and not(m:dependency/m:groupId/text() = 'io.quarkus')]")
                                .namespace("m", "http://maven.apache.org/POM/4.0.0")
                                .as(dependenciesIdentifier)
                ).evaluate(event, context)
                if (pomXmlFileFound) {
                    Iteration.over(dependenciesIdentifier)
                            .perform(((Hint) Hint.titled("Add Quarkus dependency `io.quarkus:quarkus-spring-boot-properties`")
                                    .withText("""Please add the Quarkus dependency `io.quarkus:quarkus-spring-boot-properties` to successfully use the Spring Configuration Properties in your application""")
                                    .withIssueCategory(mandatoryIssueCategory)
                                    .with(guideLink)
                                    .withEffort(1)))
                            .endIteration()
                        .perform(event, context)
                }
            }
        })
        .withId("springboot-properties-to-quarkus-groovy-00000")
