package com.diploma.logisticsService.service.routing.a_star;

import com.diploma.logisticsService.models.csv.Route;
import com.diploma.logisticsService.models.dto.NodeDTO;

public interface RouteFinder<T extends NodeDTO> {
    Route<T> getRoute(T nodeFrom, T nodeTo);
}