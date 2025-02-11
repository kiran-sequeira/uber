package com.company.uber.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.UUID;

@Data
@DynamoDbBean
public class Driver {
    private String driverId;
    private String name;
    private String status; // AVAILABLE, BUSY
    private Location currentLocation;

    public Driver() {
        this.driverId = UUID.randomUUID().toString();
        this.status = "AVAILABLE";
    }

    public Driver(String name, Location currentLocation) {
        this();
        this.name = name;
        this.currentLocation = currentLocation;
    }

    @DynamoDbPartitionKey
    public String getDriverId() {
        return this.driverId;
    }
}
