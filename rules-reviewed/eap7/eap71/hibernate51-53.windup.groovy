import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.parameters.ParameterizedIterationOperation;
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.model.QuickfixType
import org.jboss.windup.reporting.quickfix.Quickfix
import org.jboss.windup.rules.apps.java.model.JavaClassModel
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.param.ParameterStore;

import org.jboss.windup.ast.java.data.TypeReferenceLocation;
import org.jboss.windup.rules.apps.java.scan.ast.JavaTypeReferenceModel;
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.rules.apps.java.condition.JavaClass
import org.ocpsoft.rewrite.param.RegexParameterizedPatternBuilder

import java.util.stream.Collectors

final String METHOD_PARAM_NAME = "methods_param";
final Map<String, List<String>> methodsInterfaces = new HashMap<>(21);
methodsInterfaces.put("assemble", new ArrayList<>(Arrays.asList(
        "org.hibernate.usertype.CompositeUserType", 
        "org.hibernate.type.Type")));

methodsInterfaces.put("beforeAssemble", new ArrayList<>(Arrays.asList(
        "org.hibernate.type.Type")));

methodsInterfaces.put("disassemble", new ArrayList<>(Arrays.asList(
        "org.hibernate.usertype.CompositeUserType", 
        "org.hibernate.type.Type")));

methodsInterfaces.put("extract", new ArrayList<>(Arrays.asList(
        "org.hibernate.type.ProcedureParameterExtractionAware")));

methodsInterfaces.put("get", new ArrayList<>(Arrays.asList(
        "org.hibernate.type.SingleColumnType")));

methodsInterfaces.put("hydrate", new ArrayList<>(Arrays.asList(
        "org.hibernate.type.Type")));

methodsInterfaces.put("instantiate", new ArrayList<>(Arrays.asList(
        "org.hibernate.usertype.UserCollectionType")));

methodsInterfaces.put("isDirty", new ArrayList<>(Arrays.asList(
        "org.hibernate.type.Type")));

methodsInterfaces.put("isModified", new ArrayList<>(Arrays.asList(
        "org.hibernate.type.Type")));

methodsInterfaces.put("next", new ArrayList<>(Arrays.asList(
        "org.hibernate.usertype.UserVersionType",
        "org.hibernate.type.VersionType")));

methodsInterfaces.put("nullSafeGet", new ArrayList<>(Arrays.asList(
        "org.hibernate.type.Type", 
        "org.hibernate.type.SingleColumnType",
        "org.hibernate.usertype.UserType",
        "org.hibernate.usertype.CompositeUserType")));

methodsInterfaces.put("nullSafeSet", new ArrayList<>(Arrays.asList(
        "org.hibernate.type.ProcedureParameterNamedBinder", 
        "org.hibernate.type.Type", 
        "org.hibernate.usertype.UserType",
        "org.hibernate.usertype.CompositeUserType")));

methodsInterfaces.put("replace", new ArrayList<>(Arrays.asList(
        "org.hibernate.type.Type",
        "org.hibernate.usertype.CompositeUserType")));

methodsInterfaces.put("replaceElements", new ArrayList<>(Arrays.asList(
        "org.hibernate.usertype.UserCollectionType")));

methodsInterfaces.put("resolve", new ArrayList<>(Arrays.asList(
        "org.hibernate.type.Type")));

methodsInterfaces.put("seed", new ArrayList<>(Arrays.asList(
        "org.hibernate.usertype.UserVersionType",
        "org.hibernate.type.VersionType")));

methodsInterfaces.put("semiResolve", new ArrayList<>(Arrays.asList(
        "org.hibernate.type.Type")));

methodsInterfaces.put("set", new ArrayList<>(Arrays.asList(
        "org.hibernate.type.SingleColumnType")));

methodsInterfaces.put("setCurrentSession", new ArrayList<>(Arrays.asList(
        "org.hibernate.collection.spi.PersistentCollection")));

methodsInterfaces.put("unsetSession", new ArrayList<>(Arrays.asList(
        "org.hibernate.collection.spi.PersistentCollection")));

methodsInterfaces.put("wrap", new ArrayList<>(Arrays.asList(
        "org.hibernate.usertype.UserCollectionType")));

final Map<String, String> methodsClasses = new HashMap<>(3);
methodsClasses.put("get", "org.hibernate.type.AbstractStandardBasicType");
methodsClasses.put("nullSafeGet", "org.hibernate.type.AbstractStandardBasicType");
methodsClasses.put("set", "org.hibernate.type.AbstractStandardBasicType");

final String methodParamPattern = methodsInterfaces.keySet().stream().collect(Collectors.joining("|","(",")"));
final String methodParam = "{" + METHOD_PARAM_NAME + "}";

Hint hint = (Hint) Hint
        .titled("Hibernate 5.3 - SessionImplementor parameter changed to SharedSessionContractImplementor")
        .withText("`org.hibernate.engine.spi.SessionImplementor` parameter has to be changed to `org.hibernate.engine.spi.SharedSessionContractImplementor`.")
        .withIssueCategory(new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY))
        .with(Link.to("Red Hat JBoss EAP 7.2: Migrating from Hibernate ORM 5.1 to Hibernate ORM 5.3",
            "https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.2/html-single/migration_guide/#hibernate_51_compatiblity_transformer"))
        .withEffort(1)

Quickfix quickfix = new Quickfix();
quickfix.setName("SessionImplementor");
quickfix.setType(QuickfixType.REPLACE);
quickfix.setReplacementStr("SharedSessionContractImplementor");
quickfix.setSearchStr("SessionImplementor");
hint.withQuickfix(quickfix);

ruleSet("hibernate51-53-groovy")
.addSourceTechnology(new TechnologyReference("hibernate", "(,5.1]"))
.addTargetTechnology(new TechnologyReference("hibernate", "[5.3,)"))
.addTargetTechnology(new TechnologyReference("eap", "[7,8)"))
.addRule()
    .when(
        JavaClass.references("{*}.$methodParam({*}org.hibernate.engine.spi.SessionImplementor{*})").at(TypeReferenceLocation.METHOD)
    )
    .perform(
        new ParameterizedIterationOperation<JavaTypeReferenceModel>()
        {
            RegexParameterizedPatternBuilder methodsParameter = new RegexParameterizedPatternBuilder(methodParam);

            public Set<String> getRequiredParameterNames()
            {
                return methodsParameter.getRequiredParameterNames();
            }

            public void setParameterStore(ParameterStore store)
            {
                methodsParameter.setParameterStore(store);
            }

            public void performParameterized(GraphRewrite event, EvaluationContext context, JavaTypeReferenceModel payload)
            {
                final String methodName = methodsParameter.build(event, context);
                final List<String> interfacesAccepted = methodsInterfaces.get(methodName);
                final List<JavaClassModel> classes = payload.getFile().getJavaClasses();
                classes.find{
                    List<JavaClassModel> interfaces = it.getInterfaces();
                    interfaces.each{
                        if (interfacesAccepted.contains(it.getQualifiedName()))
                        {
                            hint.perform(event, context, payload);
                            return true;
                        }
                    };
                    final String classesAccepted = methodsClasses.get(methodName);
                    JavaClassModel extendedClass = it.getExtends();
                    if (extendedClass!= null && classesAccepted != null && classesAccepted == extendedClass.getQualifiedName())
                    {
                        hint.perform(event, context, payload);
                    }
                };
            }

        }
    )
    .where(METHOD_PARAM_NAME).matches(methodParamPattern)
    .withId("hibernate51-53-00000")
