package com.furkantufan.courier.tracking.distance;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class DistanceCalculatorStrategy {

    private DistanceCalculator distanceCalculator;

    public double calculate(double lat1, double lon1, double lat2, double lon2) {
        return distanceCalculator.calculateDistance(lat1, lon1, lat2, lon2);
    }
}