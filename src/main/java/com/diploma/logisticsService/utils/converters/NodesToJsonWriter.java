package com.diploma.logisticsService.utils.converters;


import com.diploma.logisticsService.exceptions.WriteResultException;
import com.diploma.logisticsService.models.csv.Node;
import com.diploma.logisticsService.models.csv.Route;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Component
public class NodesToJsonWriter {

    public void writePathToJson(String fileName, Map<Node, Route<Node>> nodes) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonList = mapper.writeValueAsString(nodes);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(jsonList);
        } catch (IOException ex) {
            String errMessage = "Error while writing in " + fileName + "." + ex.getMessage();
            throw new WriteResultException(errMessage, ex);
        }
    }
}
