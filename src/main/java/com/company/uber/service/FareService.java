package com.company.uber.service;

import com.company.uber.model.Fare;
import com.company.uber.repository.FareRepository;
import org.springframework.stereotype.Service;
@Service
public class FareService {

    private final FareRepository fareRepository;

    public FareService(FareRepository fareRepository) {
        this.fareRepository = fareRepository;
    }

    public Fare createFare(double amount) {
        Fare fare = new Fare();
        fare.setAmount(amount);
        fareRepository.save(fare);
        return fare;
    }

    public Fare getFare(String fareId) {
        return fareRepository.findById(fareId);
    }
}

