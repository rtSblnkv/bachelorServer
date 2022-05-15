package com.diploma.logisticsService.service.data.dto;

import com.diploma.logisticsService.models.csv.Order;

import java.util.HashMap;

public interface OrderService {
    HashMap<String, Order> toHashMap();
}
