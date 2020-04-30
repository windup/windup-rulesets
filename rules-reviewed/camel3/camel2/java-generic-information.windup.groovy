package camel3.camel2

import org.jboss.windup.ast.java.data.TypeReferenceLocation
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.operation.Iteration
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryModel
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.reporting.model.QuickfixType
import org.jboss.windup.reporting.quickfix.Quickfix
import org.jboss.windup.rules.apps.java.condition.JavaClass
import org.ocpsoft.rewrite.config.Or


final String MOVED_FROM = "org.apache.camel.ThreadPoolRejectedPolicy"
final String MOVED_TO = "org.apache.camel.util.concurrent.ThreadPoolRejectedPolicy"
final IssueCategory optionalIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.OPTIONAL)
final IssueCategory mandatoryIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY)

Hint createMovedClassHint(String moved, String movedTo, String addInfo, IssueCategory issueCategory) {
    Quickfix q = new Quickfix()
    q.setType(QuickfixType.REPLACE)
    q.setSearchStr(moved)
    q.setReplacementStr(movedTo)

    return (Hint) ((Hint) Hint.titled("$moved was moved").withText("`$moved` was moved to `$movedTo` ")
            .withIssueCategory(issueCategory)
            .with(Link.to("Camel migration guide", "https://camel.apache.org/manual/latest/camel-3-migration-guide.html#_eips"))).withQuickfix(q)
            .withEffort(1);
}

final String methodCall = "{*}"
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
        .when(
                JavaClass
               //         .references("org.apache.camel.util.toolbox.AggregationStrategies.xslt({*})")
                .references("org.apache.camel.impl.SimpleRegistry.put({*})")
                        .at(TypeReferenceLocation.METHOD_CALL)
        )

        .perform((Hint) Hint.titled("SimpleRegistry was moved").withText("`The org.apache.camel.support.DefaultRegistry` should be favoured instead od `SimpleRegistry`." +
                " Also `bind` operation should be used instead of `put` to add entries to the `SimpleRegistry` or `DefaultRegistry`.")
                .with(Link.to("Camel migration guide", "https://camel.apache.org/manual/latest/camel-3-migration-guide.html#_generic_information")))

        .withId("java-generic-information-00035")

        .addRule()
        .when(JavaClass.references("org.apache.camel.Exchange.{get|has}Out()").at(TypeReferenceLocation.METHOD_CALL))
        .perform(Iteration.over()
                .perform((Hint) Hint.titled("`getOut`/`hasOut` are deprecated").withText("Methods `getOut`, `hasOut` on `Exchange` has been deprecated in favour of using `getMessage` instead.")
                        .with(Link.to("Camel migration guide", "https://camel.apache.org/manual/latest/camel-3-migration-guide.html#_getout_on_message"))
                ))
        .withId("java-generic-information-00036")

        .addRule()
        .when(Or.any(JavaClass.references("org.apache.camel.Message.{is|set}Fault({*})").at(TypeReferenceLocation.METHOD_CALL),
                JavaClass.references("org.apache.camel.Context.{is|set}HandleFault({*})").at(TypeReferenceLocation.METHOD_CALL)))
        .perform(Iteration.over()
                .perform((Hint) Hint.titled("Fault API on Message was removed").withText("Fault API was removed from `org.apache.camel.Message`. " +
                        "The option `handleFault` has also been removed and you now need to turn this on as endpoint or component option on `camel-cxf` or `camel-spring-ws`.")
                        .withIssueCategory(mandatoryIssueCategory)
                        .with(Link.to("Camel migration guide", "https://camel.apache.org/manual/latest/camel-3-migration-guide.html#_fault_api_on_message")).withEffort(1)
                ))Win   .withId("java-generic-information-00037")

        .addRule()
        .when(
                Or.any(
                        JavaClass.references("org.apache.camel.CamelContext.{start|stop|suspend|resume}Route({*})").at(TypeReferenceLocation.METHOD_CALL),
                        JavaClass.references("org.apache.camel.CamelContext.{startAllRoutes|isStartingRoutes|getRouteStatus}({*})").at(TypeReferenceLocation.METHOD_CALL)
                ))
        .perform(Iteration.over()
                .perform((Hint) Hint.titled("Route control methods were moved").withText("Methods for controlling routes were moved from `CamelContext` to the `RouteController`." +
                        "To call moved method use: `context.getRouteController().startRoute(\"myRoute\")`")
                        .withIssueCategory(mandatoryIssueCategory)
                        .with(Link.to("Camel migration guide - controlling routes", "https://camel.apache.org/manual/latest/camel-3-migration-guide.html#_controlling_routes")).withEffort(1)
                ))
        .withId("java-generic-information-00038")
