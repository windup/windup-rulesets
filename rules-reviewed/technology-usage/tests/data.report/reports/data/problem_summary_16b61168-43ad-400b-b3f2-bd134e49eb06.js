MIGRATION_ISSUES_DETAILS["16b61168-43ad-400b-b3f2-bd134e49eb06"] = [
{description: "<p>The <code>clustered<\/code> element is ignored in EAP 7 and is not useful. In case the application is started using HA profile, the replication will be done automatically.<\/p>", ruleID: "jboss-eap5and6to7-xml-37000", issueName: "Stateful Session EJB Clustering configuration changes in EAP 7",
problemSummaryID: "16b61168-43ad-400b-b3f2-bd134e49eb06", files: [
{l:"<a class='' href='jboss_ejb3_xml.html?project=1585384'>non-xml/jboss-ejb3.xml<\/a>", oc:"1"},
{l:"<a class='' href='jboss_ejb3_xml.1.html?project=1585384'>clustering/jboss-ejb3.xml<\/a>", oc:"1"},
], resourceLinks: [
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/single/migration_guide/#migrate_stateful_session_ejb_clustering_changes", t:"Clustered annotation in EAP 7"},
]},
];
onProblemSummaryLoaded("16b61168-43ad-400b-b3f2-bd134e49eb06");