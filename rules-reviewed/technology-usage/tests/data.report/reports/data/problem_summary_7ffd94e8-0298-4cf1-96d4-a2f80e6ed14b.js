MIGRATION_ISSUES_DETAILS["7ffd94e8-0298-4cf1-96d4-a2f80e6ed14b"] = [
{description: "<p><code>java:/jaas/<\/code> is a JBoss EAP <code>security-domain<\/code> URI. Remove the <code>java:/jaas/<\/code> prefix for <code>security-domain<\/code> elements in EAP 7/6.<\/p>", ruleID: "jboss-eap5-7-xml-14000", issueName: "JBoss EAP security-domain configuration - java:/jaas/",
problemSummaryID: "7ffd94e8-0298-4cf1-96d4-a2f80e6ed14b", files: [
{l:"<a class='' href='jboss_xml.html?project=1585384'>clustering/jboss.xml<\/a>", oc:"1"},
], resourceLinks: [
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html-single/development_guide/#java_authentication_spi_for_containers_jaspi", t:"Java Authentication SPI for Containers (JASPI)"},
{h:"http://docs.oracle.com/javase/7/docs/technotes/guides/security/jaas/JAASRefGuide.html", t:"Java Authentication and Authorization Service (JAAS) Reference Guide"},
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html-single/development_guide/#java_authorization_contract_for_containers_jacc", t:"Java Authorization Contract for Containers (JACC)"},
]},
];
onProblemSummaryLoaded("7ffd94e8-0298-4cf1-96d4-a2f80e6ed14b");