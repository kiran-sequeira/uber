package com.company.uber.repository;

import org.springframework.stereotype.Repository;

import com.company.uber.model.Ride;
import jakarta.annotation.PostConstruct;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class RideRepository {
    private final DynamoDbEnhancedClient client;
    private DynamoDbTable<Ride> rideTable;

    public RideRepository(DynamoDbEnhancedClient client) {
        this.client = client;
    }

    @PostConstruct
    public void init() {
        rideTable = client.table("Ride", TableSchema.fromBean(Ride.class));
    }

    public void save(Ride ride) {
        rideTable.putItem(ride);
    }

    public Ride findById(String rideId) {
        return rideTable.getItem(r -> r.key(Key.builder().partitionValue(rideId).build()));
    }

    public void updateRide(Ride ride) {
        rideTable.updateItem(ride);
    }
}
