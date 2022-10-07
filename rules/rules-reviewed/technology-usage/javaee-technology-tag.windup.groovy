import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.operation.iteration.AbstractIterationOperation
import org.jboss.windup.config.phase.PreReportGenerationPhase
import org.jboss.windup.config.query.Query
import org.jboss.windup.reporting.model.TechnologyTagLevel
import org.jboss.windup.reporting.model.TechnologyUsageStatisticsModel
import org.jboss.windup.reporting.service.TechnologyTagService
import org.ocpsoft.rewrite.context.EvaluationContext

List<String> technologies = Arrays.asList("EJB", "Entity Bean", "JAX-RS", "JAX-WS", "JCA", "JDBC datasources", "JDBC XA datasources" , 
        "JMS Connection Factory", "JMS Queue", "JMS Topic", "JPA entities", "JPA named queries", "JSF Page", "JSP Page", "JTA" , 
        "Message (MDB)", "Persistence units", "RMI", "Stateful (SFSB)", "Stateless (SLSB)")

ruleSet("javaee-technology-tag")
    .setPhase(PreReportGenerationPhase.class)
    .addRule()
    .when(Query.fromType(TechnologyUsageStatisticsModel.class))
    .perform(new AbstractIterationOperation<TechnologyUsageStatisticsModel>() {
        void perform(GraphRewrite event, EvaluationContext context, TechnologyUsageStatisticsModel payload) {
            TechnologyTagService technologyTagService = new TechnologyTagService(event.getGraphContext())
            if (technologies.contains(payload.getName().trim())) {
                technologyTagService.addTagToFileModel(payload.getProjectModel().getRootFileModel(), payload.getName(), TechnologyTagLevel.INFORMATIONAL)
            }
        }
    })
    .withId("javaee-technology-tag-000100")

