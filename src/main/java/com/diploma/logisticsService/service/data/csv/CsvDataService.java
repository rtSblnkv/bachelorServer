package com.diploma.logisticsService.service.data.csv;


import com.diploma.logisticsService.exceptions.UploadDataException;
import com.diploma.logisticsService.models.csv.Branch;
import com.diploma.logisticsService.models.csv.Edge;
import com.diploma.logisticsService.models.csv.Node;
import com.diploma.logisticsService.models.csv.Order;
import com.diploma.logisticsService.utils.FileURLDecoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

/**
 * Uploads data from dataset in lists of appropriate Objects
 */
//@State(Scope.Benchmark)
@Service
@RequiredArgsConstructor
@Slf4j
public class CsvDataService {

    private final FileURLDecoder fileURLDecoder;
    private final CsvLoaderFactory loaderFactory;
    @Getter
    private List<Order> orders;
    @Getter
    private List<Node> nodes;
    @Getter
    private List<Edge> edges;
    @Getter
    private List<Branch> branches;
    private final HashMap<String, String> csvPaths = new HashMap<>();

    @PostConstruct
    void init(){
        csvPaths.put("branches", fileURLDecoder.getPathToResource("branches.csv"));
        csvPaths.put("edges",fileURLDecoder.getPathToResource("edges.csv"));
        csvPaths.put("nodes",fileURLDecoder.getPathToResource("nodes.csv"));
        csvPaths.put("orders",fileURLDecoder.getPathToResource("orders.csv"));
    }

    /**
     * Uploads data from dataset with csvToList methods
     * initialize branches,edges,nodes,orders.
     * @throws UploadDataException
     */
    //@Benchmark
    public void uploadDataFromCsvFiles() throws UploadDataException
    {
       try {
            CsvLoader branchLoader = loaderFactory.createCsvLoader("branches");
            branches = branchLoader.csvToList(csvPaths.get("branches"));

           log.debug("branches " + branches.size());

            CsvLoader edgeLoader = loaderFactory.createCsvLoader("edges");
            edges = edgeLoader.csvToList(csvPaths.get("edges"));

            log.debug("edges " + edges.size());

            CsvLoader nodeLoader = loaderFactory.createCsvLoader("nodes");
            nodes = nodeLoader.csvToList(csvPaths.get("nodes"));

            log.debug("nodes " + nodes.size());

            CsvLoader orderLoader = loaderFactory.createCsvLoader("orders");
            orders = orderLoader.csvToList(csvPaths.get("orders"));

           log.debug("orders " + orders.size());
        }
        catch (UploadDataException | IllegalArgumentException ex)
        {
            throw new UploadDataException(ex.getMessage(),ex);
        }
    }
}
