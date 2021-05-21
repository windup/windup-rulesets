import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.operation.iteration.AbstractIterationOperation
import org.jboss.windup.config.phase.DependentPhase
import org.jboss.windup.config.phase.PostMigrationRulesPhase
import org.jboss.windup.config.phase.PreReportGenerationPhase
import org.jboss.windup.config.query.Query
import org.jboss.windup.graph.model.ProjectModel
import org.jboss.windup.reporting.model.TechnologyTagLevel
import org.jboss.windup.reporting.service.TechnologyTagService
import org.jboss.windup.rules.apps.javaee.model.EjbBeanBaseModel
import org.ocpsoft.rewrite.context.EvaluationContext

ruleSet("ejb-technology-usage-groovy")
/* if we need to query for TechnologyUsageStatisticsModel models 
   added during PostMigrationRulesPhase phase
.setPhase(DependentPhase.class)
.addExecuteAfter(PostMigrationRulesPhase.class)
.addExecuteBefore(PreReportGenerationPhase.class)
*/
.setPhase(PostMigrationRulesPhase.class)
.addRule()
    .when(
        Query.fromType(EjbBeanBaseModel.class)
    )
    .perform(
        new AbstractIterationOperation<EjbBeanBaseModel>()
        {
            void perform(GraphRewrite event, EvaluationContext context, EjbBeanBaseModel payload)
            {
                List<ProjectModel> projectModels = payload.getApplications()
                for (ProjectModel projectModel : projectModels)
                {
                    TechnologyTagService technologyTagService = new TechnologyTagService(event.getGraphContext())
                    technologyTagService.addTagToFileModel(projectModel.getRootFileModel(), "Enterprise Java Bean", TechnologyTagLevel.INFORMATIONAL)
                }
            }

        }
    )
    .withId("ejb-technology-usage-groovy-00000")
