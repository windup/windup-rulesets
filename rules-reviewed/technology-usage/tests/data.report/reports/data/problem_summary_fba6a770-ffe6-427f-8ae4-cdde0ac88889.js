MIGRATION_ISSUES_DETAILS["fba6a770-ffe6-427f-8ae4-cdde0ac88889"] = [
{description: "<p>This method lookups an object using a JNDI String. During the migration process, some entity JNDI bindings may change. Ensure that the JNDI Name does not need to change for JBoss EAP.<\/p><p><em>For Example:<\/em><\/p>\n<pre><code class=\"java\">(ConnectionFactory)initialContext.lookup(&quot;weblogic.jms.ConnectionFactory&quot;);\n<\/code><\/pre><p><em>should become:<\/em><\/p>\n<pre><code class=\"java\">(ConnectionFactory)initialContext.lookup(&quot;/ConnectionFactory&quot;);\n<\/code><\/pre>", ruleID: "environment-dependent-calls-02000", issueName: "Call of JNDI lookup",
problemSummaryID: "fba6a770-ffe6-427f-8ae4-cdde0ac88889", files: [
{l:"<a class='' href='Security_java.html?project=1585384'>com.jboss.windup.test.Security<\/a>", oc:"1"},
{l:"<a class='' href='XAExceptionSessionBean_java.html?project=1585384'>org.jboss.test.jca.ejb.XAExceptionSessionBean<\/a>", oc:"7"},
], resourceLinks: [
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/6.4/html-single/migration_guide/#sect-JNDI_Changes", t:"JNDI Changes"},
{h:"https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/6.4/html-single/development_guide/#chap-Remote_JNDI_Lookup", t:"JBoss EAP 6 - JNDI Reference"},
]},
];
onProblemSummaryLoaded("fba6a770-ffe6-427f-8ae4-cdde0ac88889");