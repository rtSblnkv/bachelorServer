package com.diploma.logisticsService.service.data.dto;

import com.diploma.logisticsService.models.csv.Edge;
import com.diploma.logisticsService.models.dto.EdgeDTO;

import java.util.HashMap;
import java.util.List;

public interface EdgeService {
    HashMap<Long, List<EdgeDTO>> toHashMap();

    void saveAll(List<Edge> edges);

    EdgeDTO toDto(Edge edge);
}
