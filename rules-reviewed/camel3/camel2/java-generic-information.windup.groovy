package camel3.camel2

import org.jboss.windup.ast.java.data.TypeReferenceLocation
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.operation.Iteration
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryModel
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.HintEffort
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.reporting.model.QuickfixType
import org.jboss.windup.reporting.quickfix.Quickfix
import org.jboss.windup.rules.apps.java.condition.JavaClass
import org.ocpsoft.rewrite.config.Condition
import org.ocpsoft.rewrite.config.Or


final String MOVED_FROM = "org.apache.camel.ThreadPoolRejectedPolicy"
final String MOVED_TO = "org.apache.camel.util.concurrent.ThreadPoolRejectedPolicy"
final IssueCategory optionalIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.OPTIONAL)
final IssueCategory mandatoryIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY)

Hint createHint(String title, String messase, String linkAppendix, boolean mandatory) {
    final String docTitleAppendix = (linkAppendix.substring(0, 1).toUpperCase() + linkAppendix.substring(1))
            .replaceAll("_", " ")

    HintEffort hint = Hint.titled(title)
            .withText(messase).with(Link.to("Camel 3 - Migration Guide: $docTitleAppendix", "https://camel.apache.org/manual/latest/camel-3-migration-guide.html#_$linkAppendix")).withEffort(1)
    if (mandatory) {
        hint.withIssueCategory(new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY));
    }
    return (Hint) hint
}

Hint createHint(String title, String messase, String linkAppendix, boolean mandatory, Quickfix quickfix) {
    return createHint(title, messase, linkAppendix, mandatory).withQuickfix(quickfix);
}

Hint createMovedClassHint(String moved, String movedTo, String addInfo, IssueCategory issueCategory) {
    Quickfix q = new Quickfix()
    q.setType(QuickfixType.REPLACE)
    q.setSearchStr(moved)
    q.setReplacementStr(movedTo)

    return createHint("$moved was moved", "`$moved` was moved to `$movedTo` ", "eips", true, q)
}

Condition javaMethodCallCondition(String regex) {
    return JavaClass.references("org.apache.camel.$regex").at(TypeReferenceLocation.METHOD_CALL);
}


ruleSet("java-generic-information-groovy")
        .addSourceTechnology(new TechnologyReference("camel", "[2,3)"))
        .addTargetTechnology(new TechnologyReference("camel", "[3,)"))

        .addRule()
        .when(JavaClass.references(MOVED_FROM).at(TypeReferenceLocation.IMPORT).as("movedClass"))
        .perform(
                Iteration.over("movedClass")
                        .perform(createMovedClassHint(MOVED_FROM, MOVED_TO, "class", mandatoryIssueCategory))
        )
        .withId("java-generic-information-00034")

        .addRule()
        .when(javaMethodCallCondition("impl.SimpleRegistry.put({*})"))
        .perform(createHint("SimpleRegistry was moved",
                "`The org.apache.camel.support.DefaultRegistry` should be favoured instead od `SimpleRegistry`." +
                        "Also `bind` operation should be used instead of `put` to add entries to the `SimpleRegistry` or `DefaultRegistry`.",
                "generic_information", false)
        )
        .withId("java-generic-information-00035")

        .addRule()
        .when(javaMethodCallCondition("Exchange.{get|has}Out()"))
        .perform(Iteration.over()
                .perform(createHint("`getOut`/`hasOut` are deprecated",
                        "Methods `getOut`, `hasOut` on `Exchange` has been deprecated in favour of using `getMessage` instead.",
                        "getout_on_message", false))
        )
        .withId("java-generic-information-00036")

        .addRule()
        .when(Or.any(
                javaMethodCallCondition("Message.{is|set}Fault({*})"),
                javaMethodCallCondition("CamelContext.{is|set}HandleFault({*})")
        ))
        .perform(Iteration.over()
                .perform(createHint("Fault API on Message was removed",
                        "Fault API was removed from `org.apache.camel.Message`. The option `handleFault` has also been removed" +
                                " and you now need to turn this on as endpoint or component option on `camel-cxf` or `camel-spring-ws`.",
                        "fault_api_on_message", true)
                ))
        .withId("java-generic-information-00037")

        .addRule()
        .when(Or.any(
                javaMethodCallCondition("CamelContext.{start|stop|suspend|resume}Route({*})"),
                javaMethodCallCondition("CamelContext.startAllRoutes()"),
                javaMethodCallCondition("CamelContext.isStartingRoutes()"),
                javaMethodCallCondition("CamelContext.getRouteStatus({*})")
        ))
        .perform(Iteration.over()
                .perform(createHint("Route control methods were moved",
                        "Methods for controlling routes were moved from `CamelContext` to the `RouteController`." +
                                "To call moved method use: `context.getRouteController().startRoute(\"myRoute\")`",
                        "controlling_routes", true)
                ))
        .withId("java-generic-information-00038")
