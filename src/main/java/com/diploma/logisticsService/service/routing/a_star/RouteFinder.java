package com.diploma.logisticsService.service.routing.a_star;

import com.diploma.logisticsService.exceptions.NoShortPathException;
import com.diploma.logisticsService.models.csv.Route;
import com.diploma.logisticsService.models.dto.EdgeDTO;
import com.diploma.logisticsService.models.dto.NodeDTO;
import com.diploma.logisticsService.models.routing.RoutingParams;
import com.diploma.logisticsService.service.routing.scorers.NewNodeScorer;
import com.diploma.logisticsService.service.routing.scorers.TargetScorer;

public interface RouteFinder<T extends NodeDTO> {

    Route<T> getRoute(
            T nodeFrom,
            T nodeTo,
            NewNodeScorer<EdgeDTO> nextNodeTargetScorer,
            TargetScorer<T> targetScorer,
            RoutingParams params) throws NoShortPathException;
}