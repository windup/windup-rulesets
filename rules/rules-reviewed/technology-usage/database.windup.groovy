import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.operation.iteration.AbstractIterationOperation
import org.jboss.windup.graph.GraphContext
import org.jboss.windup.graph.model.FileLocationModel
import org.jboss.windup.graph.model.resource.FileModel
import org.jboss.windup.graph.service.WindupConfigurationService
import org.jboss.windup.project.condition.Artifact
import org.jboss.windup.project.condition.Project
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.reporting.config.classification.Classification
import org.jboss.windup.reporting.model.TechnologyTagLevel
import org.jboss.windup.reporting.service.TechnologyTagService
import org.jboss.windup.rules.apps.java.condition.Dependency
import org.jboss.windup.rules.files.condition.File
import org.ocpsoft.rewrite.config.Or
import org.ocpsoft.rewrite.context.EvaluationContext

final IssueCategory potentialIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.POTENTIAL)

static boolean hasAnalysisTargetEap(GraphContext graphContext) {
    WindupConfigurationService.getConfigurationModel(graphContext)
        .getTargetTechnologies()
        .stream()
        .anyMatch { ("eap" == it.technologyID) }
}

static boolean hasAnalysisTargetAzure(GraphContext graphContext) {
    WindupConfigurationService.getConfigurationModel(graphContext)
        .getTargetTechnologies()
        .stream()
        .anyMatch { (it.technologyID.toLowerCase().contains("azure")) }
}

static void perform(GraphRewrite event, EvaluationContext context, FileModel fileModel, String technology, boolean withLink) {
    final IssueCategoryRegistry issueCategoryRegistry = IssueCategoryRegistry.instance(context)
    final Classification classification = (Classification) Classification.as("Embedded $technology")
        .withDescription("The application embeds an $technology .")
        .withEffort(0)
        .withIssueCategory(issueCategoryRegistry.getByID(IssueCategoryRegistry.INFORMATION))
    if (withLink && hasAnalysisTargetEap(event.getGraphContext())) {
        classification.with(Link.to("Red Hat JBoss Enterprise Application Platform (EAP) 7 Supported Configurations", "https://access.redhat.com/articles/2026253"))
    }
    classification.performParameterized(event, context, fileModel)
    addTagToFileModel(event.getGraphContext(), fileModel, technology)
}

static void addTagToFileModel(GraphContext graphContext, FileModel fileModel, String technology) {
    final TechnologyTagService technologyTagService = new TechnologyTagService(graphContext)
    technologyTagService.addTagToFileModel(fileModel, "$technology", TechnologyTagLevel.INFORMATIONAL)
}

// This is inherited from previous version of these rules (i.e. XML rules)
// in order to keep consistency with tests already available we start numbering from 14

ruleSet("database")
    .addRule()
    .when(File.inFileNamed("{*}hsqldb{*}.jar"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "HSQLDB Driver", true)
        }
    })
    .withId("database-01400")
    .addRule()
    .when(File.inFileNamed("{*}mysql-connector{*}.jar"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "MySQL Driver", true)
        }
    })
    .withId("database-01500")
    .addRule()
    .when(File.inFileNamed("{*}derby{*}.jar"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "Derby Driver", true)
        }
    })
    .withId("database-01600")
    .addRule()
    .when(File.inFileNamed("{*}postgresql{*}.jar"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            final FileModel fileModel = payload.getFile()
            final String technology = "PostgreSQL Driver"
            if (hasAnalysisTargetAzure(event.graphContext)) {
                final Hint hint = Hint.titled("PostgreSQL database found")
                .withText("""PostgreSQL database found.  
                        Consider using Azure PostgreSQL Flexible Server.""")
                .withIssueCategory(potentialIssueCategory)
                .with(Link.to("Azure Database for PostgreSQL", "https://azure.microsoft.com/products/postgresql"))
                .with(Link.to("Flexible Server documentation", "https://learn.microsoft.com/azure/postgresql/flexible-server"))
                .withEffort(3)
                hint.perform(event, context, payload)
                addTagToFileModel(event.getGraphContext(), fileModel, technology)
            } else {
                perform(event, context, fileModel, technology, true)
            }
        }
    })
    .withId("database-01700")
    .addRule()
    .when(File.inFileNamed("{*}h2{*}.jar"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "H2 Driver", true)
        }
    })
    .withId("database-01800")
    .addRule()
    .when(Or.any(
            File.inFileNamed("sqljdbc{*}.jar"),
            File.inFileNamed("mssql-jdbc{*}.jar")
        )
    )
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "Microsoft SQL Driver", true)
        }
    })
    .withId("database-01900")
    .addRule()
    .when(File.inFileNamed("{*}sqlite-jdbc{*}.jar"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "SQLite Driver", true)
        }
    })
    .withId("database-02000")
    // http://www.oracle.com/technetwork/database/application-development/jdbc/downloads/index.html
    .addRule()
    .when(Or.any(
            File.inFileNamed("{*}jodbc{*}.jar"),
            File.inFileNamed("{*}ojdbc{*}.jar")
        )
    )
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "Oracle DB Driver", true)
        }
    })
    .withId("database-02100")
    // https://mvnrepository.com/open-source/cassandra-clients
    .addRule()
    .when(Or.any(
            File.inFileNamed("{*}cassandra{*}.jar"),
            File.inFileNamed("{*}hector{*}.jar"),
            File.inFileNamed("{*}astyanax{*}.jar"),
            File.inFileNamed("{*}phantom-dsl{*}.jar"),
            File.inFileNamed("{*}cql{*}.jar"),
            File.inFileNamed("{*}hecuba-client{*}.jar"),
            File.inFileNamed("{*}c-star-path{*}.jar"),
            File.inFileNamed("{*}scale7-pelops{*}.jar")
        )
    )
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "Cassandra Client", true)
        }
    })
    .withId("database-02200")
    .addRule()
    .when(File.inFileNamed("{*}axion{*}.jar"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "Axion Driver", true)
        }
    })
    .withId("database-02300")
    .addRule()
    .when(File.inFileNamed("{*}mckoisqldb{*}.jar"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "MckoiSQLDB Driver", true)
        }
    })
    .withId("database-02400")
    // https://mvnrepository.com/open-source/mongodb-clients
    .addRule()
    .when(Or.any(
            File.inFileNamed("{*}mongodb{*}.jar"),
            File.inFileNamed("{*}casbah{*}.jar"),
            File.inFileNamed("{*}reactivemongo{*}.jar"),
            File.inFileNamed("{*}jongo{*}.jar"),
            File.inFileNamed("{*}gmongo{*}.jar"),
            File.inFileNamed("{*}rogue{*}.jar")
        )
    )
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "MongoDB Client", true)
        }
    })
    .withId("database-02500")
    .addRule()
    .when(File.inFileNamed("spring-data{*}.jar"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            if (payload.getFile().getFileName().matches("spring-data.*test.*.jar")) return
            final IssueCategoryRegistry issueCategoryRegistry = IssueCategoryRegistry.instance(context)
            final Classification classification = (Classification) Classification.as("Embedded framework - Spring Data")
                .withDescription("The application embeds the Spring Data framework.")
                .withEffort(0)
                .withIssueCategory(issueCategoryRegistry.getByID(IssueCategoryRegistry.INFORMATION))
            if (hasAnalysisTargetEap(event.getGraphContext())) {
                classification.with(Link.to("Red Hat JBoss Enterprise Application Platform (EAP) 7 Supported Configurations", "https://access.redhat.com/articles/2026253"))
            }
            classification.performParameterized(event, context, payload.getFile())
            final TechnologyTagService technologyTagService = new TechnologyTagService(event.getGraphContext())
            technologyTagService.addTagToFileModel(payload.getFile(), "Spring Data", TechnologyTagLevel.INFORMATIONAL)
        }
    })
    .withId("database-02600")
    .addRule()
    .when(File.inFileNamed("{*}morphia{*}.jar"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            final IssueCategoryRegistry issueCategoryRegistry = IssueCategoryRegistry.instance(context)
            final Classification classification = (Classification) Classification.as("Embedded framework - Morphia")
                .withDescription("The application embeds Morphia.")
                .withEffort(0)
                .withIssueCategory(issueCategoryRegistry.getByID(IssueCategoryRegistry.INFORMATION))
            if (hasAnalysisTargetEap(event.getGraphContext())) {
                classification.with(Link.to("Red Hat JBoss Enterprise Application Platform (EAP) 7 Supported Configurations", "https://access.redhat.com/articles/2026253"))
            }
            classification.performParameterized(event, context, payload.getFile())
            final TechnologyTagService technologyTagService = new TechnologyTagService(event.getGraphContext())
            technologyTagService.addTagToFileModel(payload.getFile(), "Morphia", TechnologyTagLevel.INFORMATIONAL)
        }
    })
    .withId("database-02700")
    .addRule()
    .when(File.inFileNamed("{*}leveldb{*}.jar"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "LevelDB Client", true)
        }
    })
    .withId("database-02800")
    .addRule()
    .when(File.inFileNamed("{*}hbase{*}.jar"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "Apache HBase Client", true)
        }
    })
    .withId("database-02900")
    .addRule()
    .when(File.inFileNamed("{*}accumulo{*}.jar"))
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            perform(event, context, payload.getFile(), "Apache Accumulo Client", true)
        }
    })
    .withId("database-03000")
    .addRule()
    .when(Or.any(
            Project.dependsOnArtifact(Artifact.withGroupId("org.springframework.data").andArtifactId("spring-data-jpa")),
            Project.dependsOnArtifact(Artifact.withGroupId("org.springframework.boot").andArtifactId("spring-boot-starter-data-jpa")),
            Dependency.withGroupId("org.springframework.data").andArtifactId("spring-data-jpa")
        )
    )
    .perform(new AbstractIterationOperation<FileLocationModel>() {
        void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
            final IssueCategoryRegistry issueCategoryRegistry = IssueCategoryRegistry.instance(context)
            final Classification classification = (Classification) Classification.as("Embedded Spring Data JPA")
                .withDescription("The application embeds Spring Data JPA")
                .withEffort(0)
                .withIssueCategory(issueCategoryRegistry.getByID(IssueCategoryRegistry.INFORMATION))
            classification.performParameterized(event, context, payload.getFile())
            final TechnologyTagService technologyTagService = new TechnologyTagService(event.getGraphContext())
            technologyTagService.addTagToFileModel(payload.getFile(), "Spring Data JPA", TechnologyTagLevel.INFORMATIONAL)
        }
    })
    .withId("database-03100")
