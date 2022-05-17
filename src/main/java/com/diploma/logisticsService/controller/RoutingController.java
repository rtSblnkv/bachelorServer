package com.diploma.logisticsService.controller;

import com.diploma.logisticsService.models.csv.Branch;
import com.diploma.logisticsService.models.csv.Order;
import com.diploma.logisticsService.models.routing.RoutingParams;
import com.diploma.logisticsService.service.routing.a_star.AStarRunner;
import com.diploma.logisticsService.service.routing.dijkstra.DijkstraRunner;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmh.annotations.Benchmark;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController(value="/router")
@RequiredArgsConstructor
public class RoutingController {

    @PostMapping("/getRoutes")
    public void getRoutes(
            @RequestBody List<Order> orders,
            @RequestBody List<Branch> branches,
            @RequestBody RoutingParams params){

    }
}
