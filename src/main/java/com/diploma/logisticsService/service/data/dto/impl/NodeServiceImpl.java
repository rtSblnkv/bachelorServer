package com.diploma.logisticsService.service.data.dto.impl;

import com.diploma.logisticsService.exceptions.InvalidNodeException;
import com.diploma.logisticsService.models.csv.Node;
import com.diploma.logisticsService.models.dto.NodeDTO;
import com.diploma.logisticsService.repository.NodeRepository;
import com.diploma.logisticsService.service.data.dto.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class NodeServiceImpl implements NodeService {

    private final NodeRepository nodeRepository;

    /**
     * Returns the nodeId of node with latitude = lat and longtitude = lon
     * or else return 0L
     *
     * @param lat - node latitude
     * @param lon - node longitude
     * @return node id
     */
    @Override
    public Long getNodeIdByCoordinates(double lat, double lon) throws InvalidNodeException {
        return this.nodeRepository
                .findFirstIdByLatitudeAndLongitude(lat, lon)
                .orElse(null);
    }

    @Override
    public List<NodeDTO> getAll() {
        Iterable<NodeDTO> nodes = this.nodeRepository.findAll();
        return StreamSupport
                .stream(nodes.spliterator(), true)
                .collect(Collectors.toList());
    }

    @Override
    public NodeDTO getNodeById(long id) {
        return this.nodeRepository
                .findById(id)
                .orElse(null);
    }

    /**
     * Returns the Node object with latitude = lat and longtitude = lon
     * or Else returns empty Node
     *
     * @param lat - node latitude
     * @param lon - node longtitude
     * @return Node
     */
    @Override
    public NodeDTO getNodeByCoordinates(double lat, double lon) throws InvalidNodeException {
        return this.nodeRepository
                .findFirstByLatitudeAndLongitude(lat, lon)
                .orElse(null);
    }

    /**
     * Converts list of nodes into HashMap
     *
     * @return HashMap ( Node ID, Node)
     */
    @Override
    public HashMap<Long, NodeDTO> toHashMap() {
        HashMap<Long, NodeDTO> nodesHashMap = new HashMap<>();
        List<NodeDTO> nodes = getAll();
        nodes.forEach(node -> nodesHashMap.put(node.getId(), node));
        return nodesHashMap;
    }

    @Override
    public void saveAll(List<Node> nodes) {
        List<NodeDTO> nodesDTO = nodes
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        nodeRepository.saveAll(nodesDTO);
    }

    @Override
    public NodeDTO toDto(Node node) {
        NodeDTO nodeDTO = new NodeDTO();
        nodeDTO.setId(node.getId());
        nodeDTO.setLatitude(node.getLatitude());
        nodeDTO.setLongitude(node.getLongitude());
        return nodeDTO;
    }
}