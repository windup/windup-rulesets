import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.operation.Iteration;
import org.jboss.windup.config.parameters.ParameterizedIterationOperation;
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.rules.apps.java.scan.ast.annotations.JavaAnnotationTypeReferenceModel;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.param.ParameterStore;

import org.jboss.windup.graph.model.FileLocationModel;
import org.jboss.windup.ast.java.data.TypeReferenceLocation;
import org.jboss.windup.rules.apps.java.scan.ast.JavaTypeReferenceModel;
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.rules.apps.java.condition.JavaClass

ruleSet("java-glassfish-groovy")
.addSourceTechnology(new TechnologyReference("glassfish", null))
.addTargetTechnology(new TechnologyReference("eap", "[6,)"))
.addRule()
    .when(
        JavaClass.references("javax.jms.MessageListener").at(TypeReferenceLocation.IMPLEMENTS_TYPE).as("discard")
        .or(JavaClass.references("javax.ejb.MessageDriven").at(TypeReferenceLocation.ANNOTATION).as("default"))
    )
    .perform(
        Iteration.over("default").as(Iteration.DEFAULT_SINGLE_VARIABLE_STRING)
        .perform(
            new ParameterizedIterationOperation<JavaTypeReferenceModel> () {
                Hint hint = Hint
                    .titled("MDB - Implementation of MessageListener interface is required")
                    .withText("With JBoss EAP 6+ it is required to implement `javax.jms.MessageListener` or to specify the `messageListenerInterface` attribute in the `@MessageDriven` annotation in the case of a JMS MessageListener.")
                    .with(Link.to("MessageDriven Javadoc JavaEE 6", "https://docs.oracle.com/javaee/6/api/javax/ejb/MessageDriven.html"))
                    .with(Link.to("MessageDriven Javadoc JavaEE 7", "https://docs.oracle.com/javaee/7/api/javax/ejb/MessageDriven.html"))
                    .with(Link.to("Java EE 6 Tutorial", "https://docs.oracle.com/javaee/6/tutorial/doc/bnbpo.html"))
                    .withEffort(1);

                public Set<String> getRequiredParameterNames() {
                    return hint.getRequiredParameterNames();
                }

                public void setParameterStore(ParameterStore store) {
                    hint.setParameterStore(store);
                }

                public void performParameterized(final GraphRewrite event, final EvaluationContext context, final JavaTypeReferenceModel locationModel) {
                    boolean implementsMessageListener = false;
                    boolean hasMessageListenerAttribute = ((JavaAnnotationTypeReferenceModel)locationModel).annotationValues.get("messageListenerInterface") != null;

                    for (FileLocationModel otherLocation : locationModel.getFile().getAllTypeReferences()) {
                        if (!(otherLocation instanceof JavaTypeReferenceModel))
                            continue;

                        if (otherLocation.resolvedSourceSnippit != null && otherLocation.resolvedSourceSnippit.equals("javax.jms.MessageListener") && otherLocation.referenceLocation == TypeReferenceLocation.IMPLEMENTS_TYPE)
                            implementsMessageListener = true;
                    }

                    if (!hasMessageListenerAttribute && !implementsMessageListener)
                        hint.perform(event, context)
                }
            }
        )
    )
    .withId("java-glassfish-groovy-01000")
