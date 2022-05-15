package com.diploma.logisticsService.service.routing.scorers;

import com.diploma.logisticsService.models.dto.EdgeDTO;

public interface NewNodeScorer<T extends EdgeDTO> {
    double computeCost(T edge);
}
