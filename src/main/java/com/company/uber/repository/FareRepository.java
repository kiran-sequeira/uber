package com.company.uber.repository;

import com.company.uber.model.Fare;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class FareRepository {
    private DynamoDbEnhancedClient client;
    private DynamoDbTable<Fare> fareTable;

    public FareRepository(DynamoDbEnhancedClient client) {
        this.client = client;
    }

    @PostConstruct
    public void init() {
        fareTable = client.table("Fare", TableSchema.fromBean(Fare.class));
    }

    public void save(Fare fare) {
        fareTable.putItem(fare);
    }

    public Fare findById(String fareId) {
        return fareTable.getItem(r -> r.key(Key.builder().partitionValue(fareId).build()));
    }
}
