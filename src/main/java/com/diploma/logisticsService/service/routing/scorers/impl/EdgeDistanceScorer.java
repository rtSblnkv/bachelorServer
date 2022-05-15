package com.diploma.logisticsService.service.routing.scorers.impl;

import com.diploma.logisticsService.models.csv.Edge;
import com.diploma.logisticsService.service.routing.scorers.NewNodeScorer;
import org.springframework.stereotype.Component;

@Component
public class EdgeDistanceScorer<T extends Edge> implements NewNodeScorer<T> {
    @Override
    public double computeCost(T edge) {
        return edge.getDistance();
    }
}
