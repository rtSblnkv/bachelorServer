package com.diploma.logisticsService.models.csv;

import com.diploma.logisticsService.models.dto.NodeDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class Route<T extends NodeDTO> {

    private double routeScore;

    private double epsilon;

    private List<T> route;

    private String branchCode;
}
