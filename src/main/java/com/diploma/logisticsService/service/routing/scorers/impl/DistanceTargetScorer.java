package com.diploma.logisticsService.service.routing.scorers.impl;

import com.diploma.logisticsService.models.dto.NodeDTO;
import com.diploma.logisticsService.service.routing.scorers.TargetScorer;
import org.springframework.stereotype.Component;

@Component
public class DistanceTargetScorer<T extends NodeDTO> implements TargetScorer<T> {
    @Override
    public double computeCost(T from, T to) {
        double R = 6372.8; // Earth's Radius, in kilometers

        double dLat = Math.toRadians(to.getLatitude() - from.getLatitude());
        double dLon = Math.toRadians(to.getLongitude() - from.getLongitude());
        double lat1 = Math.toRadians(from.getLatitude());
        double lat2 = Math.toRadians(to.getLatitude());

        double a = Math.pow(Math.sin(dLat / 2),2)
                + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}
