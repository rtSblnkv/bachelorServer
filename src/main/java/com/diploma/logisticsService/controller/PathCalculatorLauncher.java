package com.diploma.logisticsService.controller;


import com.diploma.logisticsService.service.routing.a_star.RouteFinderRunner;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.HashMap;
import java.util.List;


/**
 * Class which calculates short pathes for list of orders
 * in different variations:
 * for full list of orders
 * linear and parallel versions for splitted by
 *  - order type
 *  - branch code
 *  linear and parallel for splitted on branch code orders
 *  with preliminary launching of the dijkstra algorithm for each branch location
 */
@State(Scope.Benchmark)
@NoArgsConstructor
public class PathCalculatorLauncher {

    private RouteFinderRunner runner;
    private DijkstraRunner dijkstraRunner;
    private List<Order> orders;
    private NewNodeScorer<Edge> nextNodeScorer;
    private TargetScorer<Node> targetScorer;

    /**
     * Linear computing of shortest pathes and distances for each order in full list of orders
     * @return Map(Node, lists,which contains shortest path to the Node from restaurant)
     */
    @Benchmark
    public HashMap<Node, Route<Node>> getShortestForAllOrders()
    {
        System.out.println("Linear All orders");
        return runner.computePathes(orders,"","orders",nextNodeScorer,targetScorer);
    }

    /**
     * Linear computing of shortest pathes and distances for each order in list orders splitted by order type
     * @return Map(Node, lists,which contains shortest path to the Node from restaurant)
     */
    @Benchmark
    public HashMap<Node,Route<Node>> getShortestForAllOrdersByOrderTypeLinear()
    {
        ByOrderTypeLayers byOrderTypeLayers = new ByOrderTypeLayers(orders);
        System.out.println("Order Type Linear");
        return byOrderTypeLayers.getLayers()
                .entrySet()
                .stream()
                .map(branchOrders -> runner.computePathes(
                        branchOrders.getValue(),
                        branchOrders.getKey(),
                        "OrderTypeLinear",
                        nextNodeScorer,
                        targetScorer))
                .reduce(this::merge)
                .orElse(new HashMap<>() );
    }

    /**
     * Parallel computing of shortest pathes and distances for each order in list orders splitted by order type
     * @return Map(Node, lists,which contains shortest path to the Node from restaurant)
     */
    @Benchmark
    public HashMap<Node,Route<Node>> getShortestForAllOrdersByOrderTypeParallel()
    {
        ByOrderTypeLayers  byOrderTypeLayers = new ByOrderTypeLayers(orders);
        System.out.println(" Order Type Parallel");
        return byOrderTypeLayers.getLayers()
                .entrySet()
                .parallelStream()
                .map(branchOrders -> runner.computePathes(
                        branchOrders.getValue(),
                        branchOrders.getKey(),
                        "OrderTypeParallel",
                        nextNodeScorer,
                        targetScorer))
                .reduce(this::merge)
                .orElse(new HashMap<>() );
    }

    /**
     * Linear computing of shortest pathes and distances for each order in list orders splitted by branch code
     * @return Map(Node, lists,which contains shortest path to the Node from restaurant)
     */
    @Benchmark
    public HashMap<Node,Route<Node>> getShortestForAllOrdersByBranchCodeLinear()
    {
        ByBranchCodeLayers byBranchCodeLayers = new ByBranchCodeLayers(orders);
        System.out.println("Branch Code Linear");
        return byBranchCodeLayers.getLayers()
                .entrySet()
                .stream()
                .map(branchOrders -> runner.computePathes(
                        branchOrders.getValue(),
                        branchOrders.getKey(),
                        "BranchCodeLinear",
                        nextNodeScorer,
                        targetScorer))
                .reduce(this::merge)
                .orElse(new HashMap<>() );
    }

    /**
     * Parallel computing of shortest pathes and distances for each order in list orders splitted by branch code
     * @return Map(Node, lists,which contains shortest path to the Node from restaurant)
     */
    @Benchmark
    public HashMap<Node,Route<Node>> getShortestForAllOrdersByBranchCodeParallel()
    {
        ByBranchCodeLayers byBranchCodeLayers = new ByBranchCodeLayers(orders);
        System.out.println("Branch Code Parallel");
        return byBranchCodeLayers.getLayers()
                .entrySet()
                .parallelStream()
                .map(branchOrders -> runner.computePathes(
                        branchOrders.getValue(),
                        branchOrders.getKey(),
                         "BranchCodeParallel",
                        nextNodeScorer,
                        targetScorer))
                .reduce(this::merge)
                .orElse(new HashMap<>() );
    }

    /**
     * Linear computing of shortest pathes and distances for each order in list orders splitted by branch code
     * Using computePathesForLayer method
     * @return Map(Node, lists,which contains shortest path to the Node from restaurant)
     */
   // @Benchmark
    public HashMap<Node,Route<Node>> getShortestForOrdersByBranchCodeLinearPreliminary(NewNodeScorer<Edge> scorer)
    {
        ByBranchCodeLayers byBranchCodeLayers = new ByBranchCodeLayers(orders);
        System.out.println("Branch Code Linear by Layer");
        return byBranchCodeLayers.getLayers()
                .entrySet()
                .stream()
                .map(branchOrders -> dijkstraRunner.computePathesForLayer(
                        scorer,
                        branchOrders.getKey(),
                        branchOrders.getValue(),
                        "BranchCodeLinear")
                )
                .reduce(this::merge)
                .orElse(new HashMap<>() );
    }

    /**
     * Parallel computing of shortest pathes and distances for each order in list orders splitted by branch code
     * Using computePathesForLayer method
     * @return Map(Node, lists,which contains shortest path to the Node from restaurant)
     */
    //@Benchmark
    public HashMap<Node,Route<Node>> getShortestForOrdersByBranchCodeParallelPreliminary(NewNodeScorer<Edge> scorer) {
        ByBranchCodeLayers byBranchCodeLayers = new ByBranchCodeLayers(orders);
        System.out.println("Branch Code Parallel by Layer");
        return byBranchCodeLayers.getLayers()
                .entrySet()
                .parallelStream()
                .map(branchOrders -> dijkstraRunner.computePathesForLayer(
                        scorer,
                        branchOrders.getKey(),
                        branchOrders.getValue(),
                        "BranchCodeParallel")
                )
                .reduce(this::merge)
                .orElse(new HashMap<>() );
    }

    /**
     * merge 2 hashmaps into 1
     * @param firstMap - HashMap <Node,List<Node>>
     * @param secondMap- HashMap <Node,List<Node>>
     * @return
     */
    private HashMap<Node,Route<Node>> merge(HashMap<Node,Route<Node>> firstMap,HashMap<Node,Route<Node>> secondMap) {
        firstMap.putAll(secondMap);
        return firstMap;
    }

}
