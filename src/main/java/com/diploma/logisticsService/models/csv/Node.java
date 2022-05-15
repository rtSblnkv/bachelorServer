package com.diploma.logisticsService.models.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

import java.io.Serializable;

/**
 * nodes.csv Data Object
 */
@Getter
@Setter
@NoArgsConstructor
public class Node implements Serializable, Cloneable {

    @CsvBindByName(column = "node")
    private long id;

    @CsvBindByName(column = "lat")
    private double latitude;

    @CsvBindByName(column = "lon")
    private double longitude;

    @Override
    public String toString() {
        return "[" + latitude +
                "," + longitude + "]";
    }

    public Node clone() {
        Node node = new Node();
        node.id = id;
        node.longitude = longitude;
        node.latitude = latitude;
        return node;
    }
}
