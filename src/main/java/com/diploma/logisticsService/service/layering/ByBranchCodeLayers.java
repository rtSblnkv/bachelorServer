package com.diploma.logisticsService.service.layering;


import com.diploma.logisticsService.models.csv.Branch;
import com.diploma.logisticsService.models.csv.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ByBranchCodeLayers extends Layers {

    public ByBranchCodeLayers(List<Order> orders) {
        super(orders);
        setSplitter();
        splitOnLayers();
    }

    @Override
    public void setSplitter() {
        super.setSplitters(new ArrayList<>(
                super.getOrders()
                        .parallelStream()
                        .map(Order::getBranchCode)
                        .collect(Collectors.toSet())
        ));
    }

    @Override
    public void splitOnLayers() {
        super.getSplitters()
                .forEach(branchCode -> super.getLayers()
                        .put(branchCode, new ArrayList<Order>()));

        super.getOrders()
                .forEach(order -> super.getLayers()
                        .get(order.getBranchCode())
                        .add(order));
    }
}
