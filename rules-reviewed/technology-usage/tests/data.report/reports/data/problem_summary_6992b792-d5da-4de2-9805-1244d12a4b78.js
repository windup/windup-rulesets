MIGRATION_ISSUES_DETAILS["6992b792-d5da-4de2-9805-1244d12a4b78"] = [
{description: "<p>WebSphere Enterprise Java Bean Extension XML Descriptor is used to specify extensions to be (de-)activated in the EJB Container. JBoss EAP uses Java EE <code>jboss-ejb.xml<\/code> file descriptor or EAP specific <code>jboss-ejb3.xml<\/code> descriptor file. EJB 3.2 doesn\'t require descriptor file to be in deployment.<\/p>", ruleID: "ResolveWebSphereEjbExtensionXmlRuleProvider_1", issueName: "WebSphere EJB extension descriptor (ibm-ejb-jar-ext)",
problemSummaryID: "6992b792-d5da-4de2-9805-1244d12a4b78", files: [
{l:"<a class='' href='ibm_ejb_jar_ext_xmi.html?project=1585384'>META-INF/ibm-ejb-jar-ext.xmi<\/a>", oc:"1"},
], resourceLinks: [
{h:"https://access.redhat.com/documentation/en-US/JBoss_Enterprise_Application_Platform/6/html-single/Development_Guide/index.html#jboss-ejb3xml_Deployment_Descriptor_Reference", t:"EAP 6 - jboss-ejb3.xml Deployment Descriptor Reference"},
{h:"https://www.ibm.com/support/knowledgecenter/en/SSAW57_7.0.0/com.ibm.websphere.nd.doc/info/ae/ae/cejb_bindingsejbfp.html", t:"Websphere AS - EJB 3.0 application bindings overview"},
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html-single/developing_ejb_applications/#jboss_ejb3_xml_deployment_descriptor_reference", t:"EAP 7 - jboss-ejb3.xml Deployment Descriptor Reference"},
]},
];
onProblemSummaryLoaded("6992b792-d5da-4de2-9805-1244d12a4b78");