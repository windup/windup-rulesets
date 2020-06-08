package camel3.camel2

import org.apache.ivy.util.cli.OptionBuilder
import org.jboss.windup.ast.java.data.TypeReferenceLocation
import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.operation.Iteration

import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.reporting.model.QuickfixType
import org.jboss.windup.reporting.quickfix.Quickfix
import org.jboss.windup.rules.apps.java.condition.JavaClass
import org.jboss.windup.rules.apps.xml.condition.XmlFile
import org.jboss.windup.rules.files.condition.FileContent
import org.jboss.windup.rules.apps.xml.model.XmlTypeReferenceModel

import org.ocpsoft.rewrite.config.Condition
import org.ocpsoft.rewrite.config.ConditionBuilder
import org.ocpsoft.rewrite.config.Or

final String telegramRegex = "(telegram:)(?!.*authorizationToken.*)"
final String regexReference = "{regex}"


ruleSet("component-changes-groovy")
        .addSourceTechnology(new TechnologyReference("camel", "[2,3)"))
        .addTargetTechnology(new TechnologyReference("camel", "[3,)"))
        .addRule()
        .when(Or.any(
                FileContent
                        .matches("$regexReference")
                        .inFileNamed("{*}.java"),
                XmlFile.matchesXpath("//*/c:route/*[contains(@uri ,'telegram') and not(contains(@uri,'authorizationToken'))]")
                        .namespace("c", "http://camel.apache.org/schema/spring")
        ))
        .perform(
                Iteration.over()
                        .perform((Hint) Hint.titled("[camel-telegram] Authorization token parameter required!")
                                .withText("Authorization token was moved from uri-path to a query parameter. " +
                                        "Use `telegram:bots?authorizationToken=myTokenHere`")
                                .with(Link.to("Camel migration guide", "https://camel.apache.org/manual/latest/camel-3-migration-guide.html#_telegram"))
                                .withEffort(1)))
        .where("regex").matches(telegramRegex)
        .withId("component-changes-00001")
