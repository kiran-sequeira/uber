package com.company.uber.service;

import com.company.uber.model.Driver;
import com.company.uber.model.Location;
import com.company.uber.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String GEO_KEY = "drivers:geo";

    public void updateDriverLocation(String driverId, Location location) {
        // Update driver location in Redis using GEOADD
        Point point = new Point(location.getLongitude(), location.getLatitude());
        redisTemplate.opsForGeo().add(GEO_KEY, new RedisGeoCommands.GeoLocation<>(driverId, point));
    }

    public void updateDriverStatus(String driverId, String status) {
        Driver driver = driverRepository.findById(driverId);
        if (driver != null) {
            driver.setStatus(status);
            driverRepository.updateDriver(driver);
        }
    }

    public Driver findNearestAvailableDriver(Location location, double radius) {
        // Find nearest available drivers within the specified radius using GEOSEARCH
        Point point = new Point(location.getLongitude(), location.getLatitude());
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = redisTemplate.opsForGeo()
                .search(GEO_KEY,
                        GeoReference.fromCoordinate(point.getX(), point.getY()),
                        new Distance(radius, Metrics.KILOMETERS),
                        RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs().sortAscending());

        if (geoResults != null) {
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoResult : geoResults.getContent()) {
                RedisGeoCommands.GeoLocation<String> geoLocation = geoResult.getContent();
                String driverId = geoLocation.getName();
                Driver driver = driverRepository.findById(driverId);
                if (driver != null && "AVAILABLE".equals(driver.getStatus())) {
                    return driver;
                }
            }
        }
        return null;
    }

    public void createDriver(Driver driver) {
        System.out.println("Creating driver: " + driver);
        driverRepository.save(driver);
    }
}