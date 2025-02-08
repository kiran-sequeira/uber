package com.company.uber.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.UUID;

@Data
@DynamoDbBean
public class Ride {
    private final String rideId;
    private Location source;
    private Location destination;
    private String fareId;
    private String userId;
    private String driverId;
    private String status; // PENDING, ACCEPTED, COMPLETE

    public Ride() {
        this.rideId = UUID.randomUUID().toString();
    }

    @DynamoDbPartitionKey
    public String getRideId() {
        return this.rideId;
    }
}
