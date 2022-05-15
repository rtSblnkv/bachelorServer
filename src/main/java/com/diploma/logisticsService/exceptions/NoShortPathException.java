package com.diploma.logisticsService.exceptions;

import com.diploma.logisticsService.models.csv.Node;
import com.diploma.logisticsService.models.dto.NodeDTO;
import lombok.Getter;

/**
 * Thrown when no any path to the node from current node
 * Param - unattainable node
 */
@Getter
public class NoShortPathException extends RuntimeException {

    private NodeDTO unattainableNode;

    public NoShortPathException(String errMessage, NodeDTO unattainableNode) {
        super(errMessage);
        this.unattainableNode = unattainableNode;
    }
}
