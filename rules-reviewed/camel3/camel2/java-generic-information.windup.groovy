package camel3.camel2

import org.jboss.windup.ast.java.data.TypeReferenceLocation
import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.operation.Iteration
import org.jboss.windup.config.operation.iteration.AbstractIterationOperation
import org.jboss.windup.graph.model.FileLocationModel
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.HintEffort
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.reporting.model.QuickfixType
import org.jboss.windup.reporting.quickfix.Quickfix
import org.jboss.windup.rules.apps.java.condition.JavaClass
import org.jboss.windup.rules.files.condition.FileContent
import org.ocpsoft.rewrite.config.Or
import org.ocpsoft.rewrite.config.And
import org.ocpsoft.rewrite.context.EvaluationContext

import java.util.regex.Matcher
import java.util.regex.Pattern


final String MOVED_FROM = "org.apache.camel.ThreadPoolRejectedPolicy"
final String MOVED_TO = "org.apache.camel.util.concurrent.ThreadPoolRejectedPolicy"
final IssueCategory optionalIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.OPTIONAL)
final IssueCategory mandatoryIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY)
final String fromRegex = "from\\([\\s]*.*?\\)"
final String jndiRegex = "(.*createRegistry)"

Hint createHint(String title, String messase, String linkAppendix, boolean mandatory, Integer effort) {
    final String docTitleAppendix = (linkAppendix.substring(0, 1).toUpperCase() + linkAppendix.substring(1))
            .replaceAll("_", " ")

    HintEffort hint = Hint.titled(title)
            .withText(messase)
            .with(Link.to("Camel 3 - Migration Guide: $docTitleAppendix", "https://camel.apache.org/manual/latest/camel-3-migration-guide.html#_$linkAppendix"))
            .withEffort(effort)
    if (mandatory) {
        hint.withIssueCategory(new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY))
    }
    return (Hint) hint
}

Hint createHint(String title, String messase, String linkAppendix, boolean mandatory) {
    return createHint(title, messase, linkAppendix, mandatory, 1);
}

Hint createHint(String title, String messase, String linkAppendix, boolean mandatory, Quickfix quickfix) {
    return createHint(title, messase, linkAppendix, mandatory).withQuickfix(quickfix);
}

Hint createMovedClassHint(String moved, String movedTo, String addInfo, IssueCategory issueCategory, String linkAppendix) {
    Quickfix q = new Quickfix()
    q.setType(QuickfixType.REPLACE)
    q.setSearchStr(moved)
    q.setReplacementStr(movedTo)

    return createHint("$moved was moved", "`$moved` was moved to `$movedTo` ", linkAppendix, true, q)
}

JavaClass javaMethodCallCondition(String regex) {
    return JavaClass.references("org.apache.camel.$regex").at(TypeReferenceLocation.METHOD_CALL)
}


ruleSet("java-generic-information-groovy")
        .addSourceTechnology(new TechnologyReference("camel", "[2,3)"))
        .addTargetTechnology(new TechnologyReference("camel", "[3,)"))

        .addRule()
        .when(JavaClass.references(MOVED_FROM).at(TypeReferenceLocation.IMPORT).as("movedClass"))
        .perform(
                Iteration.over("movedClass")
                        .perform(createMovedClassHint(MOVED_FROM, MOVED_TO, "class", mandatoryIssueCategory, "eips"))
        )
        .withId("java-generic-information-00034")

        .addRule()
        .when(javaMethodCallCondition("impl.SimpleRegistry.put({*})"))
        .perform(createHint("SimpleRegistry was moved",
                "`The org.apache.camel.support.DefaultRegistry` should be favoured instead of `SimpleRegistry`." +
                        "Also `bind` operation should be used instead of `put` to add entries to the `SimpleRegistry` or `DefaultRegistry`.",
                "generic_information", false)
        )
        .withId("java-generic-information-00035")

        .addRule()
        .when(javaMethodCallCondition("Exchange.{get|has}Out()"))
        .perform(createHint("`getOut`/`hasOut` are deprecated",
                "Methods `getOut`, `hasOut` on `Exchange` has been deprecated in favour of using `getMessage` instead.",
                "getout_on_exchange", false))

        .withId("java-generic-information-00036")

        .addRule()
        .when(Or.any(
                javaMethodCallCondition("Message.{param}Fault({*})"),
                javaMethodCallCondition("CamelContext.{param}HandleFault({*})")
        ))
        .perform(createHint("Fault API on Message was removed",
                "Fault API was removed from `org.apache.camel.Message`. The option `handleFault` has also been removed" +
                        " and you now need to turn this on as endpoint or component option on `camel-cxf` or `camel-spring-ws`.",
                "fault_api_on_message", true)
        )
        .where("param").matches("(is|set)")
        .withId("java-generic-information-00037")

        .addRule()
        .when(Or.any(
                javaMethodCallCondition("CamelContext.{param}Route({*})"),
                javaMethodCallCondition("CamelContext.startAllRoutes()"),
                javaMethodCallCondition("CamelContext.isStartingRoutes()"),
                javaMethodCallCondition("CamelContext.getRouteStatus({*})")
        ))
        .perform(
                createHint("Route control methods were moved",
                        "Methods for controlling routes were moved from `CamelContext` to the `RouteController`." +
                                "To call moved method use: `context.getRouteController().startRoute(\"myRoute\")`",
                        "controlling_routes", true)
        )
        .where("param").matches("(start|stop|suspend|resume)")
        .withId("java-generic-information-00038")

        .addRule()
        .when(javaMethodCallCondition("{*}.Main.getCamelContext{param}()"))
        .perform(
                createHint("getCamelContextMap,getCamelContexts methods removed",
                        "The methods `getCamelContextMap` and `getCamelContexts` have been removed from the `Main` classes, and there is just a `getCamelContext` method now. ",
                        "main_class_2", true)
        )
        .where("param").matches("(s|map)")
        .withId("java-generic-information-00039")


        .addRule()
        .when(JavaClass.references("org.apache.camel.util.jsse.{param}").at(TypeReferenceLocation.IMPORT))
        .perform(createMovedClassHint("org.apache.camel.util.jsse.{param}", "org.apache.camel.support.jsse.{param}", "class", mandatoryIssueCategory, "jsse"))
        .where("param").matches(".*")
        .withId("java-generic-information-00040")

        .addRule()
        .when(JavaClass.references("org.apache.camel.util.jndi.JndiContext").at(TypeReferenceLocation.IMPORT))
        .perform(createMovedClassHint("org.apache.camel.util.jndi.JndiContext", "org.apache.camel.support.jndi.JndiContext", "class", mandatoryIssueCategory, "generic_classes"))
        .withId("java-generic-information-00041")

        .addRule()
        .when(And.all(
                JavaClass.references("{class}").at(TypeReferenceLocation.INHERITANCE).as("testClasses"),
                JavaClass.from("testClasses").references("{*}createRegistry({*})")
                        .at(TypeReferenceLocation.METHOD, TypeReferenceLocation.METHOD_CALL).as("registryMethod"),
        ))
        .perform(Iteration.over("registryMethod").
                perform(
                        createHint(
                                "Override of `createRegistry` is not necessary anymore",
                                " An override the `createRegistry` method for beans registration is no longer necessary. The preferred way is to use " +
                                        "the `bind` method from the Registry API: `context.getRegistry().bind(\"myId\", myBean);`",
                                "camel_test",
                                false)
                ))
        .where("class").matches("(org.apache.camel.test.junit4.CamelTestSupport|org.apache.camel.ContextTestSupport)")
        .withId("java-generic-information-00042")

        .addRule()
        .when(JavaClass.references("org.apache.camel.management.event.{event}").at(TypeReferenceLocation.IMPORT))
        .perform(createMovedClassHint("org.apache.camel.management.event.{event}", "org.apache.camel.spi.CamelEvent.{event}", "class", mandatoryIssueCategory, "jmx_events"))
        .where("event").matches(".*")
        .withId("java-generic-information-00043")

        .addRule()
        .when(And.all(
                JavaClass.references("org.apache.camel.test.junit4.CamelTestSupport").at(TypeReferenceLocation.INHERITANCE).as("adviceTestClasses"),
                JavaClass.from("adviceTestClasses").references("{*}adviceWith({*})")
                        .at(TypeReferenceLocation.METHOD, TypeReferenceLocation.METHOD_CALL).as("adviceWith"))
        )
        .perform(Iteration.over("adviceWith").
                perform(
                        createHint(
                                "Testing with `adviceWith` changed",
                                "Testing with `adviceWith` changed. It's necessary to use `RouteReifier` or `AdviceWithRouteBuilder` such as:" +
                                        " `AdviceWithRouteBuilder.adviceWith(context, \"myRoute\", a -> {\n" +
                                        "  a.replaceFromWith(\"direct:start\");\n" +
                                        "}`",
                                "advicewith",
                                true, 2)
                ))
        .withId("java-generic-information-00044")

        .addRule()
        .when(FileContent.matches("{regex}").inFileNamed("{*}.java").as("fromStrings"))
        .perform(new AbstractIterationOperation<FileLocationModel>() {
            void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {

                Hint h = createHint("Routes with multiple inputs aren't supported anymore",
                        "Routes with multiple inputs such as `from(\"input1\",\"input2\")` aren't supported anymore",
                        "routes_with_multiple_inputs", true,2)

                Matcher matcher = Pattern.compile("(\"[^\"]*?\")").matcher(payload.sourceSnippit)
                StringBuffer sb = new StringBuffer()
                while (matcher.find()) {
                    matcher.appendReplacement(sb, matcher.group(1).replaceAll(".*", "*"))
                }
                matcher.appendTail(sb)
                final String input = sb.toString()
                if ((input.substring(input.indexOf("("), input.lastIndexOf(")")).split(",").length > 1)) {
                    h.perform(event, context, payload)
                }
            }
        })
        .where("regex").matches(fromRegex)
        .withId("java-generic-information-00045")