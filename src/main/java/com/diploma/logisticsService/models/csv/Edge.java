package com.diploma.logisticsService.models.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;
import lombok.*;

import java.io.Serializable;

/**
 * edges.csv Data Object
 */
@Getter
@Setter
@NoArgsConstructor
public class Edge implements Serializable {

    @CsvBindByName(column = "u")
    private long from;

    @CsvBindByName(column = "v")
    private long to;

    @CsvBindByName(column = "distance(m)")
    private double distance;

    @CsvBindByName(column = "speed(km/h)")
    private double speedLimit;

    @CsvBindByName(column = "street_type")
    private int streetType; // 1 - общ 2 - ограничение для грузовиков
}
