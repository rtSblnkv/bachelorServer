package com.diploma.logisticsService.models.routing;

import com.diploma.logisticsService.models.dto.NodeDTO;
import lombok.Data;


@Data
public class RouteNode<T extends NodeDTO> implements Comparable<RouteNode<T>> {
    private final T current;
    private T previous;
    private double routeScore;
    private double estimatedScore;

    public RouteNode(T current) {
        this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public RouteNode(T current, T previous, double routeScore, double estimatedScore) {
        this.current = current;
        this.previous = previous;
        this.routeScore = routeScore;
        this.estimatedScore = estimatedScore;
    }

    @Override
    public int compareTo(RouteNode o) {
        if (this.estimatedScore > o.estimatedScore) {
            return 1;
        } else if (this.estimatedScore < o.estimatedScore) {
            return -1;
        } else {
            return 0;
        }
    }
}
