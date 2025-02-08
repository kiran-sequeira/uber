package com.company.uber.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void notifyDriver(String driverId, String message) {
        // Mock notification to driver
        System.out.println("Notification to Driver [" + driverId + "]: " + message);
    }

    public void notifyUser(String userId, String message) {
        // Mock notification to user
        System.out.println("Notification to User [" + userId + "]: " + message);
    }
}
