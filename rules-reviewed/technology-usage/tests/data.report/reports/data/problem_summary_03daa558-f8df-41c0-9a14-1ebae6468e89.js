MIGRATION_ISSUES_DETAILS["03daa558-f8df-41c0-9a14-1ebae6468e89"] = [
{description: "<p>CMP entity beans are no longer supported in JBoss EAP 7. User is requested to use JPA entities that fully replaced the functionality provided by CMP beans. CMP configuration provided in this ejb-jar.xml should be configured using JPA persistence.xml or using JPA annotations.<\/p>", ruleID: "jboss-eap5and6to7-xml-31000", issueName: "CMP Entity EJB configuration",
problemSummaryID: "03daa558-f8df-41c0-9a14-1ebae6468e89", files: [
{l:"<a class='' href='ejb_jar_xml.html?project=1585384'>ejb/ejb-jar.xml<\/a>", oc:"1"},
], resourceLinks: [
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/single/migration_guide/#migrate_entity_beans_to_jpa", t:"CMP Bean migration"},
]},
];
onProblemSummaryLoaded("03daa558-f8df-41c0-9a14-1ebae6468e89");