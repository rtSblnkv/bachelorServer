package com.diploma.logisticsService.service.routing.a_star;


import com.diploma.logisticsService.exceptions.InvalidNodeException;
import com.diploma.logisticsService.exceptions.NoShortPathException;
import com.diploma.logisticsService.models.csv.Branch;
import com.diploma.logisticsService.models.csv.Node;
import com.diploma.logisticsService.models.csv.Order;
import com.diploma.logisticsService.models.csv.Route;
import com.diploma.logisticsService.models.dto.EdgeDTO;
import com.diploma.logisticsService.models.dto.NodeDTO;
import com.diploma.logisticsService.models.routing.RoutingParams;
import com.diploma.logisticsService.service.data.dto.BranchService;
import com.diploma.logisticsService.service.data.dto.EdgeService;
import com.diploma.logisticsService.service.data.dto.NodeService;
import com.diploma.logisticsService.service.geocoding.GeocodingService;
import com.diploma.logisticsService.service.routing.scorers.NewNodeScorer;
import com.diploma.logisticsService.service.routing.scorers.TargetScorer;
import com.diploma.logisticsService.service.routing.scorers.impl.DistanceTargetScorer;
import com.diploma.logisticsService.service.routing.scorers.impl.EdgeDistanceScorer;
import com.diploma.logisticsService.service.routing.scorers.impl.EdgeDistanceTrafficJamScorer;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.redlink.geocoding.LatLon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@RequiredArgsConstructor
@Slf4j
public class RouteFinderRunner {

    private final NodeService nodeService;
    private final BranchService branchService;
    private final AStar<NodeDTO> routeFinder;

    private final GeocodingService geocodingService;

    private NewNodeScorer<EdgeDTO> edgeScorer;
    private TargetScorer<NodeDTO> targetScorer;

    /**
     * computes pathes for list of orders with claculating of dijkstra for each order
     * @param orders - order list
     * @return map of node and list of nodes as the shortest path to it from departure node
     */
    public HashMap<NodeDTO, Route<NodeDTO>> computePathes(
            List<Order> orders,
            List<Branch> branches,
            RoutingParams params) {

        if(params.isUseTrafficJamPoints()){
            edgeScorer = new EdgeDistanceTrafficJamScorer<>();
        }
        else{
            edgeScorer = new EdgeDistanceScorer<>();
        }
        targetScorer = new DistanceTargetScorer<>();

        Map<String,NodeDTO> branchNodes = branchService.toBranchNodeHashMap(branches);
        HashMap<NodeDTO,Route<NodeDTO>> shortPaths = new HashMap<>();
        orders.forEach(order -> {
            LatLon latLon = geocodingService.geocode(order.getAddress());
            NodeDTO nodeTo = null;
            try {
                NodeDTO startNode = branchNodes.get(order.getBranchCode().toUpperCase());
                nodeTo = nodeService.getNodeByCoordinates(latLon.lat(), latLon.lon());
                if(nodeTo == null){
                    log.error("node To is null for lat,lon = {},{}",latLon.lat(), latLon.lon());
                    return;
                }
                Route<NodeDTO> pathToCurrentNode = routeFinder.getRoute(
                        startNode,
                        nodeTo,
                        edgeScorer,
                        targetScorer);
                shortPaths.put(nodeTo, pathToCurrentNode);
            }
            catch(InvalidNodeException ex)
            {
                log.error("Exception occured while node processing",ex);
            }
            catch(NoShortPathException ex)
            {
                double errNodeLat = ex.getUnattainableNode().getLatitude();
                double errNodeLon = ex.getUnattainableNode().getLongitude();
                log.debug("Can't calculate route for node with coordinates {},{}. No Short Path.",errNodeLat,errNodeLon);
                shortPaths.put(nodeTo,new Route<NodeDTO>());
            }
        });
        return shortPaths;
    }
}
