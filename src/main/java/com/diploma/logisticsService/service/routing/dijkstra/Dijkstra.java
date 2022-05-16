package com.diploma.logisticsService.service.routing.dijkstra;


import com.diploma.logisticsService.exceptions.NoShortPathException;
import com.diploma.logisticsService.models.csv.Route;
import com.diploma.logisticsService.models.dto.EdgeDTO;
import com.diploma.logisticsService.models.dto.NodeDTO;
import com.diploma.logisticsService.models.routing.DijkstraRouteNode;
import com.diploma.logisticsService.service.graph.GraphService;
import com.diploma.logisticsService.service.routing.a_star.RouteFinder;
import com.diploma.logisticsService.service.routing.scorers.NewNodeScorer;
import com.diploma.logisticsService.service.routing.scorers.TargetScorer;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Dijkstra algorithm realization
 * graph - variable, which contains graph structure
 * methods : computeMinDistanceFrom , getShortestpathTo
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class Dijkstra implements RouteFinder<NodeDTO> {

    private final GraphService graphService;


    private Map<Long, DijkstraRouteNode<NodeDTO>> allNodes = new HashMap<>();

    /**
     * compute minDistances and set previous Node,
     * from which the distance is the shortest, for each Node in Graph.
     * @param nodeFrom - Node from which min distances will be computed
     */
    public void computeMinDistancesfrom(NodeDTO nodeFrom ,NewNodeScorer<EdgeDTO> nextNodeTargetScorer)
    {
        try{
            DijkstraRouteNode<NodeDTO> dijkstraStartRouteNode = new DijkstraRouteNode<Node>(nodeFrom);
            dijkstraStartRouteNode.setMinDistance(0);
            PriorityBlockingQueue<DijkstraRouteNode<NodeDTO>> priorityQueue = new PriorityBlockingQueue<>();
            priorityQueue.add(dijkstraStartRouteNode);
            allNodes.put(nodeFrom.getId(),dijkstraStartRouteNode);

            while (!priorityQueue.isEmpty()) {
                DijkstraRouteNode<Node> curNode = priorityQueue.poll();
                List<Edge> curNodeEdges = graph.get(curNode.getCurrentNode());
                if(curNodeEdges != null) {
                    curNodeEdges.forEach(edge -> {
                        if(graphService.nodeWithIdExist(edge.getTo())){
                            Node curNodeTo = getNodeById(edge.getTo());
                            DijkstraRouteNode<Node> nextNode = allNodes.getOrDefault(
                                    curNodeTo.getId(),
                                    new DijkstraRouteNode<>(curNodeTo)
                            );
                            allNodes.put(curNodeTo.getId(), nextNode);
                            double minDistance = curNode.getMinDistance() + nextNodeTargetScorer.computeCost(edge);
                            if (minDistance < nextNode.getMinDistance()) {
                                priorityQueue.remove(curNode);
                                nextNode.setPreviousNode(curNode.getCurrentNode());
                                nextNode.setMinDistance(minDistance);
                                priorityQueue.add(nextNode);
                            }
                        }
                    });
                }
            }
        }
        catch(RuntimeException ex)
        {
            System.out.println("Error: "+ex.getMessage());
        }
    }

    /**
     * Returns shortest path to Node nodeTo from node,
     * for which computeMinDistancesFrom was launched
     * @param nodeTo - The final point to compute short path for
     * @return list of nodes, which contains the shortest path to nodeTo
     */
    public Route<NodeDTO> getShortestPathTo(NodeDTO nodeTo) throws NoShortPathException
    {
        List<NodeDTO> path = new ArrayList<>();
        if(routeNodeForNodeExist(nodeTo.getId())){
            DijkstraRouteNode<NodeDTO> routeNode = findRouteNodeForNode(nodeTo.getId());
            double distance = routeNode.getMinDistance();
            for(;; routeNode = findRouteNodeForNode(routeNode.getPreviousNode().getId())){
                path.add(0,routeNode.getCurrentNode());
                if(routeNode.getPreviousNode() == null) break;
            }
            Route<NodeDTO> route = new Route<>();
            route.setRoute(path);
            route.setRouteScore(distance);
            return route;
        }
        String errMessage = "No short path for order with coordinates [" + nodeTo.getLatitude() + "," + nodeTo.getLongitude() + "]";
        throw new NoShortPathException(errMessage,nodeTo);
    }

    public boolean routeNodeForNodeExist(long nodeToId){
       return allNodes.keySet().parallelStream().anyMatch(key -> key == nodeToId);
    }

    public DijkstraRouteNode<NodeDTO> findRouteNodeForNode(long nodeToId){
        return allNodes.get(nodeToId);
    }

    @Override
    public Route<NodeDTO> getRoute(NodeDTO nodeFrom,
                                   NodeDTO nodeTo,
                                   NewNodeScorer<EdgeDTO> nextNodeTargetScorer,
                                   TargetScorer<NodeDTO> targetScorer){
        computeMinDistancesfrom(nodeFrom, nextNodeTargetScorer);
        return getShortestPathTo(nodeTo);
    }
}
