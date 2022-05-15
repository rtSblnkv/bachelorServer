package com.diploma.logisticsService.service.routing.dijkstra;

import TTL.exception_handlers.InvalidNodeException;
import TTL.exception_handlers.NoShortPathException;
import TTL.exception_handlers.WriteResultException;
import TTL.models.*;
import TTL.services.graphServices.GraphService;
import TTL.services.graphServices.Scorers.NewNodeScorer;
import TTL.services.listWorkers.BranchWorker;
import TTL.services.listWorkers.NodeWorker;
import TTL.services.writers.NodesToFileWriter;
import TTL.services.writers.NodesToJsonWriter;
import TTL.services.writers.Writer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NoArgsConstructor;
import org.openjdk.jmh.annotations.*;

import java.nio.file.FileAlreadyExistsException;
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
public class DijkstraRunner {

    private  List<Node> nodes;
    private  List<Edge> edges;
    private  List<Branch> branches;

    public DijkstraRunner( List<Node> nodes, List<Edge> edges, List<Branch> branches) {
        this.nodes = nodes;
        this.edges = edges;
        this.branches = branches;
    }

    public HashMap<Node,Route<Node>> computePathesForLayer(
            NewNodeScorer<Edge> scorer,
            String branch,
            List<Order> orders,
            String type
    ) {
        HashMap<Node,Route<Node>> shortPathes = new HashMap<>();

        String noShortPathFileName = "results\\dijkstra\\"+branch +"_"+ type +"_no_short_path.txt";
        String nodeNotExistFileName = "results\\dijkstra\\"+branch +"_"+ type +"_strange.txt";

        String fileNameJson = "results\\dijkstra\\json\\"+branch +"_"+ type + "Dijkstra.json";

        try{
            Writer.createFile(noShortPathFileName);
            Writer.createFile(nodeNotExistFileName);
        }
        catch(FileAlreadyExistsException ex) {
            System.out.println(ex.getMessage());
        }

        NodeWorker nodeWorker = new NodeWorker(nodes);
        Node branchNode = getBranchNode(branch,nodeWorker);

        GraphService graphBuilder = new GraphService(nodes,edges);
        HashMap<Node,List<Edge>> graph = graphBuilder.createGraph();

        Dijkstra dijkstra = new Dijkstra(graph);
        dijkstra.computeMinDistancesfrom(branchNode,scorer);

        try {
            orders.forEach(order -> {
                double datasetDistanceToInMetres = order.getDistanceTo() * 1000;//in metres
                try {
                    Node nodeTo = nodeWorker.getNodeByCoordinates(order.getLatitude(), order.getLongitude());
                    Route<Node> pathToCurrentNode = dijkstra.getShortestPathTo(nodeTo);
                    double epsilon = pathToCurrentNode.getRouteScore() - datasetDistanceToInMetres;//difference between my result and dataset value
                    pathToCurrentNode.setEpsilon(epsilon);
                    shortPathes.put(nodeTo, pathToCurrentNode);
                }
                catch(InvalidNodeException ex) {
                    NodesToFileWriter.writeErrResultInFile(nodeNotExistFileName,ex.getLat(),ex.getLon());
                }
                catch(NoShortPathException ex) {
                    double errNodeLat = ex.getUnattainableNode().getLatitude();
                    double errNodeLon = ex.getUnattainableNode().getLongitude();
                    NodesToFileWriter.writeErrResultInFile(noShortPathFileName, errNodeLat, errNodeLon);
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

    private Node getBranchNode(String branch, NodeWorker nw){
        BranchWorker branchWorker = new BranchWorker(branches);
        HashMap<String, Node> branchNodes = branchWorker.toBranchNodeHashMap(nw);
        return branchNodes.get(branch);
    }
}
