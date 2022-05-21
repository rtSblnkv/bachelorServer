package com.diploma.logisticsService.utils;

import com.diploma.logisticsService.service.graph.GraphService;
import com.diploma.logisticsService.utils.converters.CsvToDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final CsvToDTOConverter converter;

    private final GraphService graphService;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String isInitializing;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(!isInitializing.equals("none")){
            converter.csvToDb();
        }
        graphService.getGraph();
    }
}
