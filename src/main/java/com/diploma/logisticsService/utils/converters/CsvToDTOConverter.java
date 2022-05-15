package com.diploma.logisticsService.utils.converters;

import com.diploma.logisticsService.models.csv.Edge;
import com.diploma.logisticsService.models.csv.Node;
import com.diploma.logisticsService.service.data.csv.CsvDataService;
import com.diploma.logisticsService.service.data.dto.EdgeService;
import com.diploma.logisticsService.service.data.dto.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class CsvToDTOConverter {

    private final NodeService nodeService;
    private final EdgeService edgeService;
    private final CsvDataService csvDataService;

    public void csvToDb(){
        List<Node> nodes = csvDataService.getNodes();
        List<Edge> edges = csvDataService.getEdges();
        nodeService.saveAll(nodes);
        edgeService.saveAll(edges);
    }

}
