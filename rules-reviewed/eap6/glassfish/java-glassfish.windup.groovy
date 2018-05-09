import org.jboss.windup.config.GraphRewrite;
import org.jboss.windup.config.parameters.ParameterizedIterationOperation;
import org.jboss.windup.config.metadata.TechnologyReference;
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
        JavaClass.references("javax.ejb.MessageDriven").at(TypeReferenceLocation.ANNOTATION)
    )
    .perform(
        new ParameterizedIterationOperation<JavaTypeReferenceModel> () {
            Hint hint = Hint
                .titled("MessageListener implementation is required")
                .withText("In GlassFish, it is not always required to implement a MessageListener interface. However, with JBoss EAP 6+ it is required to implement `javax.ejb.MessageListener` in the case of a JMS MessageListener.")
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
                for (FileLocationModel otherLocation : locationModel.getFile().getAllTypeReferences()) {
                    if (!(otherLocation instanceof JavaTypeReferenceModel))
                        continue;

                    if (otherLocation.getResolvedSourceSnippit() != null && otherLocation.getResolvedSourceSnippit().equals("javax.jms.MessageListener"))
                        implementsMessageListener = true;
                }
                if (!implementsMessageListener)
                    hint.perform(event, context)
            }
        }
    )
    .withId("java-glassfish-groovy-01000")
