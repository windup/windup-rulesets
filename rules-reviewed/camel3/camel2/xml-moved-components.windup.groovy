package camel3.camel2

import org.apache.commons.lang3.StringUtils
import org.jboss.windup.ast.java.data.TypeReferenceLocation
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
import org.jboss.windup.rules.apps.java.model.JavaClassModel
import org.jboss.windup.rules.apps.xml.condition.XmlFile
import org.jboss.windup.rules.apps.xml.model.XmlFileModel
import org.jboss.windup.rules.apps.xml.model.XmlTypeReferenceModel
import org.jboss.windup.rules.files.condition.FileContent
import org.ocpsoft.rewrite.config.And
import org.ocpsoft.rewrite.config.Condition
import org.ocpsoft.rewrite.context.EvaluationContext

import java.util.function.BiFunction
import java.util.function.Function

final String FROM_XML_FILES_IN_PROJECT = "xmlFilesInProject"
final String FROM_FILES_IN_PROJECT = "filesInProject"

/*
returns xpath condition that counts elements
 */
 String countXpath(String... elements) {
    StringBuilder sb = new StringBuilder("//*[( ")
    for (i = 0; i < elements.length; i++) {
        sb.append("count(c:" + elements[i] + ")")
        if (i < (elements.length - 1)) {
            sb.append(" + ")
        }
    }
    sb.append(")> 0]")
    return sb.toString()
}

static String componentNameExpression(String componentName) {
    return "(\"$componentName:"
}

static String componentNameXpath(String component) {
    return "//*/c:route/*[starts-with(@uri, '$component:')]"
}

final Map<String, String[]> movedComponents = [
        // component name: [xml xpath, java matches expression ]
        "language"     : [componentNameXpath("language"), componentNameExpression("language")],
        "bean"         : [componentNameXpath("bean"), componentNameExpression("bean")],
        "direct"       : [componentNameXpath("direct"), componentNameExpression("direct")],
        "xslt"         : [componentNameXpath("xslt"), componentNameExpression("xslt")],
        "browse"       : [componentNameXpath("browse"), componentNameExpression("browse")],
        "dataset"      : [componentNameXpath("dataset"), componentNameExpression("dataset")],
        "direct-vm"    : [componentNameXpath("direct-vm"), componentNameExpression("direct-vm")],
        "file"         : [componentNameXpath("file"), componentNameExpression("file")],
        "log"          : [componentNameXpath("log"), componentNameExpression("log")],
        "mock"         : [componentNameXpath("mock"), componentNameExpression("mock")],
        "ref"          : [componentNameXpath("ref"), componentNameExpression("ref")],
        "saga"         : [componentNameXpath("saga"), componentNameExpression("saga")],
        "scheduler"    : [componentNameXpath("scheduler"), componentNameExpression("scheduler")],
        "seda"         : [componentNameXpath("seda"), componentNameExpression("seda")],
        "stub"         : [componentNameXpath("stub"), componentNameExpression("stub")],
        "timer"        : [componentNameXpath("timer"), componentNameExpression("timer")],
        "validator"    : [componentNameXpath("validator"), componentNameExpression("validator")],
        "vm"           : [componentNameXpath("vm"), componentNameExpression("vm")],
        "rest"         : [countXpath("rest", "get", "post", "put", "delete"), "rest({*})"],
        "xslt "         : [countXpath("xpath"), ".xpath({*})"],
        "zip-deflater" : [countXpath("zip"), ".zip({*})"],
        "zip-deflater ": [countXpath("gzip"), ".gzip({*})"],
        "dataformat "  : [countXpath("marshal,unmarshal,dataformats"), ".marshal({*})"],
        "dataformat"   : ["", ".unmarshal({*})"],
        "attachements ": ["", ".addAttachement({*})"]
]

final IssueCategory optionalIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.OPTIONAL)
final Link modularizationLink = Link.to("Camel 3 - Migration Guide: Modularization of camel-core", "https://camel.apache.org/manual/latest/camel-3-migration-guide.html#_modularization_of_camel_core")


final BiFunction<String, String, Boolean> isThereArtifactId = { String xmlDependenciesBlock, String component ->
    xmlDependenciesBlock.contains("<artifactId>camel-$component</artifactId>" as CharSequence)
}

final BiFunction<String, String, Condition> xmlCondition = { String component, String namespace ->
    XmlFile.from(FROM_XML_FILES_IN_PROJECT)
            .matchesXpath("//*/c:route/*[starts-with(@uri, '$component:')]")
            .namespace("c", namespace)
            .as("$component-$namespace")
}

final BiFunction<String, String, Condition> xmlConditionWithXpath = { String xpath, String namespace ->
    XmlFile.from(FROM_XML_FILES_IN_PROJECT)
            .matchesXpath(xpath)
            .namespace("c", namespace)
}

final Function<String, Condition> javaConditionMatches = { String matches ->
    FileContent.from(FROM_FILES_IN_PROJECT)
            .matches(matches)
            .inFileNamed("{*}.java")
}

ruleSet("xml-moved-components-groovy")
        .addSourceTechnology(new TechnologyReference("camel", "[2,3)"))
        .addTargetTechnology(new TechnologyReference("camel", "[3,)"))
        .addRule()
        .when(
                XmlFile.matchesXpath("/m:project/m:dependencies[m:dependency/m:groupId/text() = 'org.apache.camel']")
                        .inFile("pom.xml").namespace("m", "http://maven.apache.org/POM/4.0.0")
        )
        .perform(new AbstractIterationOperation<XmlTypeReferenceModel>() {
            void perform(GraphRewrite event, EvaluationContext context, XmlTypeReferenceModel payload) {
                Set<String> usedComponents = []
                final FileModel fileModel = payload.getFile()
                final String filePath = StringUtils.removeEnd(fileModel.getFilePath(), fileModel.getFileName())
                Query.fromType(XmlFileModel.class).withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.CONTAINS_TOKEN, filePath).as(FROM_XML_FILES_IN_PROJECT).evaluate(event, context)
                Query.fromType(FileModel.class).withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.CONTAINS_TOKEN, filePath).as(FROM_FILES_IN_PROJECT).evaluate(event, context)
                final String xmlDependenciesBlock = payload.getSourceSnippit()
                // rules xml-moved-components-0000{0-2}
                movedComponents.keySet().stream()
                        .filter { component -> !isThereArtifactId.apply(xmlDependenciesBlock, component.replaceAll("\\s","")) }
                        .filter { component ->
                            (movedComponents[component][0] != "" ? xmlCondition.apply(movedComponents[component][0], "http://camel.apache.org/schema/spring").evaluate(event, context) : false) ||
                                    (movedComponents[component][0] != "" ? xmlConditionWithXpath.apply(movedComponents[component][0],
                                            "http://camel.apache.org/schema/blueprint").evaluate(event, context) : false) ||
                                    (movedComponents[component][1] != "" ? javaConditionMatches.apply(movedComponents[component][1]).evaluate(event, context) : false)
                        }
                        .each { component -> usedComponents.add(component.replaceAll("\\s","")) }

                String components = usedComponents.stream()
                        .map { component -> "`camel-$component`" }.toArray().toString()

                ((Hint) Hint.titled("Modularization of camel-core")
                        .withText(" $components components were moved out of `camel-core` to separate artifacts. Maven users of Apache Camel can keep using the dependency `camel-core`" +
                                "which has transitive dependencies on all of its modules, except for `camel-main`" +
                                "and therefore no migration is needed. However, users who want to trim the size of the classes on the classpath," +
                                "can use fine grained Maven dependencies on only the modules needed.")
                        .withIssueCategory(optionalIssueCategory)
                        .with(modularizationLink)
                        .withEffort(1))
                        .perform(event, context, payload)

            }
        })
        .withId("xml-moved-components-groovy-00000")
