package com.company.uber.service;

import com.company.uber.model.Driver;
import com.company.uber.model.Fare;
import com.company.uber.model.Location;
import com.company.uber.model.Ride;
import com.company.uber.repository.DriverRepository;
import com.company.uber.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private FareService fareService;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private NotificationService notificationService;

    public Ride createRide(Location source, Location destination, String userId) {
        double estimatedFareAmount = calculateFare(source, destination);
        Fare fare = fareService.createFare(estimatedFareAmount);

        Ride ride = new Ride();
        ride.setSource(source);
        ride.setDestination(destination);
        ride.setFareId(fare.getFareId());
        ride.setStatus("PENDING");
        ride.setUserId(userId);
        rideRepository.save(ride);

        findDriversAsync(ride);

        return ride;
    }

    private double calculateFare(Location source, Location destination) {
        double distance = calculateDistance(source, destination);
        return distance * 1.5; // Fare per km
    }

    private double calculateDistance(Location loc1, Location loc2) {
        // Haversine formula
        final int R = 6371;
        double latDistance = Math.toRadians(loc2.getLatitude() - loc1.getLatitude());
        double lonDistance = Math.toRadians(loc2.getLongitude() - loc1.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(loc1.getLatitude())) * Math.cos(Math.toRadians(loc2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    @Async
    public CompletableFuture<Void> findDriversAsync(Ride ride) {
        try {
            Thread.sleep(60000); // Simulate 1-minute delay

            Location pickupLocation = ride.getSource();
            double searchRadius = 5.0; // km
            List<Driver> nearbyDrivers = driverRepository.findAvailableDrivers(pickupLocation, searchRadius);

            int maxDriversToNotify = 3;
            int notifiedDrivers = 0;

            for (Driver driver : nearbyDrivers) {
                if (notifiedDrivers >= maxDriversToNotify) break;

                notificationService.notifyDriver(driver.getDriverId(), "New ride request: " + ride.getRideId());
                notifiedDrivers++;
            }

            if (notifiedDrivers == 0) {
                notificationService.notifyUser(ride.getUserId(), "No drivers available at the moment.");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture(null);
    }

    public void acceptRide(String driverId, String rideId) {
        Ride ride = rideRepository.findById(rideId);
        if (ride != null && "PENDING".equals(ride.getStatus())) {
            ride.setDriverId(driverId);
            ride.setStatus("ACCEPTED");
            rideRepository.updateRide(ride);

            Driver driver = driverRepository.findById(driverId);
            driver.setStatus("BUSY");
            driverRepository.updateDriver(driver);

            notificationService.notifyUser(ride.getUserId(), "Your ride has been accepted by a driver.");
        }
    }
}

