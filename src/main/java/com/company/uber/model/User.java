package com.company.uber.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.UUID;

@Data
@DynamoDbBean
public class User {
    private final String userId;
    private String name;

    public User() {
        this.userId = UUID.randomUUID().toString();
    }

    @DynamoDbPartitionKey
    public String getUserId() {
        return this.userId;
    }
}
