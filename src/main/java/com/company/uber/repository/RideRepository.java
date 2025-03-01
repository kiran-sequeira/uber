package com.company.uber.repository;

import com.company.uber.model.Ride;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Repository
public class RideRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbClient dynamoDbClient;
    private final String tableName = "Ride";
    private DynamoDbTable<Ride> rideTable;

    public RideRepository(DynamoDbClient dynamoDbClient, DynamoDbEnhancedClient enhancedClient) {
        this.dynamoDbClient = dynamoDbClient;
        this.enhancedClient = enhancedClient;
    }

    @PostConstruct
    private void init() {
        createTableIfNotExists();
        rideTable = enhancedClient.table(tableName, TableSchema.fromBean(Ride.class));
    }

    private void createTableIfNotExists() {
        try {
            dynamoDbClient.describeTable(DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build());
        } catch (ResourceNotFoundException e) {
            // Table doesn't exist, create it
            CreateTableRequest createTableRequest = CreateTableRequest.builder()
                    .tableName(tableName)
                    .attributeDefinitions(
                            AttributeDefinition.builder()
                                    .attributeName("rideId")
                                    .attributeType(ScalarAttributeType.S)
                                    .build())
                    .keySchema(
                            KeySchemaElement.builder()
                                    .attributeName("rideId")
                                    .keyType("HASH")
                                    .build())
                    .provisionedThroughput(
                            ProvisionedThroughput.builder()
                                    .readCapacityUnits(5L)
                                    .writeCapacityUnits(5L)
                                    .build())
                    .build();

            dynamoDbClient.createTable(createTableRequest);
        }
    }

    public void save(Ride ride) {
        System.out.println("Saving ride: " + ride);
        rideTable.putItem(ride);
    }

    public Ride findById(String rideId) {
        return rideTable.getItem(Key.builder().partitionValue(rideId).build());
    }

    public void updateRide(Ride ride) {
        rideTable.updateItem(ride);
    }
}