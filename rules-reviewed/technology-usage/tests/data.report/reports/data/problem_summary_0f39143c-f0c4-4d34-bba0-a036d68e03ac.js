MIGRATION_ISSUES_DETAILS["0f39143c-f0c4-4d34-bba0-a036d68e03ac"] = [
{description: "<p>If you migrate your application to JBoss EAP 7.3, or later, and want to ensure its Maven building, running or testing works as expected, use instead the Jakarta EE dependency with artifactId <code>jakarta.activation<\/code><\/p>", ruleID: "maven-javax-to-jakarta-00003", issueName: "Move to Jakarta EE Maven Artifacts - replace artifactId activation",
problemSummaryID: "0f39143c-f0c4-4d34-bba0-a036d68e03ac", files: [
{l:"<a class='' href='pom_xml.121.html?project=1585384'>META-INF/maven/org.codehaus.xfire/xfire-core/pom.xml<\/a>", oc:"1"},
{l:"<a class='' href='pom_xml.132.html?project=1585384'>META-INF/maven/org.drools/drools-core/pom.xml<\/a>", oc:"1"},
], resourceLinks: [
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.3/html-single/migration_guide/index#maven-artifact-changes-for-jakarta-ee_default", t:"Red Hat JBoss EAP 7.3 Migration Guide: Maven Artifact Changes for Jakarta EE"},
]},
];
onProblemSummaryLoaded("0f39143c-f0c4-4d34-bba0-a036d68e03ac");