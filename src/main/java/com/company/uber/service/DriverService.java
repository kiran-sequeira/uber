package com.company.uber.service;

import com.company.uber.model.Driver;
import com.company.uber.model.Location;
import com.company.uber.repository.DriverRepository;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public void updateDriverLocation(String driverId, Location location) {
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
}
