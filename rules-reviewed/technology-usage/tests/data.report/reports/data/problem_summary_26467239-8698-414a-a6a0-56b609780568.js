MIGRATION_ISSUES_DETAILS["26467239-8698-414a-a6a0-56b609780568"] = [
{description: "<p>If you migrate your application to JBoss EAP 7.3 (or later), and want to ensure its Maven building, running or testing works as expected, use instead the Jakarta EE dependency with groupId <code>org.jboss.spec.javax.transaction<\/code>, and artifactId <code>jboss-transaction-api_1.3_spec<\/code>.<\/p>", ruleID: "maven-javax-to-jakarta-00011", issueName: "Move to Jakarta EE Maven Artifacts - org.jboss.spec.javax.transaction:jboss-transaction-api_1.2_spec",
problemSummaryID: "26467239-8698-414a-a6a0-56b609780568", files: [
{l:"<a class='' href='pom_xml.120.html?project=1585384'>META-INF/maven/org.jbpm/jbpm-bpmn2/pom.xml<\/a>", oc:"1"},
{l:"<a class='' href='pom_xml.110.html?project=1585384'>META-INF/maven/org.hibernate.ogm/hibernate-ogm-core/pom.xml<\/a>", oc:"1"},
], resourceLinks: [
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.3/html-single/migration_guide/index#maven-artifact-changes-for-jakarta-ee_default", t:"Red Hat JBoss EAP 7.3 Migration Guide: Maven Artifact Changes for Jakarta EE"},
]},
];
onProblemSummaryLoaded("26467239-8698-414a-a6a0-56b609780568");