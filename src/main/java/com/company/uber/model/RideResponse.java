package com.company.uber.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideResponse {
    private String rideId;
    private String fareId;
    private String status;
}
