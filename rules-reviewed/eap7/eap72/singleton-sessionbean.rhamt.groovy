import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.parameters.ParameterizedIterationOperation
import org.jboss.windup.config.query.Query
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.rules.apps.java.model.JavaClassModel
import org.jboss.windup.rules.apps.java.scan.ast.JavaTypeReferenceModel
import org.jboss.windup.rules.apps.java.service.TypeReferenceService
import org.jboss.windup.rules.apps.javaee.model.EjbSessionBeanModel
import org.ocpsoft.rewrite.context.EvaluationContext
import org.ocpsoft.rewrite.param.ParameterStore

ruleSet("singleton-sessionbean-groovy")
    .addTargetTechnology(new TechnologyReference("eap", "[7,8)"))
    .addRule()
    .when(Query.fromType(EjbSessionBeanModel.class).withProperty("sessionType", "Singleton"))
    .perform(
        new ParameterizedIterationOperation<EjbSessionBeanModel>() {
            Hint hint = Hint
                    .titled("Removed SessionBean interface")
                    .withText(
                            """When a singleton EJB bean class implements `javax.ejb.SessionBean` interface, this interface should be removed from the implements clause.  
                            All methods declared in `javax.ejb.SessionBean` interface (see below) that are implemented in the bean class or its super classes should be checked for `@Override` annotation and remove this annotation too if present.  
                            Methods declared by `javax.ejb.SessionBean` interface:  
                            
                            ```
                            void setSessionContext(SessionContext ctx);  
                            
                            void ejbRemove();  
                            
                            void ejbActivate();  
                            
                            void ejbPassivate();  
                            ```
                            """)
                    .withIssueCategory(new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY))
                    .with(Link.to("FIXME", "FIXME"))
                    .withEffort(1);

            Set<String> getRequiredParameterNames() {
                return hint.getRequiredParameterNames();
            }

            void setParameterStore(ParameterStore store) {
                hint.setParameterStore(store);
            }

            void performParameterized(final GraphRewrite event, final EvaluationContext context, final EjbSessionBeanModel ejbSessionBeanModel) {
                JavaClassModel ejbJavaClassModel = ejbSessionBeanModel.getEjbClass()
                List<JavaClassModel> interfaces = ejbJavaClassModel.getInterfaces()
                interfaces.each{
                    if ("javax.ejb.SessionBean" == it.getQualifiedName())
                    {
                        TypeReferenceService typeReferenceService = new TypeReferenceService(event.getGraphContext())
                        JavaTypeReferenceModel javaTypeReferenceModel = typeReferenceService.getUniqueByProperty(JavaTypeReferenceModel.RESOLVED_SOURCE_SNIPPIT, ejbJavaClassModel.getQualifiedName())
                        hint.perform(event, context, javaTypeReferenceModel)
                        return true
                    }
                }
            }
        }
    )
    .withId("singleton-sessionbean-groovy-00001")

