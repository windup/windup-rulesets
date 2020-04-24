package camel3.camel2

import org.jboss.windup.ast.java.data.TypeReferenceLocation
import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.operation.Iteration

import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.reporting.model.QuickfixType
import org.jboss.windup.reporting.quickfix.Quickfix
import org.jboss.windup.rules.apps.java.condition.JavaClass

final String MOVED_FROM = "org.apache.camel.ThreadPoolRejectedPolicy"
final String MOVED_TO = "org.apache.camel.util.concurrent.ThreadPoolRejectedPolicy"

Hint createMovedClassHint(String moved, String movedTo, String addInfo) {
    Quickfix q = new Quickfix()
    q.setType(QuickfixType.REPLACE)
    q.setSearchStr(moved)
    q.setReplacementStr(movedTo)

    return (Hint) ((Hint) Hint.titled("$moved was moved").withText("`$moved` was moved to `$movedTo` ")
            .with(Link.to("Camel migration guide", "https://camel.apache.org/manual/latest/camel-3-migration-guide.html#_eips"))).withQuickfix(q)
            .withEffort(1);
}

ruleSet("java-generic-information-groovy")
        .addSourceTechnology(new TechnologyReference("camel", "[2,3)"))
        .addTargetTechnology(new TechnologyReference("camel", "[3,)"))
        .addRule()
        .when(JavaClass.references(MOVED_FROM).at(TypeReferenceLocation.IMPORT).as("movedClass"))
        .perform(
                Iteration.over("movedClass")
                        .perform(createMovedClassHint(MOVED_FROM, MOVED_TO, "class"))
        )
        .withId("java-generic-information-00034")

