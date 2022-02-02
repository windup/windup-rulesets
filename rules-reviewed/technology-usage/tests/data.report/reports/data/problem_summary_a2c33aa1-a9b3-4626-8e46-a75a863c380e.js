MIGRATION_ISSUES_DETAILS["a2c33aa1-a9b3-4626-8e46-a75a863c380e"] = [
{description: "<p>If you migrate your application to JBoss EAP 7.3, or later, and want to ensure its Maven building, running or testing works as expected, use instead the Jakarta EE dependency with groupId <code>com.sun.activation<\/code><\/p>", ruleID: "maven-javax-to-jakarta-00002", issueName: "Move to Jakarta EE Maven Artifacts - replace groupId javax.activation",
problemSummaryID: "a2c33aa1-a9b3-4626-8e46-a75a863c380e", files: [
{l:"<a class='' href='pom_xml.121.html?project=1585384'>META-INF/maven/org.codehaus.xfire/xfire-core/pom.xml<\/a>", oc:"1"},
{l:"<a class='' href='pom_xml.132.html?project=1585384'>META-INF/maven/org.drools/drools-core/pom.xml<\/a>", oc:"1"},
], resourceLinks: [
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.3/html-single/migration_guide/index#maven-artifact-changes-for-jakarta-ee_default", t:"Red Hat JBoss EAP 7.3 Migration Guide: Maven Artifact Changes for Jakarta EE"},
]},
];
onProblemSummaryLoaded("a2c33aa1-a9b3-4626-8e46-a75a863c380e");