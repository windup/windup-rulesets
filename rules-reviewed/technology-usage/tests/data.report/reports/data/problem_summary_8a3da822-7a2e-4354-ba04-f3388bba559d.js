MIGRATION_ISSUES_DETAILS["8a3da822-7a2e-4354-ba04-f3388bba559d"] = [
{description: "<p>In JBoss EAP, the <code>InitialContext<\/code> should be instantiated with no arguments. Once an instance is constructed, look up the service using portable JNDI lookup syntax. Ensure also that in case system properties for <code>InitialContext<\/code> are provided, they do not need to be changed for the JBoss EAP.<\/p>\n<pre><code class=\"java\">InitialContext context = new InitialContext();\nService service = (Service) context.lookup( &quot;java:app/service/&quot; + ServiceImpl.class.getSimpleName() );\n<\/code><\/pre>", ruleID: "environment-dependent-calls-03000", issueName: "Proprietary InitialContext initialization",
problemSummaryID: "8a3da822-7a2e-4354-ba04-f3388bba559d", files: [
{l:"<a class='' href='Security_java.html?project=1585384'>com.jboss.windup.test.Security<\/a>", oc:"1"},
], resourceLinks: [
{h:"https://access.redhat.com/articles/1496973", t:"Migrate Applications From Other Platforms to Use Portable JNDI Syntax in Red Hat JBoss Enterprise Application Platform"},
]},
];
onProblemSummaryLoaded("8a3da822-7a2e-4354-ba04-f3388bba559d");