package com.diploma.logisticsService.service.data.dto;

import com.diploma.logisticsService.exceptions.InvalidNodeException;
import com.diploma.logisticsService.models.csv.Node;
import com.diploma.logisticsService.models.dto.NodeDTO;

import java.util.HashMap;
import java.util.List;

public interface NodeService {

    NodeDTO getNodeById(long id);

    NodeDTO getNodeByCoordinates(double lat, double lon) throws InvalidNodeException;

    Long getNodeIdByCoordinates(double lat, double lon) throws InvalidNodeException;

    List<NodeDTO> getAll();

    HashMap<Long, NodeDTO> toHashMap();

    void saveAll(List<Node> nodes);

    NodeDTO toDto(Node node);
}
