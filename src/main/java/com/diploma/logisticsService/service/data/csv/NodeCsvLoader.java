package com.diploma.logisticsService.service.data.csv;


import com.diploma.logisticsService.exceptions.UploadDataException;
import com.diploma.logisticsService.models.csv.Node;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class NodeCsvLoader implements CsvLoader {
    /**
     * Uploads data from nodes.csv
     * @param path - path to nodes.csv in resources directory
     * @return List of Node type
     */
    @Override
    public List csvToList(String path) throws UploadDataException,IllegalArgumentException{
        List<Node> nodes = null;
        try(FileReader reader = new FileReader(path)){
            nodes = new CsvToBeanBuilder(reader)
                    .withType(Node.class)
                    .build()
                    .parse();
        }
        catch(IOException|NullPointerException ex)
        {
            String errMessage = "Can't be parsed : " + ex.getMessage();
            throw new UploadDataException( errMessage,ex);
        }
        if (nodes == null || nodes.isEmpty())
        {
            throw new IllegalArgumentException(" nodes list is empty or null");
        }

        return nodes;
    }
}
