package com.diploma.logisticsService.utils.converters;


import com.diploma.logisticsService.models.csv.Route;
import com.diploma.logisticsService.models.dto.NodeDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NodesToJsonWriter {

    public String writePathToJson(Map<NodeDTO, Route<NodeDTO>> nodes) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonList = mapper.writeValueAsString(nodes);
        return  jsonList;
    }
}
