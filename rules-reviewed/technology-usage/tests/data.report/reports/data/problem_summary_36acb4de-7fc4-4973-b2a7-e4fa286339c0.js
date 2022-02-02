MIGRATION_ISSUES_DETAILS["36acb4de-7fc4-4973-b2a7-e4fa286339c0"] = [
{description: "<p>WebSphere Webservice Extension XML Deployment Descriptor.<br/>This deployment descriptor extension is IBM specific and it needs to be migrated to JBossWS.<br/>JBossWS implements the latest JAX-WS specification, which users can reference for any vendor-agnostic web service usage need.<br/>You can migrate deployment descriptors following the links below. <\/p>", ruleID: "ResolveWebSphereWsExtensionXmlRuleProvider_1", issueName: "WebSphere web service extension descriptor (ibm-webservices-ext)",
problemSummaryID: "36acb4de-7fc4-4973-b2a7-e4fa286339c0", files: [
{l:"<a class='' href='ibm_webservices_ext_xmi.html?project=1585384'>META-INF/ibm-webservices-ext.xmi<\/a>", oc:"1"},
], resourceLinks: [
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html-single/developing_web_services_applications/#ws_endpoint_assign_config", t:"Assigning Client and Endpoint Configurations (JBoss EAP 7)"},
{h:"https://access.redhat.com/documentation/en-US/JBoss_Enterprise_Application_Platform/6.4/html/Development_Guide/chap-JAX-WS_Web_Services.html", t:"JAX-WS Web Services (JBoss EAP 6)"},
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html/developing_web_services_applications/developing_jax_ws_web_services", t:"Developing JAX-WS Web Services (JBoss EAP 7)"},
]},
];
onProblemSummaryLoaded("36acb4de-7fc4-4973-b2a7-e4fa286339c0");