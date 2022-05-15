package com.diploma.logisticsService.service.data.dto.impl;

import com.diploma.logisticsService.models.csv.Order;
import com.diploma.logisticsService.service.data.dto.OrderService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    /**
     * Returns list of branch codes from list of orders (Order.BranchCode)
     * @return list of branch codes
     */

    @Override
    public List<String> getBranchCodes(List<Order> orders)
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
    @Override
    public List<String> getOrderTypes(List<Order> orders)
    {
        return orders
                .parallelStream()
                .distinct()
                .map(Order::getOrderType)
                .collect(Collectors.toList());
    }
}
