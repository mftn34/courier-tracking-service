package com.furkantufan.courier.tracking.distance;

public interface DistanceCalculator {
    double calculateDistance(double lat1, double lon1, double lat2, double lon2);
}