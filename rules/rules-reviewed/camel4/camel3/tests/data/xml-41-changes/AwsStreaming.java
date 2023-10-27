import org.apache.camel.builder.RouteBuilder;

public class Aws1Streaming {

    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:listActivities")
                .setHeader("CamelAwsStateMachineOperation",5)
                .setHeader("CamelAwsStateMachinesMaxResults",5)
                .setHeader("CamelAwsStepFunctionsStateMachineActivityName",5)
                .setHeader("CamelAwsStepFunctionsStateMachineActivityArn",5)
                .setHeader("CamelAwsStateMachineActivitiesMaxResults",5)
                .setHeader("CamelAwsStateMachineExecutionArn",5)
                .setHeader("CamelAwsStateMachineExecutionName",5)
                .setHeader("CamelAwsStateMachineExecutionInput",5)
                .setHeader("CamelAwsStateMachineExecutionTraceHeader",5)
                .setHeader("CamelAwsStateMachineExecutionHistoryMaxResults",5)
                .setHeader("CamelAwsStateMachineExecutionHistoryIncludeExecutionData",5)
                .setHeader("CamelAwsStateMachineExecutionHistoryReverseOrder",5)
                .setHeader("CamelAwsStateMachineExecutionMaxResults",5)
                 .to("aws2-step-functions://test?awsSfnClient=#awsSfnClient&operation=listActivities");
            }
        };
    }

}
