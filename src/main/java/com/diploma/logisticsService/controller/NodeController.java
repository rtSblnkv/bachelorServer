package com.diploma.logisticsService.controller;

import com.diploma.logisticsService.models.dto.NodeDTO;
import com.diploma.logisticsService.service.data.dto.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/nodes")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;

    @GetMapping(value = "/all")
    public ResponseEntity<List<NodeDTO>> listAll() {
        List<NodeDTO> nodes = nodeService.getAll();
        return ResponseEntity.ok(nodes);
    }
}
