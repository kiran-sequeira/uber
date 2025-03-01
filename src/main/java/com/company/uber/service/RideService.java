package com.company.uber.service;

import com.company.uber.model.Driver;
import com.company.uber.model.Fare;
import com.company.uber.model.Location;
import com.company.uber.model.Ride;
import com.company.uber.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private DriverService driverService;

    @Autowired
    private FareService fareService;

    @Autowired
    private NotificationService notificationService;

    public Ride createRide(Location source, Location destination, String userId) {
        Ride ride = new Ride();
        ride.setSource(source);
        ride.setDestination(destination);
        ride.setUserId(userId);
        ride.setStatus("PENDING");

        // Calculate fare (mock implementation)
        double fareAmount = calculateFare(source, destination);
        Fare fare = fareService.createFare(fareAmount);
        ride.setFareId(fare.getFareId());
        System.out.println("ride: " + ride);
        rideRepository.save(ride);

        // Find a random available driver and notify them
        Driver driver = driverService.findNearestAvailableDriver(source, 5);
        if (driver != null) {
            notificationService.notifyDriver(driver.getDriverId(), "New ride request: " + ride.getRideId());
        }

        return ride;
    }

    public void acceptRide(String driverId, String rideId) {
        Ride ride = rideRepository.findById(rideId);
        if (ride != null && "PENDING".equals(ride.getStatus())) {
            ride.setDriverId(driverId);
            ride.setStatus("ACCEPTED");
            rideRepository.updateRide(ride);

            // Update driver status
            driverService.updateDriverStatus(driverId, "BUSY");

            // Notify user
            notificationService.notifyUser(ride.getUserId(), "Your ride has been accepted by driver: " + driverId);
        }
    }

    private double calculateFare(Location source, Location destination) {
        // Mock fare calculation
        return 10.0;
    }
}