package com.diploma.logisticsService.service.graph;


import com.diploma.logisticsService.models.dto.EdgeDTO;
import com.diploma.logisticsService.models.dto.NodeDTO;
import com.diploma.logisticsService.service.data.dto.EdgeService;
import com.diploma.logisticsService.service.data.dto.NodeService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Class which creates graph structure from
 * list of nodes (nodes)
 * list of edges (edges)
 **/
@RequiredArgsConstructor
@Setter
@Service
public class GraphServiceImpl implements GraphService {
    private final NodeService nodeService;
    private final EdgeService edgeService;

    private HashMap<NodeDTO, List<EdgeDTO>> graph;

    /**
     * Creates graph structure from Nodes and Edges lists by adding list of adjacent edges for each node
     *
     * @return Map(node id, node)
     */
    private void createGraph() {

        HashMap<Long, List<EdgeDTO>> edgeHashMap = edgeService.toHashMap();
        HashMap<Long, NodeDTO> nodesHashMap = nodeService.toHashMap();
        graph = new HashMap<>();
        edgeHashMap.keySet().forEach(id -> graph.put(
                        nodesHashMap.get(id),
                        edgeHashMap.get(id)
                )
        );
    }

    @Override
    public HashMap<NodeDTO, List<EdgeDTO>> getGraph(){
        if(graph == null) createGraph();
        return graph;
    }

    @Override
    public boolean nodeWithIdExist(long id) {
        if(graph == null) createGraph();
        return graph.keySet()
                .parallelStream()
                .anyMatch(node -> node.getId() == id);
    }

    @Override
    public NodeDTO getNodeById(long id) {
        if(graph == null) createGraph();
        return graph.keySet()
                .parallelStream()
                .filter(node -> node.getId() == id)
                .findAny()
                .get();
    }

    @Override
    public List<EdgeDTO> get(NodeDTO node){
        if(graph == null) createGraph();
        return graph.get(node);
    }


}
