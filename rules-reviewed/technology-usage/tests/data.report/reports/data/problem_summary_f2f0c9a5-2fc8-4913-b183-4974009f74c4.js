MIGRATION_ISSUES_DETAILS["f2f0c9a5-2fc8-4913-b183-4974009f74c4"] = [
{description: "<p>This JSF template includes references to the Seam 2.x tag library. Seam 2.2 and earlier is not supported on JBoss EAP 6 and above.<\/p><p>There are two options available:<\/p>\n<ol>\n <li>Continue using Seam 2.x. This approach is low effort but the application will not use a tested and supported library and it may not work on EAP 7 at all.<\/li>\n <li>Switch to standard CDI beans and migrate to JSF 2.2 UI tags. This will require significant migration effort.<\/li>\n<\/ol>", ruleID: "seam-ui-jsf-00001", issueName: "JSF Seam 2.x tag library usage",
problemSummaryID: "f2f0c9a5-2fc8-4913-b183-4974009f74c4", files: [
{l:"<a class='' href='main_xhtml.html?project=1585384'>web/jsf/main.xhtml<\/a>", oc:"1"},
], resourceLinks: [
{h:"https://access.redhat.com/solutions/2773121", t:"How to use JSF 1.2 with EAP 7?"},
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html-single/migration_guide/#migrate-eap5-component-upgrade-reference", t:"JBoss EAP 5 Component Upgrade Reference"},
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/6.4/html-single/migration_guide/index#sect-JSF_changes", t:"Enable Applications To Use Older Versions of JSF"},
]},
];
onProblemSummaryLoaded("f2f0c9a5-2fc8-4913-b183-4974009f74c4");