package com.example.villagefirerisk.controller;

import com.example.villagefirerisk.dto.ApiResponse;
import com.example.villagefirerisk.entity.KnowledgeArticle;
import com.example.villagefirerisk.entity.RiskLevel;
import com.example.villagefirerisk.entity.Role;
import com.example.villagefirerisk.service.KnowledgeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @GetMapping("/recommend")
    @PreAuthorize("hasAnyRole('RESIDENT','GRID','ADMIN')")
    public ApiResponse<List<KnowledgeArticle>> recommend(@RequestParam Role role, @RequestParam RiskLevel riskLevel) {
        return ApiResponse.success(knowledgeService.recommend(role, riskLevel));
    }
}
