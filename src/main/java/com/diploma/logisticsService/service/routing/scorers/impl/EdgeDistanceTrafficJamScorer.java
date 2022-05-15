package com.diploma.logisticsService.service.routing.scorers.impl;

import com.diploma.logisticsService.models.dto.EdgeDTO;
import com.diploma.logisticsService.service.routing.scorers.NewNodeScorer;

public class EdgeDistanceTrafficJamScorer<T extends EdgeDTO> implements NewNodeScorer<T> {
    @Override
    public double computeCost(T edge) {
        return 0;
    }
}
