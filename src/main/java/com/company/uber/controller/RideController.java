package com.company.uber.controller;


import com.company.uber.model.Location;
import com.company.uber.model.Ride;
import com.company.uber.model.RideRequest;
import com.company.uber.model.RideResponse;
import com.company.uber.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rides")
public class RideController {

    @Autowired
    private RideService rideService;

    @PostMapping("/request")
    public RideResponse createRide(@RequestBody RideRequest rideRequest) {
        System.out.println("rideRequest: " + rideRequest);
        Location source = new Location();
        source.setLatitude(rideRequest.getSourceLatitude());
        source.setLongitude(rideRequest.getSourceLongitude());
        System.out.println("source: " + source);

        Location destination = new Location();
        System.out.println("destination: " + destination);
        destination.setLatitude(rideRequest.getDestinationLatitude());
        destination.setLongitude(rideRequest.getDestinationLongitude());

        Ride ride = rideService.createRide(source, destination, rideRequest.getUserId());

        return new RideResponse(ride.getRideId(), ride.getFareId(), ride.getStatus());
    }

    @PostMapping("/accept")
    public void acceptRide(@RequestParam String driverId, @RequestParam String rideId) {
        rideService.acceptRide(driverId, rideId);
    }
}

