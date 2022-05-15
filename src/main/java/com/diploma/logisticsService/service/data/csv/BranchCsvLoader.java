package com.diploma.logisticsService.service.data.csv;


import com.diploma.logisticsService.exceptions.UploadDataException;
import com.diploma.logisticsService.models.csv.Branch;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class BranchCsvLoader implements CsvLoader {
    /**
     * Uploads data from branches.csv
     * @param path - path to branches.csv in resources directory
     * @return List of Branch type
     */
    @Override
    public List<Branch> csvToList(String path) throws UploadDataException, IllegalArgumentException {
        List<Branch> branches = null;
        try(FileReader reader = new FileReader(path)){
            branches = new CsvToBeanBuilder(reader)
                    .withType(Branch.class)
                    .build()
                    .parse();
        }
        catch(IOException|NullPointerException ex)
        {
            String errMessage = "Can't be parsed : " + ex.getMessage();
            throw new UploadDataException( errMessage,ex);
        }
        if (branches == null || branches.isEmpty())
        {
            throw new IllegalArgumentException(" branches list is empty or null");
        }
        return branches;
    }

}
