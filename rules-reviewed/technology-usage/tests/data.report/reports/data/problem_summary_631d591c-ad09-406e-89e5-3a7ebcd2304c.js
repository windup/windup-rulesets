MIGRATION_ISSUES_DETAILS["631d591c-ad09-406e-89e5-3a7ebcd2304c"] = [
{description: "<p>If you migrate your application to JBoss EAP 7.3, or later, and want to ensure its Maven building, running or testing works as expected, use instead the Jakarta EE dependency - groupId <code>jakarta.inject<\/code>.<\/p>", ruleID: "maven-javax-to-jakarta-00004", issueName: "javax.inject groupId has been replaced by jakarta.inject",
problemSummaryID: "631d591c-ad09-406e-89e5-3a7ebcd2304c", files: [
{l:"<a class='' href='pom_xml.132.html?project=1585384'>META-INF/maven/org.drools/drools-core/pom.xml<\/a>", oc:"1"},
], resourceLinks: [
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.3/html-single/migration_guide/index#maven-artifact-changes-for-jakarta-ee_default", t:"Red Hat JBoss EAP 7.3 Migration Guide: Maven Artifact Changes for Jakarta EE"},
]},
];
onProblemSummaryLoaded("631d591c-ad09-406e-89e5-3a7ebcd2304c");