package com.company.uber.service;

import com.company.uber.model.Driver;
import com.company.uber.model.Location;
import com.company.uber.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    public void updateDriverLocation(String driverId, Location location) {
        // Update driver location in the repository
        Driver driver = driverRepository.findById(driverId);
        if (driver != null) {
            driver.setCurrentLocation(location);
            driverRepository.updateDriver(driver);
        }
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
}