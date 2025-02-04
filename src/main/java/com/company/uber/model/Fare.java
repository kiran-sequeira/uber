package com.company.uber.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.UUID;

@Data
@DynamoDbBean
public class Fare {
    private final String fareId;
    private double amount;

    public Fare() {
        this.fareId = UUID.randomUUID().toString();
    }

    @DynamoDbPartitionKey
    public String getFareId() {
        return this.fareId;
    }
}
