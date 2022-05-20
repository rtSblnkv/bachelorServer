package com.diploma.logisticsService.controller;

import com.diploma.logisticsService.models.dto.EdgeDTO;
import com.diploma.logisticsService.service.data.dto.EdgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/edges")
@RequiredArgsConstructor
public class EdgeController {

    private final EdgeService edgeService;

    @GetMapping("/all")
    public ResponseEntity<List<EdgeDTO>> listAll(){
        List<EdgeDTO> edges = edgeService.getAll();
        return ResponseEntity.ok(edges);
    }
}
