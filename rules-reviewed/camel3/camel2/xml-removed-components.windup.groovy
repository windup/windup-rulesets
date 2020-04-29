package camel3.camel2

import org.apache.commons.lang3.StringUtils
import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.operation.iteration.AbstractIterationOperation
import org.jboss.windup.config.query.Query
import org.jboss.windup.config.query.QueryPropertyComparisonType
import org.jboss.windup.graph.model.resource.FileModel
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.rules.apps.xml.condition.XmlFile
import org.jboss.windup.rules.apps.xml.model.XmlFileModel
import org.jboss.windup.rules.apps.xml.model.XmlTypeReferenceModel
import org.jboss.windup.rules.files.condition.FileContent
import org.ocpsoft.rewrite.config.Condition
import org.ocpsoft.rewrite.context.EvaluationContext

import java.util.function.BiFunction
import java.util.function.Function

final String FROM_XML_FILES_IN_PROJECT = "xmlFilesInProject"
final String FROM_FILES_IN_PROJECT = "filesInProject"
final Set<String> componentsAdded = ["aws-cw", "aws-ddb", "aws-ec2", "aws-iam", "aws-kinesis", "aws-kms", "aws-lambda", "aws-mq", "aws-s3", "aws-sdb", "aws-ses", "aws-sns", "aws-sqs", "aws-swf", "aws-xray"]

final IssueCategory mandatoryIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY)
final Link awsLink = Link.to("Camel 3 - Migration Guide: AWS", "https://camel.apache.org/manual/latest/camel-3-migration-guide.html#_aws")

final BiFunction<String, String, Condition> xmlCondition = { String component, String namespace -> 
    XmlFile.from(FROM_XML_FILES_IN_PROJECT)
            .matchesXpath("//*/c:route/*[starts-with(@uri, '$component:')]")
            .namespace("c", namespace)
            .as("$component-$namespace")
}

final Function<String, Condition> javaCondition = { String component ->
    FileContent.from(FROM_FILES_IN_PROJECT)
        .matches("(\"$component:")
        .inFileNamed("{*}.java")
        .as("$component-java")
}

ruleSet("xml-removed-components-groovy")
    .addSourceTechnology(new TechnologyReference("camel", "[2,3)"))
    .addTargetTechnology(new TechnologyReference("camel", "[3,)"))
    .addRule()
    .when(
        XmlFile.matchesXpath("/m:project/m:dependencies[m:dependency/m:groupId/text() = 'org.apache.camel' and m:dependency/m:artifactId/text() = 'camel-aws']")
            .inFile("pom.xml").namespace("m", "http://maven.apache.org/POM/4.0.0")
    )
    .perform(new AbstractIterationOperation<XmlTypeReferenceModel>()
    {
        void perform(GraphRewrite event, EvaluationContext context, XmlTypeReferenceModel payload) {
            final FileModel fileModel = payload.getFile()
            final String filePath = StringUtils.removeEnd(fileModel.getFilePath(), fileModel.getFileName())
            Query.fromType(XmlFileModel.class).withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.CONTAINS_TOKEN, filePath).as(FROM_XML_FILES_IN_PROJECT).evaluate(event, context)
            Query.fromType(FileModel.class).withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.CONTAINS_TOKEN, filePath).as(FROM_FILES_IN_PROJECT).evaluate(event, context)
            componentsAdded.stream()
                .filter { component -> javaCondition.apply(component).evaluate(event, context) ||
                xmlCondition.apply(component, "http://camel.apache.org/schema/spring").evaluate(event, context) ||
                xmlCondition.apply(component, "http://camel.apache.org/schema/blueprint").evaluate(event, context) }
                .each { component ->
                    ((Hint) Hint.titled("`org.apache.camel:camel-aws` artifact has been split up into multiple components (camel-$component)")
                        .withText("""`org.apache.camel:camel-aws` artifact has been removed and split up into multiple components.  
                                So you’ll have to explicitly add the dependencies for these components.  
                                Please add `org.apache.camel:camel-$component` to your `pom.xml` file.  
                                From the OSGi perspective, there is still a `camel-aws` Karaf feature, which includes all the components features.""")
                        .withIssueCategory(mandatoryIssueCategory)
                        .with(awsLink)
                        .withEffort(1))
                        .perform(event, context, payload)
                }
        }
    })
    .withId("xml-removed-components-groovy-00000")
