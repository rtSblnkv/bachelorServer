package com.diploma.logisticsService.service.data.dto.impl;

import com.diploma.logisticsService.models.csv.Branch;
import com.diploma.logisticsService.models.csv.Node;
import com.diploma.logisticsService.models.dto.NodeDTO;
import com.diploma.logisticsService.service.data.dto.BranchService;
import com.diploma.logisticsService.service.data.dto.NodeService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    @Setter
    private List<Branch> branches;

    /**
     * Converts List of branches into HashMap
     * @param nodeService - handler of nodes list
     * @return HashMap of ranch and its computed Node
     */
    @Override
    public HashMap<String, NodeDTO> toBranchNodeHashMap(NodeService nodeService)
    {
        HashMap<String, NodeDTO> branchNodes = new HashMap<>();
        branches.forEach(branch ->
        {
            NodeDTO branchNode = nodeService.getNodeByCoordinates(branch.getLatitude(),branch.getLongitude());
            branchNodes.put(branch.getBranchCode(),branchNode);
        });
        return branchNodes;
    }

    /**
     * Converts List of branches into HashMap (BranchCode, Branch)
     * @return HashMap (BranchCode, Branch)
     */
    @Override
    public HashMap<String, Branch> toHashMap() {
        HashMap<String,Branch> branchesHashMap = new HashMap<>();
        branches.forEach(branch -> branchesHashMap.put(branch.getBranchCode(),branch));
        return branchesHashMap;
    }
}
