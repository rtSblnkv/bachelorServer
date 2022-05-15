package com.diploma.logisticsService.service.data.dto.impl;

import com.diploma.logisticsService.models.csv.Order;
import com.diploma.logisticsService.service.data.dto.OrderService;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Setter
    private List<Order> orders;

    /**
     * Returns list of branch codes from list of orders (Order.BranchCode)
     * @return list of branch codes
     */
    public List<String> getBranchCodes()
    {
        return orders
                .parallelStream()
                .distinct()
                .map(Order::getBranchCode)
                .collect(Collectors.toList()
                );
    }

    /**
     * Returns list of order types from list of orders (Order.OrderType)
     * @return list of order types
     */
    public List<String> getOrderTypes()
    {
        return orders
                .parallelStream()
                .distinct()
                .map(Order::getOrderType)
                .collect(Collectors.toList());
    }

    /**
     * Converts list of orders into HashMap
     * @return HashMap ( order ID, order)
     */
    @Override
    public HashMap<String, Order> toHashMap() {
        HashMap<String,Order> ordersHashMap = new HashMap<>();
        orders.forEach(order -> ordersHashMap.put(order.getOrderId(),order));
        return ordersHashMap;
    }
}
