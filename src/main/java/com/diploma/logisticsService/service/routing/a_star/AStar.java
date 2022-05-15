package com.diploma.logisticsService.service.routing.a_star;

import com.diploma.logisticsService.exceptions.NoShortPathException;
import com.diploma.logisticsService.models.csv.Route;
import com.diploma.logisticsService.models.dto.EdgeDTO;
import com.diploma.logisticsService.models.dto.NodeDTO;
import com.diploma.logisticsService.models.routing.RouteNode;
import com.diploma.logisticsService.service.graph.GraphServiceImpl;
import com.diploma.logisticsService.service.routing.scorers.NewNodeScorer;
import com.diploma.logisticsService.service.routing.scorers.TargetScorer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Component
public class AStar <T extends NodeDTO> implements RouteFinder<T> {

    private final NewNodeScorer<EdgeDTO> nextNodeTargetScorer;
    private final TargetScorer<T> targetScorer;
    private final GraphServiceImpl graphService;

    /**
     * Returns shortest path to Node nodeTo from node,
     * for which computeMinDistancesFrom was launched
     * @param nodeFrom - start point
     * @param nodeTo - The final point to compute short path for
     * @return list of nodes, which contains the shortest path to nodeTo
     */
    public Route<T> getRoute(T nodeFrom, T nodeTo, NewNodeScorer<EdgeDTO> nextNodeTargetScorer, TargetScorer<T> targetScorer) throws NoShortPathException
    {
        Queue<RouteNode<T>> openSet = new PriorityQueue<>();
        Map<Long,RouteNode<T>> allNodes = new HashMap<>();

        RouteNode<T> start = new RouteNode<>(
                nodeFrom,
                null,
                0,
                targetScorer.computeCost(nodeFrom,nodeTo)
        );

        openSet.add(start);
        allNodes.put(nodeFrom.getId(),start);

        while(!openSet.isEmpty()){
            RouteNode<T> next = openSet.poll();
            if (next.getCurrent().equals(nodeTo)) {
                List<T> route = new ArrayList<>();
                RouteNode<T> current = next;
                double score = current.getRouteScore();

                for(;;  current = allNodes.get(current.getPrevious().getId())){
                    route.add(0, current.getCurrent());
                    if(current.getPrevious() == null) break;
                }

                Route<T> result = new Route<T>();
                result.setRouteScore(score);
                result.setRoute(route);
                return result;
            }
            List<EdgeDTO> curNodeEdges = graphService.get(next.getCurrent());
            if(curNodeEdges != null){
                curNodeEdges.forEach(edge -> {
                    if(graphService.nodeWithIdExist(edge.getTo().getId())){
                        T curNodeTo = (T) edge.getTo();
                        RouteNode<T> nextNode = allNodes.getOrDefault(curNodeTo.getId(), new RouteNode<T>(curNodeTo));
                        allNodes.put(curNodeTo.getId(), nextNode);

                        double newScore = next.getRouteScore() + nextNodeTargetScorer.computeCost(edge);
                        if (newScore < nextNode.getRouteScore()) {
                            nextNode.setPrevious(next.getCurrent());
                            nextNode.setRouteScore(newScore);
                            nextNode.setEstimatedScore(newScore + targetScorer.computeCost((T)edge.getTo(), nodeTo));
                            openSet.add(nextNode);
                        }
                    }
                });
            }
        }
        throw new NoShortPathException("No route found", start.getCurrent());
    }


    public Route<T> getRoute(T nodeFrom, T nodeTo) throws NoShortPathException {
        return getRoute(nodeFrom,nodeTo,nextNodeTargetScorer,targetScorer);
    }
}
