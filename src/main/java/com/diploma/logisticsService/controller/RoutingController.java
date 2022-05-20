package com.diploma.logisticsService.controller;

import com.diploma.logisticsService.models.csv.Branch;
import com.diploma.logisticsService.models.csv.Order;
import com.diploma.logisticsService.models.csv.Route;
import com.diploma.logisticsService.models.dto.NodeDTO;
import com.diploma.logisticsService.models.routing.RoutingParams;
import com.diploma.logisticsService.service.routing.RoutingService;
import com.diploma.logisticsService.service.routing.a_star.AStarRunner;
import com.diploma.logisticsService.service.routing.dijkstra.DijkstraRunner;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmh.annotations.Benchmark;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/router")
@RequiredArgsConstructor
public class RoutingController {
    private final RoutingService routingService;

    @PostMapping("/getRoutes")
    public ResponseEntity<List<Route<NodeDTO>>> getRoutes(
            @RequestBody List<Order> orders,
            @RequestBody List<Branch> branches,
            @RequestBody RoutingParams params) {
        if (branches == null || branches.isEmpty()) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
        if (orders == null || orders.isEmpty()) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
        if (params == null) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
        HashMap<NodeDTO, Route<NodeDTO>> routes;
        try {
            routes = routingService.getShortestForOrdersByBranchCodeParallelPreliminary(
                    orders, branches, params);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
        List<Route<NodeDTO>> response = new ArrayList<>(routes.values());
        return ResponseEntity.ok(response);
    }
}
