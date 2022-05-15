package com.diploma.logisticsService.models.routing;

import lombok.Getter;
import lombok.Setter;

import java.util.StringJoiner;

@Getter
@Setter
public class RoutingParams {
    private Double commonDistance;
    private boolean needsGeneralization;
    private boolean useTrafficJamPoints;

    private int streetType;

    public String toString() {
        return new StringJoiner("[", ",", "]")
                .add("commonDistance: " + this.commonDistance)
                .add("needsGeneralization: " + this.needsGeneralization)
                .add("streetType: " + this.streetType)
                .add("useTrafficJamPoints: " + this.useTrafficJamPoints)
                .toString();
    }
}
