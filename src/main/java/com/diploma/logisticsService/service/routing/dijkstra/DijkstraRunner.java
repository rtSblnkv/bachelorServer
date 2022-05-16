package com.diploma.logisticsService.service.routing.dijkstra;


import com.diploma.logisticsService.exceptions.InvalidNodeException;
import com.diploma.logisticsService.exceptions.NoShortPathException;
import com.diploma.logisticsService.models.csv.Branch;
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
import com.diploma.logisticsService.service.routing.scorers.impl.EdgeDistanceScorer;
import com.diploma.logisticsService.service.routing.scorers.impl.EdgeDistanceTrafficJamScorer;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.redlink.geocoding.LatLon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;
import org.springframework.stereotype.Component;

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
@Component
@RequiredArgsConstructor
@Slf4j
public class DijkstraRunner {

    private final NodeService nodeService;
    private final EdgeService edgeService;
    private final GeocodingService geocodingService;
    private final BranchService branchService;
    private final Dijkstra dijkstra;

    private NewNodeScorer<EdgeDTO> edgeScorer;

    public HashMap<NodeDTO, Route<NodeDTO>> computePathesForLayer(
            Branch branch,
            List<Order> orders,
            RoutingParams params
    ) {
        HashMap<NodeDTO, Route<NodeDTO>> shortPaths = new HashMap<>();
        if (params.isUseTrafficJamPoints()) {
            edgeScorer = new EdgeDistanceTrafficJamScorer<>();
        } else {
            edgeScorer = new EdgeDistanceScorer<>();
        }
        NodeDTO branchNode = branchService.getBranchNode(branch);
        dijkstra.computeMinDistancesfrom(branchNode, edgeScorer);
        orders.forEach(order -> {
            LatLon latLon = geocodingService.geocode(order.getAddress());
            NodeDTO nodeTo = null;
            try {
                nodeTo = nodeService.getNodeByCoordinates(latLon.lat(), latLon.lon());
                Route<NodeDTO> pathToCurrentNode = dijkstra.getShortestPathTo(nodeTo);
                shortPaths.put(nodeTo, pathToCurrentNode);
            } catch (InvalidNodeException ex) {
                log.error("Exception occured while node processing", ex);
            } catch (NoShortPathException ex) {
                double errNodeLat = ex.getUnattainableNode().getLatitude();
                double errNodeLon = ex.getUnattainableNode().getLongitude();
                log.debug("Can't calculate route for node with coordinates {},{}. No Short Path.", errNodeLat, errNodeLon);
                shortPaths.put(nodeTo, new Route<NodeDTO>());
            }
        });
        return shortPaths;
    }
}
