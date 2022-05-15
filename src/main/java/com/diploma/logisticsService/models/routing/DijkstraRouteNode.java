package com.diploma.logisticsService.models.routing;

import com.diploma.logisticsService.models.csv.Node;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class DijkstraRouteNode<T extends Node> implements Comparable<DijkstraRouteNode<T>> {

    private final T currentNode;

    private T previousNode;

    private boolean visited;

    private double epsilon;

    private double minDistance;

    public DijkstraRouteNode(T currentNode) {
        this(currentNode, null, Double.MAX_VALUE);
    }

    public DijkstraRouteNode(T currentNode, T previousNode, double minDistance) {
        this.currentNode = currentNode;
        this.previousNode = previousNode;
        this.minDistance = minDistance;
    }

    @Override
    public int compareTo(DijkstraRouteNode<T> o) {
        return Double.compare(this.minDistance, o.getMinDistance());
    }
}
