MIGRATION_ISSUES_DETAILS["71f4e327-a93b-49df-96d4-d6ca637f2115"] = [
{description: "<p>The application has a Seam library embedded.<\/p><p>While official support for Seam 2.2 applications was dropped in JBoss EAP 6, it was still possible to configure dependencies for JSF 1.2 and Hibernate 3 to allow Seam 2.2 applications to run on that release.<\/p><p>Seam 2.3 should work on JBoss EAP 6 even some framework features and integrations from Seam 2.2 are not supported.<\/p><p>Red Hat JBoss EAP 7, which now includes JSF 2.2 and Hibernate 5, does not support Seam 2.2 or Seam 2.3 due to end of life of Red Hat JBoss Web Framework Kit. It is recommended that you rewrite your Seam components using CDI beans. In the links below there are the instructions to enable alternatives for both EAP 6 and 7<\/p>", ruleID: "embedded-framework-libraries-06000", issueName: "Seam 2 embedded library",
problemSummaryID: "71f4e327-a93b-49df-96d4-d6ca637f2115", files: [
{l:"data/non-xml/jboss-seam-2.jar", oc:"1"},
], resourceLinks: [
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/6.4/html-single/migration_guide/index#sect-Migrate_Seam_2.2_Applications", t:"EAP 6 - Migrate Seam 2.2 applications"},
{h:"https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_Web_Framework_Kit/2.7/html-single/Seam_Guide/index.html#migration23", t:"Red Hat JBoss EAP 6: Migration from 2.2 to 2.3"},
{h:"https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_Web_Framework_Kit/2.7/html-single/Seam_Guide/index.html#idm54350960", t:"Red Hat JBoss EAP: Migration from Seam 2 to Java EE and alternatives"},
{h:"https://access.redhat.com/solutions/2773121", t:"How to use JSF 1.2 with EAP 7?"},
]},
];
onProblemSummaryLoaded("71f4e327-a93b-49df-96d4-d6ca637f2115");