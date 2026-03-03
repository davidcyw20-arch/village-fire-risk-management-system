package com.example.villagefirerisk.controller;

import com.example.villagefirerisk.aop.OperationLoggable;
import com.example.villagefirerisk.dto.ApiResponse;
import com.example.villagefirerisk.dto.HazardDtos;
import com.example.villagefirerisk.entity.Hazard;
import com.example.villagefirerisk.service.HazardService;
import com.example.villagefirerisk.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hazards")
public class HazardController {

    private final HazardService hazardService;

    public HazardController(HazardService hazardService) {
        this.hazardService = hazardService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RESIDENT','GRID','ADMIN')")
    @OperationLoggable(module = "Hazard", operation = "Report Hazard")
    public ApiResponse<Hazard> report(@Valid @RequestBody HazardDtos.ReportRequest request) {
        return ApiResponse.success(hazardService.report(request, SecurityUtil.getCurrentUsername()));
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    @OperationLoggable(module = "Hazard", operation = "Assign Hazard")
    public ApiResponse<Hazard> assign(@PathVariable Long id, @Valid @RequestBody HazardDtos.AssignRequest request) {
        return ApiResponse.success(hazardService.assign(id, request.getGridUserId(), SecurityUtil.getCurrentUsername()));
    }

    @PostMapping("/{id}/process")
    @PreAuthorize("hasAnyRole('GRID','ADMIN')")
    @OperationLoggable(module = "Hazard", operation = "Process Hazard")
    public ApiResponse<Hazard> process(@PathVariable Long id, @Valid @RequestBody HazardDtos.ProcessRequest request) {
        return ApiResponse.success(hazardService.process(id, request, SecurityUtil.getCurrentUsername()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('RESIDENT','GRID','ADMIN')")
    public ApiResponse<List<Hazard>> list() {
        return ApiResponse.success(hazardService.list());
    }
}
