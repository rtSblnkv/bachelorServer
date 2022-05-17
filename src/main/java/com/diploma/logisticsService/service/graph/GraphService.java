package com.diploma.logisticsService.service.graph;

import com.diploma.logisticsService.models.dto.EdgeDTO;
import com.diploma.logisticsService.models.dto.NodeDTO;

import java.util.HashMap;
import java.util.List;

public interface GraphService {
    HashMap<NodeDTO, List<EdgeDTO>> getGraph();

    boolean nodeWithIdExist(long id);

    NodeDTO getNodeById(long id);

    List<EdgeDTO> get(NodeDTO node);
}
