package com.diploma.logisticsService.service.routing.a_star;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NoArgsConstructor;
import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.List;

/**
 * Computes short pathes for list of orders in 2 variations
 * 1. Making recalculating of dijkstra algorithm for each
 * order
 * 2. Calculating dijkstra for branch codes location
 * for splitting by bracnh codes order sublists
 */
@BenchmarkMode(Mode.All)
@Warmup(iterations = 2)
@State(Scope.Benchmark)
@NoArgsConstructor
public class RouteFinderRunner {

    private  List<Node> nodes;
    private  List<Edge> edges;
    private  List<Branch> branches;

    private static HashMap<String, Node> branchNodes;

    public RouteFinderRunner(List<Node> nodes, List<Edge> edges, List<Branch> branches) {
        this.nodes = nodes;
        this.edges = edges;
        this.branches = branches;
    }

    /**
     * computes pathes for list of orders with claculating of dijkstra for each order
     * @param orders - order list
     * @param splitter - splitter (if exists) for which whole order list are divided into order sublists (used for creating file name)
     * @param algorithmType - type of started algorithm (used for creating file name)
     * @return map of node and list of nodes as the shortest path to it from departure node
     */
    public HashMap<Node,Route<Node>> computePathes(
            List<Order> orders,
            String splitter,
            String algorithmType,
            NewNodeScorer<Edge> nextNodeScorer,
            TargetScorer<Node> targetScorer) {

        String noShortPathFileName = "results\\json\\" + splitter + "_no_short_path.txt";
        String nodeNotExistFileName = "results\\json\\" + splitter + "_strange.txt";

        String fileNameJson = "results\\json\\"+splitter +"_"+ algorithmType + "AStar.json";


        NodeWorker nodeWorker = new NodeWorker(nodes);
        BranchWorker branchWorker = new BranchWorker(branches);
        branchNodes = branchWorker.toBranchNodeHashMap(nodeWorker);

        GraphService graphBuilder = new GraphService(nodes, edges);
        HashMap<Node,List<Edge>> graph = graphBuilder.createGraph();

        AStar<Node> routeFinder = new AStar<>();
        routeFinder.setTargetScorer(targetScorer);
        routeFinder.setNextNodeTargetScorer(nextNodeScorer);
        routeFinder.setGraph(graph);

        HashMap<Node,Route<Node>> shortPathes = new HashMap<>();
        try {
            orders.forEach(order -> {
                double datasetDistanceToInMetres = order.getDistanceTo() * 1000;
                try {
                    Node startNode = branchNodes.get(order.getBranchCode().toUpperCase());
                    Node nodeTo = nodeWorker.getNodeByCoordinates(order.getLatitude(), order.getLongitude());
                    Route<Node> pathToCurrentNode = routeFinder.getRoute(startNode,nodeTo);
                    //difference between my result and dataset value
                    double epsilon = pathToCurrentNode.getRouteScore() - datasetDistanceToInMetres;
                    pathToCurrentNode.setEpsilon(epsilon);
                    shortPathes.put(nodeTo, pathToCurrentNode);
                }
                catch(InvalidNodeException ex)
                {
                    NodesToFileWriter.writeErrResultInFile(nodeNotExistFileName,ex.getLat(),ex.getLon());
                }
                catch(NoShortPathException ex)
                {
                    double errNodeLat = ex.getUnattainableNode().getLatitude();
                    double errNodeLon = ex.getUnattainableNode().getLongitude();
                    NodesToFileWriter.writeErrResultInFile(noShortPathFileName,errNodeLat,errNodeLon);
                }
            });
        }
        catch(WriteResultException ex)
        {
            ex.printStackTrace();
        }
        try {
            NodesToJsonWriter.writePathToJson(fileNameJson, shortPathes);
        }
        catch(JsonProcessingException ex){
            ex.printStackTrace();
        }
        return shortPathes;
    }
}
