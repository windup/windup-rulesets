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
import org.jboss.windup.rules.apps.java.condition.JavaClass
import org.jboss.windup.rules.apps.xml.condition.XmlFile
import org.jboss.windup.rules.apps.xml.model.XmlFileModel
import org.jboss.windup.rules.apps.xml.model.XmlTypeReferenceModel
import org.ocpsoft.rewrite.config.Condition
import org.ocpsoft.rewrite.context.EvaluationContext

import java.util.function.BiFunction
import java.util.function.Function

final String FROM_XML_FILES_IN_PROJECT = "xmlFilesInProject"
final String FROM_FILES_IN_PROJECT = "filesInProject"
final Set<String> componentsMoved = ["language", "bean", "direct", "xslt", "browse", "dataset", "direct-vm", "file", "log", "mock", "ref", "saga", "scheduler", "seda", "stub", "timer", "validator", "vm"]

final IssueCategory mandatoryIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY)
final Link modularizationLink = Link.to("Camel 3 - Migration Guide: Modularization of camel-core", "https://camel.apache.org/manual/latest/camel-3-migration-guide.html#_modularization_of_camel_core")

final BiFunction<String, String, Boolean> isThereArtifactId = { String xmlDependenciesBlock, String component -> 
    xmlDependenciesBlock.contains("<artifactId>camel-$component</artifactId>" as CharSequence) }

final Function<String, Condition> springCondition = { String component -> 
    XmlFile.from(FROM_XML_FILES_IN_PROJECT)
            .matchesXpath("//*/c:route/*/@uri[windup:matches(self::node(), '" + component + ":{*}')]")
            .namespace("c", "http://camel.apache.org/schema/spring")
            .as("$component-spring")
}

final Function<String, Condition> blueprintCondition = { String component ->
    XmlFile.from(FROM_XML_FILES_IN_PROJECT)
            .matchesXpath("//*/b:route/*/@uri[windup:matches(self::node(), '" + component + ":{*}')]")
            .namespace("b", "http://camel.apache.org/schema/blueprint")
            .as("$component-blueprint")
}

final Function<String, Condition> javaCondition = { String component ->
    JavaClass.from(FROM_FILES_IN_PROJECT)
        .references("(\"$component:")
        .as("$component-java")
}

ruleSet("xml-moved-components-groovy")
    .addSourceTechnology(new TechnologyReference("camel", "[2,3)"))
    .addTargetTechnology(new TechnologyReference("camel", "[3,)"))
    .addRule()
    .when(
        XmlFile.matchesXpath("/m:project/m:dependencies[m:dependency/m:groupId/text() = 'org.apache.camel']")
            .inFile("pom.xml").namespace("m", "http://maven.apache.org/POM/4.0.0")
    )
    .perform(new AbstractIterationOperation<XmlTypeReferenceModel>()
    {
        void perform(GraphRewrite event, EvaluationContext context, XmlTypeReferenceModel payload) {
            final FileModel fileModel = payload.getFile()
            final String filePath = StringUtils.removeEnd(fileModel.getFilePath(), fileModel.getFileName())
            Query.fromType(XmlFileModel.class).withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.CONTAINS_TOKEN, filePath).as(FROM_XML_FILES_IN_PROJECT).evaluate(event, context)
            Query.fromType(FileModel.class).withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.CONTAINS_TOKEN, filePath).as(FROM_FILES_IN_PROJECT).evaluate(event, context)
            final String xmlDependenciesBlock = payload.getSourceSnippit()
            // rules xml-moved-components-0000{0-2}
            componentsMoved.stream()
                .filter { component -> !isThereArtifactId.apply(xmlDependenciesBlock, component) }
                .filter { component ->
                    springCondition.apply(component).evaluate(event, context) ||
                    blueprintCondition.apply(component).evaluate(event, context) ||
                    javaCondition.apply(component).evaluate(event, context)}
                .each { component ->
                    ((Hint) Hint.titled("`camel-$component` has been moved")
                        .withText("""`camel-$component` component has been moved from `camel-core` 
                            to separate artifact `org.apache.camel:camel-$component` that has to be added as a 
                            dependency to your project `pom.xml` file""")
                        .withIssueCategory(mandatoryIssueCategory)
                        .with(modularizationLink)
                        .withEffort(1))
                        .perform(event, context, payload)
                }

        }
    })
    .withId("xml-moved-components-00000")
