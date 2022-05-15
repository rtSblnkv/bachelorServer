package com.diploma.logisticsService.service.data.dto;

import com.diploma.logisticsService.models.csv.Order;

import java.util.HashMap;
import java.util.List;

public interface OrderService {
    List<String> getBranchCodes(List<Order> orders);
    List<String> getOrderTypes(List<Order> orders);

}
