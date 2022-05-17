package com.diploma.logisticsService.service.data.dto;

import com.diploma.logisticsService.models.csv.Branch;
import com.diploma.logisticsService.models.dto.NodeDTO;

import java.util.HashMap;
import java.util.List;

public interface BranchService {
    HashMap<String, NodeDTO> toBranchNodeHashMap(List<Branch> branches);

    NodeDTO getBranchNode(Branch branch);

    Branch getByBranchCode(List<Branch> branches, String branchCode);
}
