package com.diploma.logisticsService.service.layering;


import com.diploma.logisticsService.models.csv.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class,which realises splitting the list of orders on sublists,
 * or layers, by order type
 */
public class ByOrderTypeLayers extends Layers {

    public ByOrderTypeLayers(List<Order> orders) {
        super(orders);
        setSplitter();
        splitOnLayers();
    }

    @Override
    public void setSplitter() {
        super.setSplitters(new ArrayList<>(
                super.getOrders()
                        .parallelStream()
                        .distinct()
                        .map(Order::getOrderType)
                        .collect(Collectors.toList())
        ));
    }

    @Override
    public void splitOnLayers() {
        super.getSplitters()
                .forEach(orderType ->
                        super.getLayers()
                                .put(orderType, new ArrayList<Order>()));

        super.getOrders().forEach(order ->
                super.getLayers()
                        .get(order.getOrderType())
                        .add(order));
    }
}
