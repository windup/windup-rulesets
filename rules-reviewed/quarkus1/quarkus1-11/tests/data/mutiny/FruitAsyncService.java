package org.acme.dynamodb;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.smallrye.mutiny.Multi;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@ApplicationScoped
public class FruitAsyncService extends AbstractService {

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
        
        Multi<Object> multiObject = (Multi<Object>) Multi.createFrom().empty();
        Multi multiEmpty = Multi.createFrom().empty();
        
        multiLong.collectItems();
        multiObject.collectItems();
        multiEmpty.collectItems();

        multiLong.groupItems();
        multiObject.groupItems();
        multiEmpty.groupItems();
        multiEmpty.groupItems();

        multiLong.transform().toHotStream();
        multiLong.transform().toHotStream();
        multiObject.transform().toHotStream();
        multiObject.transform().toHotStream();
        multiEmpty.transform().toHotStream();

        multiLong.subscribeOn(Executors.newSingleThreadExecutor());
        multiLong.subscribeOn(Executors.newSingleThreadExecutor());
        multiLong.subscribeOn(Executors.newSingleThreadExecutor());

        multiLong.transform().byFilteringItemsWith(e -> e == 10L);
        multiObject.transform().byTakingFirstItems(3L);
        multiObject.transform().bySkippingFirstItems(3L);
        multiEmpty.transform().byTestingItemsWith(e -> Uni.createFrom().item(Boolean.TRUE));
    }
}
