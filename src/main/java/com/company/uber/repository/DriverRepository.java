package com.company.uber.repository;

import com.company.uber.model.Driver;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DriverRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<Driver> driverTable;

    public DriverRepository(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
    }

    @PostConstruct
    private void init() {
        driverTable = enhancedClient.table("Driver", TableSchema.fromBean(Driver.class));
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