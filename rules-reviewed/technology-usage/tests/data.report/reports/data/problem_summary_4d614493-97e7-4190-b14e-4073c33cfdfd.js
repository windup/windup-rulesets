MIGRATION_ISSUES_DETAILS["4d614493-97e7-4190-b14e-4073c33cfdfd"] = [
{description: "<p>Using ConsoleAppender configured in log4j.properties can cause a deadlock on JBoss EAP 6. It is recommended to Remove application level log4j ConsoleAppenders.<\/p>", ruleID: "log4j-03000", issueName: "Log4j ConsoleAppender Configuration - Potential Deadlock",
problemSummaryID: "4d614493-97e7-4190-b14e-4073c33cfdfd", files: [
{l:"<a class='' href='log4j_properties.html?project=1585384'>WEB-INF/classes/log4j.properties<\/a>", oc:"1"},
], resourceLinks: [
{h:"https://access.redhat.com/solutions/375273", t:"EAP 6 deadlocks when using ConsoleHandler and java.io.PrintStream"},
]},
];
onProblemSummaryLoaded("4d614493-97e7-4190-b14e-4073c33cfdfd");