package org.jboss.xavier.integrations.route;

import org.apache.camel.BeanInject;
import org.apache.camel.BindToRegistry;
import org.apache.camel.Consume;
import org.apache.camel.DynamicRouter;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.PropertyInject;
import org.apache.camel.RecipientList;
import org.apache.camel.RoutingSlip;

import java.util.function.BiFunction;

public class Removed
{
    @BeanInject(value = "foo", context = "context-0")
    Object foo;

    @BeanInject("foo2")
    Object foo2;

    @EndpointInject(uri="activemq:foo.bar", context = "context-0")
    ProducerTemplate producerTemplate;

    @EndpointInject(uri="activemq:foo.bar")
    ProducerTemplate producerTemplate2;

    @Produce(uri = "activemq:foo", context = "context-1")
    Object object;

    @Produce(uri = "activemq:foo")
    Object anotherObject;

    @Consume(uri="activemq:cheese", context = "context-2")
    @DynamicRouter(delimiter = ".", context = "context-2")
    public void onCheese(String name) {}

    @Consume(uri="activemq:cheese")
    @DynamicRouter(context = "context-2")
    public void onCheese2(String name) {}

    @PropertyInject(value = "hello", context = "context-3")
    private String greeting;

    @PropertyInject("cheers")
    private String cheers;

    @RecipientList(context = "context-0")
    @RoutingSlip(delimiter = ".")
    public String routeTo() {
        String queueName = "activemq:queue:test2";
        return queueName;
    }

    @RecipientList(streaming = true)
    @RoutingSlip(context = "context-2")
    public String routeTo2() {
        String queueName = "activemq:queue:test2";
        return queueName;
    }
}
