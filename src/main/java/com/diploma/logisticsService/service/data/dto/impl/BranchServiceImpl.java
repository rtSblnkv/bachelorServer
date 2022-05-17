package com.diploma.logisticsService.service.data.dto.impl;

import com.diploma.logisticsService.models.csv.Branch;
import com.diploma.logisticsService.models.dto.NodeDTO;
import com.diploma.logisticsService.service.data.dto.BranchService;
import com.diploma.logisticsService.service.data.dto.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final NodeService nodeService;

    /**
     * Converts List of branches into HashMap
     * @return HashMap of branch and its computed Node
     */
    @Override
    public HashMap<String, NodeDTO> toBranchNodeHashMap(List<Branch> branches)
    {
        HashMap<String, NodeDTO> branchNodes = new HashMap<>();
        branches.forEach(branch ->
        {
            NodeDTO branchNode = nodeService.getNodeByCoordinates(branch.getLatitude(),branch.getLongitude());
            branchNodes.put(branch.getBranchCode(),branchNode);
        });
        return branchNodes;
    }

    @Override
    public NodeDTO getBranchNode(Branch branch){
        HashMap<String, NodeDTO> branchNodes = toBranchNodeHashMap(
                Collections.singletonList(branch)
        );
        return branchNodes.get(branch.getBranchCode());
    }

    @Override
    public Branch getByBranchCode(List<Branch> branches, String branchCode){
        return branches
                .stream()
                .filter(branch -> branch.getBranchCode().equals(branchCode))
                .findFirst()
                .orElse(null);
    }
}
