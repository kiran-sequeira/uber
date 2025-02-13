package com.company.uber.service;

import com.company.uber.model.Driver;
import com.company.uber.model.Location;
import com.company.uber.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void updateDriverLocation(String driverId, Location location) {
        // Update driver location in Redis
        String key = "driver:location:" + driverId;
        redisTemplate.opsForValue().set(key, location);
    }

    public void updateDriverStatus(String driverId, String status) {
        Driver driver = driverRepository.findById(driverId);
        if (driver != null) {
            driver.setStatus(status);
            driverRepository.updateDriver(driver);
        }
    }

    public Driver findRandomAvailableDriver() {
        List<Driver> availableDrivers = driverRepository.findAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return availableDrivers.get(random.nextInt(availableDrivers.size()));
    }

    public void createDriver(Driver driver) {
        System.out.println("Creating driver: " + driver);
        driverRepository.save(driver);
    }
}