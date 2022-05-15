package com.diploma.logisticsService.service.data.csv;


import org.springframework.stereotype.Component;

@Component
public class CsvLoaderFactory {
    /**
     * fabric method
     * @param tableName - name of table for parse
     * @return CsvLoader class
     */
    public CsvLoader createCsvLoader(String tableName)
    {
        switch(tableName)
        {
            case"branches": return new BranchCsvLoader();

            case"orders": return new OrderCsvLoader();

            case"edges": return new EdgeCsvLoader();

            case"nodes": return new NodeCsvLoader();

            default: return null;
        }
    }
}
