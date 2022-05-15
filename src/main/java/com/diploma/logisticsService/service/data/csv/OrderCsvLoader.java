package com.diploma.logisticsService.service.data.csv;


import com.diploma.logisticsService.exceptions.UploadDataException;
import com.diploma.logisticsService.models.csv.Order;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class OrderCsvLoader implements CsvLoader {
    /**
     * Uploads data from orders.csv
     * @param path - path to orders.csv in resources directory
     * @return List of Order type
     */
    @Override
    public List<Order> csvToList(String path) throws UploadDataException,IllegalArgumentException {
        List<Order> orders = null;
        try(FileReader reader = new FileReader(path)){
            orders = new CsvToBeanBuilder(reader)
                    .withType(Order.class)
                    .build()
                    .parse();
        }
        catch(IOException|NullPointerException ex)
        {
            String errMessage = "Can't be parsed : " + ex.getMessage();
            throw new UploadDataException( errMessage,ex);
        }
        if (orders == null || orders.isEmpty())
        {
            throw new IllegalArgumentException(" orders list is empty or null");
        }

        return orders;
    }
}
