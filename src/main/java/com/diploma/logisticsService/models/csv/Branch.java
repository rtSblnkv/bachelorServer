package com.diploma.logisticsService.models.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

import java.io.Serializable;

/**
 * branches.csv Data Object
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Branch implements Serializable {

    private String branchCode;
    private double latitude;
    private double longitude;
}
