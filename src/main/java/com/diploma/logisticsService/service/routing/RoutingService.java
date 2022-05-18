package com.diploma.logisticsService.service.routing;

import com.diploma.logisticsService.models.csv.Branch;
import com.diploma.logisticsService.models.csv.Order;
import com.diploma.logisticsService.models.csv.Route;
import com.diploma.logisticsService.models.dto.EdgeDTO;
import com.diploma.logisticsService.models.dto.NodeDTO;
import com.diploma.logisticsService.models.routing.RoutingParams;
import com.diploma.logisticsService.service.data.dto.BranchService;
import com.diploma.logisticsService.service.layering.ByBranchCodeLayers;
import com.diploma.logisticsService.service.layering.ByOrderTypeLayers;
import com.diploma.logisticsService.service.routing.a_star.AStarRunner;
import com.diploma.logisticsService.service.routing.dijkstra.DijkstraRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Benchmark;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoutingService {

    private final AStarRunner aStarRunner;
    private final DijkstraRunner dijkstraRunner;

    private final BranchService branchService;

    /**
     * Linear computing of shortest pathes and distances for each order in full list of orders
     *
     * @return Map(Node, lists, which contains shortest path to the Node from restaurant)
     */
    @Benchmark
    public HashMap<NodeDTO, Route<NodeDTO>> getShortestForAllOrders(
            List<Order> orders,
            List<Branch> branches,
            RoutingParams params
    ) {
        log.debug("Linear All orders {}, branches {}, params {}", orders, branches, params);
        return aStarRunner.computePathes(orders, branches, params);
    }

    /**
     * Parallel computing of shortest pathes and distances for each order in list orders splitted by order type
     *
     * @return Map(Node, lists, which contains shortest path to the Node from restaurant)
     */
    @Benchmark
    public HashMap<NodeDTO, Route<NodeDTO>> getShortestForAllOrdersByOrderTypeParallel(
            List<Order> orders,
            List<Branch> branches,
            RoutingParams params
    ) {
        ByOrderTypeLayers byOrderTypeLayers = new ByOrderTypeLayers(orders);
        log.debug("Order Type Parallel orders {}, branches {}, params {}", orders, branches, params);
        return byOrderTypeLayers.getLayers()
                .entrySet()
                .parallelStream()
                .map(branchOrders -> aStarRunner.computePathes(
                        branchOrders.getValue(),
                        branches,
                        params))
                .reduce(this::merge)
                .orElse(new HashMap<>());
    }


    /**
     * Parallel computing of shortest pathes and distances for each order in list orders splitted by branch code
     *
     * @return Map(Node, lists, which contains shortest path to the Node from restaurant)
     */
    @Benchmark
    public HashMap<NodeDTO, Route<NodeDTO>> getShortestForAllOrdersByBranchCodeParallel(
            List<Order> orders,
            List<Branch> branches,
            RoutingParams params
    ) {
        ByBranchCodeLayers byBranchCodeLayers = new ByBranchCodeLayers(orders);
        log.debug("Branch Code Parallel orders {}, branches {}, params {}", orders, branches, params);
        return byBranchCodeLayers.getLayers()
                .entrySet()
                .parallelStream()
                .map(branchOrders -> aStarRunner.computePathes(
                        branchOrders.getValue(),
                        branches,
                        params))
                .reduce(this::merge)
                .orElse(new HashMap<>());
    }

    /**
     * Parallel computing of shortest pathes and distances for each order in list orders splitted by branch code
     * Using computePathesForLayer method
     *
     * @return Map(Node, lists, which contains shortest path to the Node from restaurant)
     */
    @Benchmark
    public HashMap<NodeDTO, Route<NodeDTO>> getShortestForOrdersByBranchCodeParallelPreliminary(
            List<Order> orders,
            List<Branch> branches,
            RoutingParams params
    ) {
        ByBranchCodeLayers byBranchCodeLayers = new ByBranchCodeLayers(orders);
        log.debug("Branch Code Parallel Dijkstra orders {}, branches {}, params {}", orders, branches, params);
        return byBranchCodeLayers.getLayers()
                .entrySet()
                .parallelStream()
                .map(branchOrders -> {
                    Branch branch = branchService.getByBranchCode(branches, branchOrders.getKey());
                    return dijkstraRunner.computePathesForLayer(
                            branch,
                            branchOrders.getValue(),
                            params);
                })
                .reduce(this::merge)
                .orElse(new HashMap<>());
    }

    /**
     * merge 2 hashmaps into 1
     *
     * @param firstMap   - HashMap <Node,List<Node>>
     * @param secondMap- HashMap <Node,List<Node>>
     * @return
     */
    private HashMap<NodeDTO, Route<NodeDTO>> merge(
            HashMap<NodeDTO, Route<NodeDTO>> firstMap,
            HashMap<NodeDTO, Route<NodeDTO>> secondMap) {
        firstMap.putAll(secondMap);
        return firstMap;
    }
}
