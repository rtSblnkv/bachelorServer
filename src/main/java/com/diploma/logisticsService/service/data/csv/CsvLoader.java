package com.diploma.logisticsService.service.data.csv;


import com.diploma.logisticsService.exceptions.UploadDataException;

import java.util.List;

public interface CsvLoader {
    List csvToList(String path) throws UploadDataException;
}
