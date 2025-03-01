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

    public Driver() {
        this.driverId = UUID.randomUUID().toString();
        this.status = "AVAILABLE";
    }

    public Driver(String name) {
        this();
        this.name = name;
    }

    @DynamoDbPartitionKey
    public String getDriverId() {
        return this.driverId;
    }

    // Have a method to print this obj
    @Override
    public String toString() {
        return "Driver{" +
                "driverId='" + driverId + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status +
                '}';
    }
}
