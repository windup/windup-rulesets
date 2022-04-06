package org.acme.dynamodb;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@ApplicationScoped
public class FruitAsyncService3 extends AbstractService {

    @Inject
    DynamoDbAsyncClient dynamoDB;

    public Uni<List<Fruit>> findAll() {
        return Uni.createFrom().completionStage(() -> dynamoDB.scan(scanRequest()))
                .onItem().transform(res -> res.items().stream().map(Fruit::from).collect(Collectors.toList()));
    }

    public Uni<List<Fruit>> add(Fruit fruit) {
        return Uni.createFrom().completionStage(() -> dynamoDB.putItem(putRequest(fruit)))
                .onItem().ignore().andSwitchTo(this::findAll);
    }

    public Uni<Fruit> get(String name) {
        return Uni.createFrom().completionStage(() -> dynamoDB.getItem(getRequest(name)))
                .onItem().transform(resp -> Fruit.from(resp.item()));
    }

    public void testRule() {
        Multi<Long> multiLong = Multi.createFrom().ticks().every(Duration.ofMillis(500));
        multiLong.collectItems();
        
        Multi<Object> multiObject = Multi.createFrom().ticks().every(Duration.ofMillis(500));
        Multi multiEmpty = Multi.createFrom().ticks().every(Duration.ofMillis(500));

        multiObject.collectItems();
        multiEmpty.collectItems();
        
        multiLong.groupItems();
        multiLong.groupItems();
        multiLong.groupItems();
        
        multiLong.transform().toHotStream();
        multiLong.transform().toHotStream();
        
        multiLong.subscribeOn(new Object());
        multiLong.subscribeOn(new Object());
        multiLong.subscribeOn(new Object());
        
        Uni uniLong = Uni.createFrom().nothing();
        uniLong.subscribeOn(new Object());
        uniLong.subscribeOn(new Object());
        uniLong.subscribeOn(new Object());

        multiLong.transform().first();
        multiLong.transform().filter(myFilter);
        multiLong.transform().test(myTest);
    }
}
