package com.company.uber.repository;

import com.company.uber.model.Driver;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DriverRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbClient dynamoDbClient;
    private final String tableName = "Driver";
    private DynamoDbTable<Driver> driverTable;

    public DriverRepository(DynamoDbClient dynamoDbClient, DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
        this.dynamoDbClient = dynamoDbClient;
    }

    @PostConstruct
    private void init() {
        createTableIfNotExists();
        driverTable = enhancedClient.table("Driver", TableSchema.fromBean(Driver.class));
    }

    private void createTableIfNotExists() {
        try {
            dynamoDbClient.describeTable(DescribeTableRequest.builder().tableName(tableName).build());
        } catch (ResourceNotFoundException e) {
            CreateTableRequest createTableRequest = CreateTableRequest.builder()
                    .tableName(tableName)
                    .attributeDefinitions(
                            AttributeDefinition.builder()
                                    .attributeName("driverId")
                                    .attributeType(ScalarAttributeType.S)
                                    .build())
                    .keySchema(
                            KeySchemaElement.builder()
                                    .attributeName("driverId")
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

    public void save(Driver driver) {
        driverTable.putItem(driver);
    }

    public Driver findById(String driverId) {
        return driverTable.getItem(r -> r.key(Key.builder().partitionValue(driverId).build()));
    }

    public void updateDriver(Driver driver) {
        driverTable.updateItem(driver);
    }

    public List<Driver> findAvailableDrivers() {
        return driverTable.scan().items().stream()
                .filter(driver -> "AVAILABLE".equals(driver.getStatus()))
                .collect(Collectors.toList());
    }
}