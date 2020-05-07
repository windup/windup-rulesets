package camel3.camel2

import jdk.internal.org.objectweb.asm.TypeReference
import org.apache.commons.lang3.StringUtils
import org.jboss.windup.ast.java.data.TypeReferenceLocation
import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.operation.Iteration
import org.jboss.windup.config.operation.iteration.AbstractIterationOperation
import org.jboss.windup.config.query.Query
import org.jboss.windup.config.query.QueryPropertyComparisonType
import org.jboss.windup.graph.model.resource.FileModel
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.HintEffort
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.reporting.model.QuickfixType
import org.jboss.windup.reporting.quickfix.Quickfix
import org.jboss.windup.rules.apps.java.condition.JavaClass
import org.jboss.windup.rules.apps.java.model.JavaClassFileModel
import org.jboss.windup.rules.apps.java.model.JavaSourceFileModel
import org.jboss.windup.rules.apps.java.scan.ast.JavaTypeReferenceModel
import org.jboss.windup.rules.apps.xml.condition.XmlFile
import org.jboss.windup.rules.apps.xml.model.XmlFileModel
import org.jboss.windup.rules.apps.xml.model.XmlTypeReferenceModel
import org.jboss.windup.rules.files.condition.FileContent
import org.ocpsoft.rewrite.config.And
import org.ocpsoft.rewrite.config.Condition
import org.ocpsoft.rewrite.config.Not
import org.ocpsoft.rewrite.config.Or
import org.ocpsoft.rewrite.context.EvaluationContext

final String telegramRegex = "(telegram:)(?!.*authorizationToken.*)"
final String regexReference = "{regex}"

final IssueCategory optionalIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.OPTIONAL)
final IssueCategory mandatoryIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY)

Hint createHint(String title, String messase, String linkAppendix, boolean mandatory) {
    final String docTitleAppendix = (linkAppendix.substring(0, 1).toUpperCase() + linkAppendix.substring(1))
            .replaceAll("_", " ")

    HintEffort hint = Hint.titled(title)
            .withText(messase)
            .with(Link.to("Camel 3 - Migration Guide: $docTitleAppendix", "https://camel.apache.org/manual/latest/camel-3-migration-guide.html#_$linkAppendix"))
            .withEffort(1)
    if (mandatory) {
        hint.withIssueCategory(new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY));
    }
    return (Hint) hint
}

Hint createHint(String title, String messase, String linkAppendix, boolean mandatory, Quickfix quickfix) {
    return createHint(title, messase, linkAppendix, mandatory).withQuickfix(quickfix);
}

boolean has(String input) {
    if (input.contains("="))
        if (input.substring(input.indexOf("("), input.lastIndexOf(")")).split(",").length == 1)
            return true
    return false
}

Hint createMovedClassHint(String moved, String movedTo, String addInfo, IssueCategory issueCategory) {
    Quickfix q = new Quickfix()
    q.setType(QuickfixType.REPLACE)
    q.setSearchStr(moved)
    q.setReplacementStr(movedTo)

    return createHint("$moved was moved", "`$moved` was moved to `$movedTo` ", "eips", true, q)
}

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
                        .perform(createHint("[camel-telegram] Authorization token parameter required!",
                                "Authorization token was moved from uri-path to a query parameter. " +
                                        "Use `telegram:bots?authorizationToken=myTokenHere`",
                                "telegram", true)
                        ))
        .where("regex").matches(telegramRegex)
        .withId("component-changes-00001")

        .addRule()
        .when(JavaClass.references("org.apache.camel.component.shiro.security.ShiroSecurityPolicy{*}"))
        .perform(new AbstractIterationOperation<JavaTypeReferenceModel>() {
            void perform(GraphRewrite event, EvaluationContext context, JavaTypeReferenceModel payload) {

                Hint h = createHint("Shiro - default encryption key was removed",
                        "It's mandatory to specify key/passphrase for `ShiroSecurityPolicy`.", "shiro_component", true)

                final String input = payload.sourceSnippit
                if (input.contains("=")
                        && (input.substring(input.indexOf("("), input.lastIndexOf(")")).split(",").length == 1)) {
                    h.perform(event, context, payload)
                }
            }
        })
        .withId("component-changes-00002")
