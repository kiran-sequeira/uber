package com.company.uber.repository;

import com.company.uber.model.Driver;
import com.company.uber.model.Location;
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

    public List<Driver> findAvailableDrivers(Location location, double radiusInKm) {
        // In a real-world scenario, we'd use geospatial queries.
        // For DynamoDB, implement a custom method or use a secondary index.
        // Here, we simulate by filtering all drivers within a radius.

        return driverTable.scan().items().stream()
                .filter(driver -> "AVAILABLE".equals(driver.getStatus()))
                .filter(driver -> calculateDistance(location, driver.getCurrentLocation()) <= radiusInKm)
                .collect(Collectors.toList());
    }

    private double calculateDistance(Location loc1, Location loc2) {
        // Haversine formula
        final int R = 6371; // Earth radius in km
        double latDistance = Math.toRadians(loc2.getLatitude() - loc1.getLatitude());
        double lonDistance = Math.toRadians(loc2.getLongitude() - loc1.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(loc1.getLatitude())) * Math.cos(Math.toRadians(loc2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
