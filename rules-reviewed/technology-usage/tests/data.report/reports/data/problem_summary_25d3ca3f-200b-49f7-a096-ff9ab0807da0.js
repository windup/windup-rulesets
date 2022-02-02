MIGRATION_ISSUES_DETAILS["25d3ca3f-200b-49f7-a096-ff9ab0807da0"] = [
{description: "<p>With JBoss EAP 6+ it is required to implement <code>javax.jms.MessageListener<\/code> or to specify the <code>messageListenerInterface<\/code> attribute in the <code>@MessageDriven<\/code> annotation in the case of a JMS MessageListener.<\/p>", ruleID: "java-glassfish-groovy-01000", issueName: "MDB - Implementation of MessageListener interface is required",
problemSummaryID: "25d3ca3f-200b-49f7-a096-ff9ab0807da0", files: [
{l:"<a class='' href='MessageDrivenBean3_java.html?project=1585384'>org.windup.examples.ejb.messagedriven.MessageDrivenBean3<\/a>", oc:"1"},
{l:"<a class='' href='MessageDrivenBean2_java.html?project=1585384'>org.windup.examples.ejb.messagedriven.MessageDrivenBean2<\/a>", oc:"1"},
], resourceLinks: [
{h:"https://docs.oracle.com/javaee/7/api/javax/ejb/MessageDriven.html", t:"MessageDriven Javadoc JavaEE 7"},
{h:"https://docs.oracle.com/javaee/6/api/javax/ejb/MessageDriven.html", t:"MessageDriven Javadoc JavaEE 6"},
{h:"https://docs.oracle.com/javaee/6/tutorial/doc/bnbpo.html", t:"Java EE 6 Tutorial"},
]},
];
onProblemSummaryLoaded("25d3ca3f-200b-49f7-a096-ff9ab0807da0");