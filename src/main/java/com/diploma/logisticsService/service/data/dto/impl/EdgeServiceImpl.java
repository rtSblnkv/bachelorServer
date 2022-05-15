package com.diploma.logisticsService.service.data.dto.impl;

import com.diploma.logisticsService.models.csv.Edge;
import com.diploma.logisticsService.models.dto.EdgeDTO;
import com.diploma.logisticsService.models.dto.NodeDTO;
import com.diploma.logisticsService.repository.EdgeRepository;
import com.diploma.logisticsService.service.data.dto.EdgeService;
import com.diploma.logisticsService.service.data.dto.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class EdgeServiceImpl implements EdgeService {

    private final EdgeRepository edgeRepository;
    private final NodeService nodeService;

    public List<EdgeDTO> getAll(){
        Iterable<EdgeDTO> edges = edgeRepository.findAll();
        return StreamSupport
                .stream(edges.spliterator(),true)
                .collect(Collectors.toList());
    }

    /**
     * Converts list of edges into HashMap
     * @return HashMap (fromNode ID, List of edges, for which fromNode is start node)
     */
    @Override
    public HashMap<Long,List<EdgeDTO>> toHashMap() {
        HashMap<Long,List<EdgeDTO>> edgesHashMap = new HashMap<>();
        List<EdgeDTO> edges = getAll();
        for (EdgeDTO edge : edges) {
            edgesHashMap.computeIfAbsent(edge.getFrom().getId(), k -> new ArrayList<>()).add(edge);
        }
        return edgesHashMap;
    }

    @Override
    public void saveAll(List<Edge> edges) {
        List<EdgeDTO> edgesDTO = edges
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        setTrafficJamPoints(edgesDTO);
        edgeRepository.saveAll(edgesDTO);
    }

    private void setTrafficJamPoints(List<EdgeDTO> edges){
        Random r = new Random();
        edges.forEach(edge -> {
            int jamePoint = r.nextInt(10);
            edge.setTrafficJamPoint(jamePoint);
        });
    }

    @Override
    public EdgeDTO toDto(Edge edge) {
        EdgeDTO edgeDTO = new EdgeDTO();
        edgeDTO.setDistance(edge.getDistance());
        edgeDTO.setStreetType(edge.getStreetType());
        NodeDTO from = nodeService.getNodeById(edge.getFrom());
        NodeDTO to = nodeService.getNodeById(edge.getTo());
        edgeDTO.setFrom(from);
        edgeDTO.setTo(to);
        return edgeDTO;
    }
}
