package com.company.uber.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.UUID;

@Data
@DynamoDbBean
public class Driver {
    private final String driverId;
    private String name;
    private String status; // AVAILABLE, BUSY
    private Location currentLocation;

    public Driver() {
        this.driverId = UUID.randomUUID().toString();
    }

    @DynamoDbPartitionKey
    public String getDriverId() {
        return this.driverId;
    }
}
