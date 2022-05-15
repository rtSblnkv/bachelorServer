package com.diploma.logisticsService.service.data.dto;

import com.diploma.logisticsService.models.csv.Branch;
import com.diploma.logisticsService.models.dto.NodeDTO;

import java.util.HashMap;

public interface BranchService {
    HashMap<String, NodeDTO> toBranchNodeHashMap(NodeService nodeService);

    HashMap<String, Branch> toHashMap();
}
