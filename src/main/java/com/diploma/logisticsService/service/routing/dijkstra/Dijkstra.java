package com.diploma.logisticsService.service.routing.dijkstra;

import TTL.exception_handlers.NoShortPathException;
import TTL.models.DijkstraRouteNode;
import TTL.models.Edge;
import TTL.models.Node;
import TTL.models.Route;
import TTL.services.graphServices.RouteFinder;
import TTL.services.graphServices.Scorers.EdgeDistanceScorer;
import TTL.services.graphServices.Scorers.NewNodeScorer;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public class Dijkstra implements RouteFinder<Node> {

    private Map<Node,List<Edge>> graph;
    private Map<Long,DijkstraRouteNode<Node>> allNodes = new HashMap<>();
    private NewNodeScorer<Edge> nextNodeTargetScorer;

    public Dijkstra(Map<Node,List<Edge>> graph) {
        this.graph = graph;
        nextNodeTargetScorer = new EdgeDistanceScorer<>();
    }

    public Dijkstra(NewNodeScorer<Edge> nextNodeTargetScorer, Map<Node,List<Edge>> graph) {
        this.graph = graph;
        this.nextNodeTargetScorer = nextNodeTargetScorer;
    }

    /**
     * compute minDistances and set previous Node,
     * from which the distance is the shortest, for each Node in Graph.
     * @param nodeFrom - Node from which min distances will be computed
     */
    public void computeMinDistancesfrom(Node nodeFrom ,NewNodeScorer<Edge> nextNodeTargetScorer)
    {
        try{
            DijkstraRouteNode<Node> dijkstraStartRouteNode = new DijkstraRouteNode<Node>(nodeFrom);
            dijkstraStartRouteNode.setMinDistance(0);
            PriorityBlockingQueue<DijkstraRouteNode<Node>> priorityQueue = new PriorityBlockingQueue<>();
            priorityQueue.add(dijkstraStartRouteNode);
            allNodes.put(nodeFrom.getId(),dijkstraStartRouteNode);

            while (!priorityQueue.isEmpty()) {
                DijkstraRouteNode<Node> curNode = priorityQueue.poll();
                List<Edge> curNodeEdges = graph.get(curNode.getCurrentNode());
                if(curNodeEdges != null) {
                    curNodeEdges.forEach(edge -> {
                        if(nodeWithIdExist(edge.getTo())){
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
    public Route<Node> getShortestPathTo(Node nodeTo) throws NoShortPathException
    {
        List<Node> path = new ArrayList<>();
        if(routeNodeForNodeExist(nodeTo.getId())){
            DijkstraRouteNode<Node> routeNode = findRouteNodeForNode(nodeTo.getId());
            double distance = routeNode.getMinDistance();
            for(;; routeNode = findRouteNodeForNode(routeNode.getPreviousNode().getId())){
                path.add(0,routeNode.getCurrentNode());
                if(routeNode.getPreviousNode() == null) break;
            }
            Route<Node> route = new Route<Node>();
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

    public DijkstraRouteNode<Node> findRouteNodeForNode(long nodeToId){
        return allNodes.get(nodeToId);
    }

    public Route<Node> getRoute(Node nodeFrom, Node nodeTo, NewNodeScorer<Edge> nextNodeTargetScorer){
        computeMinDistancesfrom(nodeFrom, nextNodeTargetScorer);
        return getShortestPathTo(nodeTo);
    }

    public boolean nodeWithIdExist(long id) {
        return graph.keySet()
                .parallelStream()
                .anyMatch(node -> node.getId() == id);
    }

    @Override
    public Node getNodeById(long id) {
        return graph.keySet().parallelStream().filter(node -> node.getId() == id).findAny().get();
    }


    public Route<Node> getRoute(Node nodeFrom, Node nodeTo){
        computeMinDistancesfrom(nodeFrom, nextNodeTargetScorer);
        return getShortestPathTo(nodeTo);
    }
}
