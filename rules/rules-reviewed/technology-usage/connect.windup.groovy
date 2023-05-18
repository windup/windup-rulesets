import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.operation.iteration.AbstractIterationOperation
import org.jboss.windup.graph.GraphContext
import org.jboss.windup.graph.model.FileLocationModel
import org.jboss.windup.graph.model.resource.FileModel
import org.jboss.windup.graph.service.WindupConfigurationService
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.reporting.config.classification.Classification
import org.jboss.windup.reporting.model.TechnologyTagLevel
import org.jboss.windup.reporting.service.TechnologyTagService
import org.jboss.windup.rules.files.condition.File
import org.ocpsoft.rewrite.config.Or
import org.ocpsoft.rewrite.context.EvaluationContext

static boolean hasAnalysisTargetEap(GraphContext graphContext) {
    WindupConfigurationService.getConfigurationModel(graphContext)
        .getTargetTechnologies()
        .stream()
        .anyMatch { ("eap" == it.technologyID) }
}

static void perform(GraphRewrite event, EvaluationContext context, FileModel fileModel, String technology, boolean withLink) {
    final IssueCategoryRegistry issueCategoryRegistry = IssueCategoryRegistry.instance(context)
    final Classification classification = (Classification) Classification.as("Embedded library - $technology")
        .withDescription("The application embeds an $technology library.")
        .withEffort(0)
        .withIssueCategory(issueCategoryRegistry.getByID(IssueCategoryRegistry.INFORMATION))
    if (withLink && hasAnalysisTargetEap(event.getGraphContext())) {
        classification.with(Link.to("Red Hat JBoss Enterprise Application Platform (EAP) 7 Supported Configurations", "https://access.redhat.com/articles/2026253"))
    }
    classification.performParameterized(event, context, fileModel)
    final TechnologyTagService technologyTagService = new TechnologyTagService(event.getGraphContext())
    technologyTagService.addTagToFileModel(fileModel, technology, TechnologyTagLevel.INFORMATIONAL)
}

// this is inherited from previous version of these rules (i.e. XML rules)
// in order to keep consistency with tests already available
int id = 14;

ruleSet("connect")
    .addRule()
    .when(File.inFileNamed("{*}.rar"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            final IssueCategoryRegistry issueCategoryRegistry = IssueCategoryRegistry.instance(context)
            final Classification classification = (Classification) Classification.as("Embedded Resource Adapter")
                .withDescription("The application embeds a resource adapter.")
                .withEffort(0)
                .withIssueCategory(issueCategoryRegistry.getByID(IssueCategoryRegistry.INFORMATION))
            classification.performParameterized(event, context, payload.getFile())
            final TechnologyTagService technologyTagService = new TechnologyTagService(event.getGraphContext())
            technologyTagService.addTagToFileModel(payload.getFile(), "Resource Adapter", TechnologyTagLevel.INFORMATIONAL)
        }
    })
    .withId(String.format("connect-0%d00", id++))
    .addRule()
    .when(File.inFileNamed("{*}activemq{*}"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
/*
            final IssueCategoryRegistry issueCategoryRegistry = IssueCategoryRegistry.instance(context)
            final Classification classification = (Classification) Classification.as("Embedded library - ActiveMQ")
                .withDescription("The application embeds an ActiveMQ client library.")
                .withEffort(0)
                .withIssueCategory(issueCategoryRegistry.getByID(IssueCategoryRegistry.INFORMATION))
            if (hasAnalysisTargetEap(event.getGraphContext())) {
                classification.with(eap7SupportedConfigurations)
            }
            classification.performParameterized(event, context, payload.getFile())
            final TechnologyTagService technologyTagService = new TechnologyTagService(event.getGraphContext())
            technologyTagService.addTagToFileModel(payload.getFile(), "ActiveMQ", TechnologyTagLevel.INFORMATIONAL)
*/
            perform(event, context, payload.getFile(), "ActiveMQ", true)
        }
    })
    .withId(String.format("connect-0%d00", id++))
    .addRule()
    .when(File.inFileNamed("{*}openws{*}"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
/*
            final IssueCategoryRegistry issueCategoryRegistry = IssueCategoryRegistry.instance(context)
            final Classification classification = (Classification) Classification.as("Embedded library - OpenWS")
                .withDescription("The application embeds an OpenWS library.")
                .withEffort(0)
                .withIssueCategory(issueCategoryRegistry.getByID(IssueCategoryRegistry.INFORMATION))
            classification.performParameterized(event, context, payload.getFile())
            final TechnologyTagService technologyTagService = new TechnologyTagService(event.getGraphContext())
            technologyTagService.addTagToFileModel(payload.getFile(), "OpenWS", TechnologyTagLevel.INFORMATIONAL)
*/
            perform(event, context, payload.getFile(), "OpenWS", false)
        }
    })
    .withId(String.format("connect-0%d00", id++))
    .addRule()
    .when(File.inFileNamed("{*}wsdl{*}"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
/*
            final IssueCategoryRegistry issueCategoryRegistry = IssueCategoryRegistry.instance(context)
            final Classification classification = (Classification) Classification.as("Embedded library - WSDL")
                .withDescription("The application embeds a WSDL library.")
                .withEffort(0)
                .withIssueCategory(issueCategoryRegistry.getByID(IssueCategoryRegistry.INFORMATION))
            classification.performParameterized(event, context, payload.getFile())
            final TechnologyTagService technologyTagService = new TechnologyTagService(event.getGraphContext())
            technologyTagService.addTagToFileModel(payload.getFile(), "WSDL", TechnologyTagLevel.INFORMATIONAL)
*/
            perform(event, context, payload.getFile(), "WSDL", false)
        }
    })
    .withId(String.format("connect-0%d00", id++))
    .addRule()
    .when(Or.any(
            File.inFileNamed("{*}amqp-client{*}"),
            File.inFileNamed("{*}rabbitmq{*}"),
            File.inFileNamed("{*}spring-rabbit{*}"),
            File.inFileNamed("{*}lyra{*}"),
            File.inFileNamed("{*}conduit{*}")
        )
    )
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
/*
            final IssueCategoryRegistry issueCategoryRegistry = IssueCategoryRegistry.instance(context)
            final Classification classification = (Classification) Classification.as("Embedded library - RabbitMQ Client")
                .withDescription("The application embeds a RabbitMQ client library.")
                .withEffort(0)
                .withIssueCategory(issueCategoryRegistry.getByID(IssueCategoryRegistry.INFORMATION))
            if (hasAnalysisTargetEap(event.getGraphContext())) {
                classification.with(eap7SupportedConfigurations)
            }
            classification.performParameterized(event, context, payload.getFile())
            TechnologyTagService technologyTagService = new TechnologyTagService(event.getGraphContext())
            technologyTagService.addTagToFileModel(payload.getFile(), "RabbitMQ Client", TechnologyTagLevel.INFORMATIONAL)
*/
            perform(event, context, payload.getFile(), "RabbitMQ Client", true)
        }
    })
    .withId(String.format("connect-0%d00", id++))
    .addRule()
    .when(Or.any(
            File.inFileNamed("{*}spring-messaging{*}"),
            File.inFileNamed("{*}spring-jms{*}")
        )
    )
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "Spring Messaging Client", true)
        }
    })
    .withId(String.format("connect-0%d00", id++))
    .addRule()
    .when(File.inFileNamed("{*}camel-jms{*}"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "Camel Messaging Client", true)
        }
    })
    .withId(String.format("connect-0%d00", id++))
    .addRule()
    .when(File.inFileNamed("{*}aws-java-sdk-sqs{*}"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "Amazon SQS Client", false)
        }
    })
    .withId(String.format("connect-0%d00", id++))
    .addRule()
    .when(File.inFileNamed("{*}hornetq{*}"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "HornetQ Client", true)
        }
    })
    .withId(String.format("connect-0%d00", id++))
    .addRule()
    .when(File.inFileNamed("{*}amqp{*}"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "AMQP Client", true)
        }
    })
    .withId(String.format("connect-0%d00", id++))
    .addRule()
    .when(File.inFileNamed("{*}rocketmq-client{*}"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "RocketMQ Client", true)
        }
    })
    .withId(String.format("connect-0%d00", id++))
    .addRule()
    .when(Or.any(
            File.inFileNamed("{*}jzmq{*}"),
            File.inFileNamed("{*}jeromq{*}")
        )
    )
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "0MQ Client", true)
        }
    })
    .withId(String.format("connect-0%d00", id++))
    .addRule()
    .when(File.inFileNamed("{*}jbossmq-client{*}"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "JBossMQ Client", true)
        }
    })
    .withId(String.format("connect-0%d00", id++))
    .addRule()
    .when(File.inFileNamed("{*}zbus-client{*}"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "Zbus Client", true)
        }
    })
    .withId(String.format("connect-0%d00", id++))
    .addRule()
    .when(File.inFileNamed("{*}qpid{*}"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "Qpid Client", true)
        }
    })
    .withId(String.format("connect-0%d00", id++))
    .addRule()
    .when(Or.any(
        File.inFileNamed("{*}kafka-clients{*}"),
        File.inFileNamed("{*}spring-kafka{*}")
    )
    )
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "Kafka Client", false)
        }
    })
    .withId(String.format("connect-0%d00", id))
