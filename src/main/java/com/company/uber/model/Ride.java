package com.company.uber.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.UUID;

import com.company.uber.util.LocationConverter;

@Data
@DynamoDbBean
public class Ride {
    private String rideId;
    private Location source;
    private Location destination;
    private String fareId;
    private String userId;
    private String driverId;
    private String status; // PENDING, ACCEPTED, COMPLETE

    public Ride() {
        this.rideId = UUID.randomUUID().toString();
        this.status = "PENDING";
    }

    @DynamoDbPartitionKey
    public String getRideId() {
        return this.rideId;
    }

    @DynamoDbConvertedBy(LocationConverter.class)
    public Location getSource() {
        return this.source;
    }

    @DynamoDbConvertedBy(LocationConverter.class)
    public Location getDestination() {
        return this.destination;
    }
}
