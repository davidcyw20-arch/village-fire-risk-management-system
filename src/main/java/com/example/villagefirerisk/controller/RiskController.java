package com.example.villagefirerisk.controller;

import com.example.villagefirerisk.dto.ApiResponse;
import com.example.villagefirerisk.dto.RiskAreaResponse;
import com.example.villagefirerisk.service.RiskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/risk")
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    @GetMapping("/areas")
    @PreAuthorize("hasAnyRole('GRID','ADMIN')")
    public ApiResponse<List<RiskAreaResponse>> getAreaRisk() {
        return ApiResponse.success(riskService.calculateAreaRisk());
    }
}
