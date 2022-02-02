MIGRATION_ISSUES_DETAILS["e4274496-0b72-4801-bdbf-593627f311dc"] = [
{description: "<p>If you migrate your application to JBoss EAP 7.3, or later, and want to ensure its Maven building, running or testing works as expected, use instead the Jakarta EE dependency - groupId <code>jakarta.enterprise<\/code>.<\/p>", ruleID: "maven-javax-to-jakarta-00004", issueName: "javax.enterprise groupId has been replaced by jakarta.enterprise",
problemSummaryID: "e4274496-0b72-4801-bdbf-593627f311dc", files: [
{l:"<a class='' href='pom_xml.82.html?project=1585384'>META-INF/maven/org.infinispan/infinispan-jcache-commons/pom.xml<\/a>", oc:"1"},
{l:"<a class='' href='pom_xml.86.html?project=1585384'>META-INF/maven/org.infinispan/infinispan-cdi/pom.xml<\/a>", oc:"1"},
], resourceLinks: [
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.3/html-single/migration_guide/index#maven-artifact-changes-for-jakarta-ee_default", t:"Red Hat JBoss EAP 7.3 Migration Guide: Maven Artifact Changes for Jakarta EE"},
]},
];
onProblemSummaryLoaded("e4274496-0b72-4801-bdbf-593627f311dc");