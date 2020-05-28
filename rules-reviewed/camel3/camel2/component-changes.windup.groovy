package camel3.camel2

import jdk.internal.org.objectweb.asm.TypeReference
import org.apache.commons.lang3.StringUtils
import org.jboss.windup.ast.java.data.TypeReferenceLocation
import org.jboss.windup.config.GraphRewrite
import org.jboss.windup.config.metadata.TechnologyReference
import org.jboss.windup.config.operation.Iteration
import org.jboss.windup.config.operation.iteration.AbstractIterationOperation
import org.jboss.windup.config.query.Query
import org.jboss.windup.config.query.QueryPropertyComparisonType
import org.jboss.windup.graph.model.resource.FileModel
import org.jboss.windup.reporting.category.IssueCategory
import org.jboss.windup.reporting.category.IssueCategoryRegistry
import org.jboss.windup.reporting.config.Hint
import org.jboss.windup.reporting.config.HintEffort
import org.jboss.windup.reporting.config.Link
import org.jboss.windup.reporting.model.QuickfixType
import org.jboss.windup.reporting.quickfix.Quickfix
import org.jboss.windup.rules.apps.java.condition.JavaClass
import org.jboss.windup.rules.apps.java.model.JavaClassFileModel
import org.jboss.windup.rules.apps.java.model.JavaSourceFileModel
import org.jboss.windup.rules.apps.java.scan.ast.JavaTypeReferenceModel
import org.jboss.windup.rules.apps.xml.condition.XmlFile
import org.jboss.windup.rules.apps.xml.model.XmlFileModel
import org.jboss.windup.rules.apps.xml.model.XmlTypeReferenceModel
import org.jboss.windup.rules.files.condition.FileContent
import org.ocpsoft.rewrite.config.And
import org.ocpsoft.rewrite.config.Condition
import org.ocpsoft.rewrite.config.ConditionBuilder
import org.ocpsoft.rewrite.config.Not
import org.ocpsoft.rewrite.config.Or
import org.ocpsoft.rewrite.config.True
import org.ocpsoft.rewrite.context.EvaluationContext

final String telegramRegex = "(telegram:)(?!.*authorizationToken.*)"
final String regexReference = "{regex}"

final String regex = "\"(?.*outBody.*)\""

final IssueCategory optionalIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.OPTIONAL)
final IssueCategory mandatoryIssueCategory = new IssueCategoryRegistry().getByID(IssueCategoryRegistry.MANDATORY)

Hint createHint(String title, String messase, String linkAppendix, String issueCategory) {
    final String docTitleAppendix = (linkAppendix.substring(0, 1).toUpperCase() + linkAppendix.substring(1))
            .replaceAll("_", " ")

    HintEffort hint = Hint.titled(title)
            .withText(messase)
            .with(Link.to("Camel 3 - Migration Guide: $docTitleAppendix", "https://camel.apache.org/manual/latest/camel-3-migration-guide.html#_$linkAppendix"))
            .withEffort(1)

    ((Hint) hint).withIssueCategory(new IssueCategoryRegistry().getByID(issueCategory))
    return hint
}

Hint createHint(String title, String messase, String linkAppendix, boolean mandatory) {
    HintEffort hint
    if (mandatory) {
        return createHint(title, messase, linkAppendix, IssueCategoryRegistry.MANDATORY)
    } else {
        return createHint(title, messase, linkAppendix, IssueCategoryRegistry.POTENTIAL)
    }
}

Hint createHint(String title, String messase, String linkAppendix, boolean mandatory, Quickfix quickfix) {
    return createHint(title, messase, linkAppendix, mandatory).withQuickfix(quickfix);
}

boolean has(String input) {
    if (input.contains("="))
        if (input.substring(input.indexOf("("), input.lastIndexOf(")")).split(",").length == 1)
            return true
    return false
}

Hint createSimpleLanguageRemovedHint(String removed) {
    return createHint("Simple language: out.{params} has been removed",
            "`$removed` has been removed from simple language",
            "out_message_removed_from_simple_language_and_mock_component", true)
}

Quickfix createReplaceQuickfix(String replace, String replaceWith) {
    Quickfix q = new Quickfix()
    q.setType(QuickfixType.REPLACE)
    q.setSearchStr(replace)
    q.setReplacementStr(replaceWith)
    return q;
}

Hint createMovedClassHint(String moved, String movedTo, String addInfo, IssueCategory issueCategory) {
    Quickfix q = createReplaceQuickfix(mmoved, movedTo)
    return createHint("$moved was moved", "`$moved` was moved to `$movedTo` ", "eips", true, q)
}

ruleSet("component-changes-groovy")
        .addSourceTechnology(new TechnologyReference("camel", "[2,3)"))
        .addTargetTechnology(new TechnologyReference("camel", "[3,)"))
        .addRule()
        .when(Or.any(
                FileContent
                        .matches("$regexReference")
                        .inFileNamed("{*}.java"),
                XmlFile.matchesXpath("//*/c:route/*[contains(@uri ,'telegram') and not(contains(@uri,'authorizationToken'))]")
                        .namespace("c", "http://camel.apache.org/schema/spring")
        ))
        .perform(
                Iteration.over()
                        .perform(createHint("[camel-telegram] Authorization token parameter required!",
                                "Authorization token was moved from uri-path to a query parameter. " +
                                        "Use `telegram:bots?authorizationToken=myTokenHere`",
                                "telegram", true)
                        ))
        .where("regex").matches(telegramRegex)
        .withId("component-changes-00001")

        .addRule()
        .when(JavaClass.references("org.apache.camel.component.shiro.security.ShiroSecurityPolicy{*}"))
        .perform(new AbstractIterationOperation<JavaTypeReferenceModel>() {
            void perform(GraphRewrite event, EvaluationContext context, JavaTypeReferenceModel payload) {

                Hint h = createHint("Shiro - default encryption key was removed",
                        "It's mandatory to specify key/passphrase for `ShiroSecurityPolicy`.", "shiro_component", true)

                final String input = payload.sourceSnippit
                if (input.contains("=")
                        && (input.substring(input.indexOf("("), input.lastIndexOf(")")).split(",").length == 1)) {
                    h.perform(event, context, payload)
                }
            }
        })
        .withId("component-changes-00002")

        .addRule()
        .when(FileContent.matches("{*}.{method}{*}").inFileNamed("{*}.java"))
        .perform(createHint("Mock: {method} has been removed",
                "`{method}` has been removed from the mock component's assertion api.",
                "out_message_removed_from_simple_language_and_mock_component", true))
        .where("method").matches("(outBody|outHeaders)")
        .withId("component-changes-00003")

        .addRule()
        .when(JavaClass.references("org.apache.camel.OutHeaders").at(TypeReferenceLocation.ANNOTATION, TypeReferenceLocation.IMPORT))

        .perform(createHint("@OutHeaders annotation has been removed",
                "`@OutHeaders` annotation has been removed. Use `@Headers` instead",
                "out_message_removed_from_simple_language_and_mock_component", true))
        .withId("component-changes-00004")

        .addRule()
        .when(XmlFile.matchesXpath("//*/c:simple[text()=windup:matches(self::node(), '{*}out.{params}{*}')]")
                .namespace("c", "http://camel.apache.org/schema/spring"))
        .perform(createSimpleLanguageRemovedHint("out.{params}"))
        .where("params").matches("(body|header)")
        .withId("component-changes-00005")

        .addRule()
        .when(XmlFile.matchesXpath("//*/b:simple[text()=windup:matches(self::node(), '{*}out.{params}{*}')]")
                .namespace("b", "http://camel.apache.org/schema/blueprint"))
        .perform(createSimpleLanguageRemovedHint("out.{params}"))
        .where("params").matches("(body|header)")
        .withId("component-changes-00006")

        .addRule()
        .when(FileContent.matches("{*}simple({*}out.{params}{*})").inFileNamed("{*}.java"))
        .perform(createSimpleLanguageRemovedHint("out.{params}"))
        .where("params").matches("(body|header)")
        .withId("component-changes-00007")

        .addRule()
        .when(Or.any(
                XmlFile.matchesXpath("//*/c:simple[text()=windup:matches(self::node(), '{*}property{*}')]")
                        .namespace("c", "http://camel.apache.org/schema/spring"),
                XmlFile.matchesXpath("//*/b:simple[text()=windup:matches(self::node(), '{*}property{*}')]")
                        .namespace("b", "http://camel.apache.org/schema/blueprint"),
                FileContent.matches("{*}simple({*}property{*})").inFileNamed("{*}.java"))
        )
        .perform(createHint("Simple language: property function has been removed",
                "`property` function has been removed from simple language. Use `exchangeProperty` instead.",
                "languages", true))
        .withId("component-changes-00008")

        .addRule()
        .when(Or.any(JavaClass.references("org.apache.camel.component.hl7.HL7.terser").at(TypeReferenceLocation.IMPORT).as("terser"),
                FileContent.from("terser").matches("{*}terser({*}"))
        )
        .perform(Iteration.over("default").perform(
                createHint("Terser language: language renamed",
                        "`terser` language renamed to `hl7terser`",
                        "languages", true)
                        .withQuickfix(createReplaceQuickfix("org.apache.camel.component.hl7.HL7.terser",
                                "org.apache.camel.component.hl7.HL7.hl17terser"))))
        .withId("component-changes-00008")

        .addRule()
        .when(Or.any(JavaClass.references("org.apache.camel.converter.crypto.CryptoDataFormat{*}").at(TypeReferenceLocation.VARIABLE_DECLARATION),
                XmlFile.matchesXpath("//*[c:crypto and not(b:crypto/@algorithm)]/c:crypto").namespace("c", "http://camel.apache.org/schema/spring"),
                XmlFile.matchesXpath("//*[b:crypto and not(b:crypto/@algorithm)]/b:crypto").namespace("b", "http://camel.apache.org/schema/blueprint"))
        )
        .perform(createHint("Crypto dataformat: The default encryption algorithm changed to null",
                "The default encryption algorithm is mandatory changed from `DES/CBC/PKCS5Padding` to null. " +
                        "Therefore if algorithm hasn't been set yet, it's required to set a value for it",
                "crypto_dataformat", false))
        .withId("component-changes-00009")

        .addRule()
        .when(Or.any(
                XmlFile.matchesXpath("//*[c:secureXML and (count(c:secureXML/@passPhrase)+count(c:secureXML/@passPhraseByte))<1]/c:secureXML/")
                        .namespace("c", "http://camel.apache.org/schema/spring"),
                XmlFile.matchesXpath("//*[b:secureXML and (count(b:secureXML/@passPhrase)+count(b:secureXML/@passPhraseByte))<1]/b:secureXML/")
                        .namespace("b", "http://camel.apache.org/schema/blueprint"),
                FileContent.matches("{*}secureXML").inFileNamed("{*}.java"))
        )
        .perform(createHint("XMLsecure dataformat: The default encryption key has been removed",
                "The default encryption key has been removed, so it is now mandatory to supply " +
                        "the key String/bytes if you are using symmetric encryption.",
                "xml_security_dataformat", false))
        .withId("component-changes-00010")

        .addRule()
        .when(Or.any(
                XmlFile.matchesXpath("//*/c:from[@uri=windup:matches(self::node(), '{*}consumer.{*}')]")
                        .namespace("c", "http://camel.apache.org/schema/spring"),
                XmlFile.matchesXpath("//*/b:from[@uri=windup:matches(self::node(), '{*}consumer.{*}')]")
                        .namespace("b", "http://camel.apache.org/schema/blueprint"),
                FileContent.matches("{*}from({*}consumer.{*})").inFileNamed("{*}.java"))
        )
        .perform(createHint("Consumer endpoints: options with consumer. prefix have been removed. ",
                "Consumer.options with `consumer.` prefix have been removed. Use options without the prefix" +
                        "i.e `delay` instead of `consumer.delay`",
                "using_endpoint_options_with_consumer_prefix", true))
        .withId("component-changes-00011")

        .addRule()
        .when(Or.any(
                XmlFile.matchesXpath("//*[@class='org.apache.camel.processor.interceptor.Tracer']"),
                 JavaClass.references("org.apache.camel.processor.interceptor.Tracer").at(TypeReferenceLocation.IMPORT)
        ))
        .perform(createHint("Tracing: Tracer class removed",
                "`org.apache.camel.processor.interceptor.Tracer` class has been removed and replaced by `org.apache.camel.Tracing`." +
                        "See the documentation:",
                "tracing", true)
                .with(Link.to("Tracer in Camel 3","https://camel.apache.org/manual/latest/tracer.html"))
                .withEffort(2))
        .withId("component-changes-00012")

        .addRule()
        .when(Or.any(
                XmlFile.matchesXpath("//*[@class='org.apache.camel.processor.interceptor.DefaultTraceFormatter']"),
                 JavaClass.references("org.apache.camel.processor.interceptor.DefaultTraceFormatter").at(TypeReferenceLocation.IMPORT)
        ))
        .perform(createHint("Tracing: DefaultTraceFormatter formatter removed",
                "`org.apache.camel.processor.interceptor.DefaultTraceFormatter` class has been removed. " +
                        "Use `ExchangeFormatter` as described in the documentation:",
                "tracing", true)
                .with(Link.to("Tracer in Camel 3","https://camel.apache.org/manual/latest/tracer.html"))
                .withEffort(2))
        .withId("component-changes-00013")

        .addRule()
        .when( XmlFile.matchesXpath("/m:project/m:dependencies[m:dependency/m:artifactId/text() = 'camel-core']")
                .inFile("pom.xml").namespace("m", "http://maven.apache.org/POM/4.0.0"))
        .perform(createHint("Tracing:  BacklogTracer is no longer enabled by default in JMX",
                "`BacklogTracer` is no longer enabled by default in JMX. For using BacklogTracer " +
                        "you need to enable by setting `backlogTracing=true` on CamelContext.",
                "tracing", false))
        .withId("component-changes-00014")

        .addRule()
        .when(Or.any(
                XmlFile.matchesXpath("//*/c:to[contains(@uri,'xmlsecurity')]")
                        .namespace("c", "http://camel.apache.org/schema/spring"),
                XmlFile.matchesXpath("//*/b:to[contains(@uri,'xmlsecurity')]")
                        .namespace("b", "http://camel.apache.org/schema/blueprint"),
                FileContent.matches(".to({*}xmlsecurity{*}").inFileNamed("{*}.java")//
        )
        )
        .perform(createHint("XMLSecurity component: The default signature algorithm has changed",
                "The default signature algorithm in the XMLSecurity component has changed `SHA1WithDSA` to `SHA256withRSA`. ",
                "using_endpoint_options_with_consumer_prefix", IssueCategoryRegistry.INFORMATION))
        .withId("component-changes-00015")

        .addRule()
        .when(Or.any(
                XmlFile.matchesXpath("//*/c:route/*[contains(@uri,'crypto:')]")
                        .namespace("c", "http://camel.apache.org/schema/spring"),
                XmlFile.matchesXpath("//*/b:route/*[contains(@uri,'crypto:')]")
                        .namespace("b", "http://camel.apache.org/schema/blueprint"),
                FileContent.matches(".to({*}crypto:{*}").inFileNamed("{*}.java"))
        )
        .perform(createHint("Crypto component: The default signature algorithm has changed",
                "The default signature algorithm in the Crypto component has changed from `SHA1WithDSA` to `SHA256withRSA`.",
                "using_endpoint_options_with_consumer_prefix", IssueCategoryRegistry.INFORMATION))
        .withId("component-changes-00016")
