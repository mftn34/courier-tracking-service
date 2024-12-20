package com.furkantufan.courier.tracking.distance;

public class EuclideanDistanceCalculator implements DistanceCalculator {

    @Override
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2)) * 111_139;
    }
}
