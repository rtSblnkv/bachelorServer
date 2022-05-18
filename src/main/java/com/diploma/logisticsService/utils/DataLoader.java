package com.diploma.logisticsService.utils;

import com.diploma.logisticsService.utils.converters.CsvToDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final CsvToDTOConverter converter;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        converter.csvToDb();
    }
}
