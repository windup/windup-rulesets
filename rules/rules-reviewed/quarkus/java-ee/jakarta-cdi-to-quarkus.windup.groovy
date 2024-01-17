package quarkus.javaee

import org.jboss.windup.ast.java.data.TypeReferenceLocation
import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.Variables
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.operation.Iteration
import org.jboss.windup.config.operation.iteration.AbstractIterationOperation
import org.jboss.windup.config.query.Query
import org.jboss.windup.config.query.QueryPropertyComparisonType
import org.jboss.windup.graph.model.FileLocationModel
import org.jboss.windup.graph.model.FileReferenceModel
import org.jboss.windup.graph.model.ProjectModel
import org.jboss.windup.graph.model.WindupVertexFrame
import org.jboss.windup.graph.model.resource.FileModel
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.rules.apps.java.condition.JavaClass
import org.jboss.windup.rules.apps.java.condition.annotation.AnnotationTypeCondition
import org.jboss.windup.rules.apps.java.scan.ast.annotations.JavaAnnotationTypeReferenceModel
import org.jboss.windup.rules.apps.xml.condition.XmlFile
import org.ocpsoft.rewrite.config.And
import org.ocpsoft.rewrite.config.Or
import org.ocpsoft.rewrite.context.EvaluationContext

import java.util.stream.Collectors
import java.util.stream.StreamSupport

final IssueCategory potentialIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.POTENTIAL)
final Link guideLink = Link.to("Quarkus - Guides", "https://quarkus.io/guides/cdi-reference")
final Link cdiSpecLink = Link.to("CDI 2.0 - Scopes: Default scope", "https://docs.jboss.org/cdi/spec/2.0/cdi-spec.html#default_scope")

static boolean matchesProject(GraphRewrite event, FileLocationModel payload) {
    final Iterable<? extends WindupVertexFrame> previouslyFound = Optional.ofNullable(Variables.instance(event).findVariable("discard")).orElse(Collections.emptySet())
    final Set<ProjectModel> projectModels = StreamSupport.stream(previouslyFound.spliterator(), false)
        .map {
            if (it instanceof FileReferenceModel) return ((FileReferenceModel) it).getFile().getProjectModel()
            else if (it instanceof FileModel) return ((FileModel) it).getProjectModel()
            else return null
        }
        .collect (Collectors.toSet())
    final boolean matchesProject = projectModels.isEmpty() || projectModels.stream().anyMatch{payload.getFile().belongsToProject(it)}
    return matchesProject
}

ruleSet("jakarta-cdi-to-quarkus-groovy")
    .addSourceTechnology(new TechnologyReference("java-ee", null))
    .addTargetTechnology(new TechnologyReference("quarkus", null))
// this rule si required for Windup to know about storing data related to the classes involved in the
// `when` condition because useful later on in the `perform` step of the next rule
    .addRule()
    .when(
        Or.any(
            JavaClass.references("jakarta.enterprise.context.{scope}").at(TypeReferenceLocation.ANNOTATION).as("placeholder1"),
            JavaClass.references("jakarta.inject.Singleton").at(TypeReferenceLocation.ANNOTATION).as("placeholder2"),
        )
    )
    .where("scope").matches("(ApplicationScoped|ConversationScoped|Dependent|RequestScoped|SessionScoped)")
    .withId("jakarta-cdi-to-quarkus-groovy-00000")
    .addRule()
    .when(
        JavaClass.references("jakarta.inject.Inject").at(TypeReferenceLocation.ANNOTATION).as("main")
    )
    .perform(
        Iteration.over("main")
            .perform(
                new AbstractIterationOperation<JavaAnnotationTypeReferenceModel>() {
                    public static final String FROM_FILES_IN_PROJECT = "filesInProject"
                    public static final String INJECT_CLASS_DECLARATION = "injectClassDeclaration"

                    void perform(GraphRewrite event, EvaluationContext context, JavaAnnotationTypeReferenceModel payload) {
                        final String annotatedClass = payload.getAnnotatedType().getResolvedSourceSnippit()
                        final boolean injectedClassHasScopeAnnotations =
                            JavaClass.references(annotatedClass)
                                .at(TypeReferenceLocation.TYPE)
                                .annotationMatches(new AnnotationTypeCondition("jakarta.enterprise.context.(ApplicationScoped|ConversationScoped|Dependent|RequestScoped|SessionScoped)"))
                                .as("discard")
                                .evaluate(event, context)
                        final boolean injectedClassHasSingletonAnnotations =
                            JavaClass.references(annotatedClass)
                                .at(TypeReferenceLocation.TYPE)
                                .annotationMatches(new AnnotationTypeCondition("jakarta.inject.Singleton"))
                                .as("discardAsWell")
                                .evaluate(event, context)
                        if (!injectedClassHasScopeAnnotations && !injectedClassHasSingletonAnnotations) {
                            // first of all select only the file belonging to the same root project as the payload
                            // to reduce (i.e. optimize) the number of files found from the second query
                            final FileModel fileModel = payload.getFile()
                            final String filePath = fileModel.getProjectModel().getRootFileModel().getPrettyPath() + "/"
                            Query.fromType(FileModel.class).withProperty(FileModel.FILE_PATH, QueryPropertyComparisonType.CONTAINS_TOKEN, filePath).as(FROM_FILES_IN_PROJECT).evaluate(event, context)
                            //Query.fromType(FileModel.class).withProperty(JavaClass.from
                            JavaClass.from(FROM_FILES_IN_PROJECT).references(annotatedClass).at(TypeReferenceLocation.TYPE).as(INJECT_CLASS_DECLARATION).evaluate(event, context)
                                Iteration.over(INJECT_CLASS_DECLARATION)
                                    .perform(
                                        ((Hint) Hint.titled("Injected class is missing scope annotation")
                                            .withText("""
                                            A class injected but missing an annotation to define its scope type is not going to be discovered from Quarkus.  
                                            Consider adding the `@Dependent` scope which is the default scope for a bean which does not explicitly declare a scope type (ref. [CDI 2.0 - Scopes: Default scope](https://docs.jboss.org/cdi/spec/2.0/cdi-spec.html#default_scope))
                                            """)
                                            .withIssueCategory(potentialIssueCategory)
                                            .with(guideLink)
                                            .with(cdiSpecLink)
                                            .withEffort(1))
                                    )
                                .endIteration()
                        }
                    }
                }
            )
            .endIteration()
    )
    .withId("jakarta-cdi-to-quarkus-groovy-00010") 
// suggest to replace cdi-api TRANSITIVE dependency if no Quarkus dependency has been already added and 'javax.enterprise.{packages}.{*}' package is used somewhere in the code
    .addRule()
    .when(
        And.all(
            JavaClass.references("jakarta.enterprise.{packages}.{*}").at(TypeReferenceLocation.ANNOTATION).as("discard"),
            XmlFile.matchesXpath("/m:project/m:dependencies[count(m:dependency/m:artifactId[contains(., 'cdi-api')]) = 0 and count(m:dependency/m:artifactId[contains(., 'quarkus-')]) = 0]")
                .inFile("pom.xml")
                .namespace("m", "http://maven.apache.org/POM/4.0.0")
                .as("dependencies-section")
        )
    )
    .perform(
        Iteration.over("dependencies-section").perform(
            new AbstractIterationOperation<FileLocationModel>() {
                void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
                    if (matchesProject(event, payload)) {
                        ((Hint) Hint.titled("Remove jakarta.enterprise:cdi-api transitive dependency")
                            .withText("""
                            Transitive dependency `jakarta.enterprise:cdi-api` should be removed and the `io.quarkus:quarkus-arc` dependency added.  
                            """)
                            .withIssueCategory(potentialIssueCategory)
                            .with(guideLink)
                            .withEffort(1)
                        ).performParameterized(event, context, payload)
                    }
                }
            }
        )
            .endIteration()
    )
    .where("packages").matches("(context|event|inject|util)")
    .withId("jakarta-cdi-to-quarkus-groovy-00020")
// suggest to replace javax.inject TRANSITIVE dependency if no Quarkus dependency has been already added and 'javax.inject' package is used somewhere in the code
    .addRule()
    .when(
        And.all(
            JavaClass.references("jakarta.inject.{*}").at(TypeReferenceLocation.ANNOTATION).as("discard"),
            XmlFile.matchesXpath("/m:project/m:dependencies[count(m:dependency/m:artifactId[contains(., 'jakarta.inject')]) = 0 and count(m:dependency/m:artifactId[contains(., 'quarkus-')]) = 0]")
                .inFile("pom.xml")
                .namespace("m", "http://maven.apache.org/POM/4.0.0")
                .as("dependencies-section")
        )
    )
    .perform(
        Iteration.over("dependencies-section").perform(
            new AbstractIterationOperation<FileLocationModel>() {
                void perform(GraphRewrite event, EvaluationContext context, FileLocationModel payload) {
                    if (matchesProject(event, payload)) {
                        ((Hint) Hint.titled("Remove jakarta.inject:jakarta.inject transitive dependency")
                            .withText("""
                            The application has a transitive `javax.inject:javax.inject` dependency because at least one Java class that imports from the `javax.inject` has been found.  
                            The direct dependency injecting `javax.inject:javax.inject` should be identified and replaced with the `io.quarkus:quarkus-arc` dependency.
                            """)
                            .withIssueCategory(potentialIssueCategory)
                            .with(guideLink)
                            .withEffort(1)
                        ).performParameterized(event, context, payload)
                    }
                }
            }
        )
            .endIteration()
    )
    .withId("jakarta-cdi-to-quarkus-groovy-00030")