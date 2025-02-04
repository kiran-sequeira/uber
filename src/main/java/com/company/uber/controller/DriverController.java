package com.company.uber.controller;

import com.company.uber.model.Location;
import com.company.uber.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping("/update-location")
    public void updateLocation(@RequestParam String driverId, @RequestBody Location location) {
        driverService.updateDriverLocation(driverId, location);
    }

    @PostMapping("/update-status")
    public void updateStatus(@RequestParam String driverId, @RequestParam String status) {
        driverService.updateDriverStatus(driverId, status);
    }
}

