import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.parameters.ParameterizedIterationOperation
import org.jboss.windup.config.metadata.TechnologyReference
import org.ocpsoft.rewrite.context.EvaluationContext
import org.ocpsoft.rewrite.param.ParameterStore

import org.jboss.windup.ast.java.data.TypeReferenceLocation
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.rules.apps.java.condition.JavaClass
import org.jboss.windup.rules.apps.java.scan.ast.annotations.JavaAnnotationTypeReferenceModel
import org.jboss.windup.rules.apps.java.scan.ast.annotations.JavaAnnotationTypeValueModel
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.xml.HintHandler

import org.jboss.windup.util.Logging

ruleSet("hsearch-groovy")
        .addSourceTechnology(new TechnologyReference("hibernate", "[4,)"))
        .addSourceTechnology(new TechnologyReference("eap", "[6,7)"))
        .addTargetTechnology(new TechnologyReference("hibernate", "[5,)"))
        .addTargetTechnology(new TechnologyReference("eap", "[7,8)"))
        .addRule()
        .when(
            JavaClass.references("org.hibernate.search.annotations.{bridge}").at(TypeReferenceLocation.ANNOTATION)
        )
        .perform(
        new ParameterizedIterationOperation<JavaAnnotationTypeReferenceModel> () {
            Hint hint = Hint
                    .titled("Hibernate Search 5 - Changes in indexing date values")
                    .withText(
                    """Date and Calendar values are no longer indexed as strings. Instead, instances are encoded as long values representing the number
of milliseconds since January 1, 1970, 00:00:00 GMT. You can switch the indexing format by using the new EncodingType enum. For example:

```java
@DateBridge(encoding=EncodingType.STRING)
@CalendarBridge(encoding=EncodingType.STRING)
```

The encoding change for dates is important and can have a big impact on application behavior. If you have
a query that targets a field that was previously string-encoded, but is now encoded numerically, you must update the query.
You must also make sure that all fields targeted by faceting are string encoded.
If you use the Search query DSL, the correct query should be created automatically for you.""")
                    .with(Link.to("Number and Date Index Formatting Changes in Hibernate Search 5.x", "https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/single/migration_guide/#migrate_hibernate_search_number_and_date_index_formatting_changes"))
                    .with(Link.to("Number and date index format", "http://hibernate.org/search/documentation/migrate/5.0/#number-and-date-index-format"))
                    .with(Link.to("Javadoc API for org.hibernate.search.bridge.builtin package", "http://docs.jboss.org/hibernate/search/5.5/api/org/hibernate/search/bridge/builtin/package-summary.html"))
                    .withTags(new HashSet<>(Arrays.asList("hibernate-search")))
                    .withEffort(0);

            public Set<String> getRequiredParameterNames() {
                return hint.getRequiredParameterNames();
            }

            public void setParameterStore(ParameterStore store) {
                hint.setParameterStore(store);
            }

            public void performParameterized(final GraphRewrite event, final EvaluationContext context, final JavaAnnotationTypeReferenceModel locationModel) {
                boolean hasEncodingElement = false;
                    Map<String, JavaAnnotationTypeValueModel> annotationValues = locationModel.getAnnotationValues();
                    if (annotationValues != null && annotationValues.containsKey("encoding")) {
                        hasEncodingElement = true;
                    }
                if (!hasEncodingElement) {
                    IssueCategoryRegistry issueCategoryRegistry = IssueCategoryRegistry.instance(context)
                    IssueCategory issueCategory = issueCategoryRegistry.getByID("potential")
                    hint.withIssueCategory(issueCategory).perform(event, context)
                }
            }
        }
        )
        .where("bridge").matches("DateBridge|CalendarBridge")
        .withId("hsearch-groovy-01000")
