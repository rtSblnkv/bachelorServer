package com.diploma.logisticsService.service.routing.scorers;

import com.diploma.logisticsService.models.dto.NodeDTO;

public interface TargetScorer<T extends NodeDTO> {
    double computeCost(T from, T to);
}
