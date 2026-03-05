package com.example.villagefirerisk.controller;

import com.example.villagefirerisk.aop.OperationLoggable;
import com.example.villagefirerisk.dto.AdminConfigDtos;
import com.example.villagefirerisk.dto.ApiResponse;
import com.example.villagefirerisk.entity.DataDictionaryItem;
import com.example.villagefirerisk.entity.KnowledgeArticle;
import com.example.villagefirerisk.entity.OperationLog;
import com.example.villagefirerisk.entity.PushRule;
import com.example.villagefirerisk.service.AdminOpsService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOpsController {

    private final AdminOpsService adminOpsService;

    public AdminOpsController(AdminOpsService adminOpsService) {
        this.adminOpsService = adminOpsService;
    }

    @GetMapping("/hazards/monitor")
    public ApiResponse<List<com.example.villagefirerisk.entity.Hazard>> listHazardsForMonitor() {
        return ApiResponse.success(adminOpsService.listHazardsForMonitor());
    }

    @PostMapping("/hazards/{id}/urge")
    @OperationLoggable(module = "AdminHazard", operation = "Urge Hazard")
    public ApiResponse<com.example.villagefirerisk.entity.NotificationLog> urgeHazard(@PathVariable Long id,
                                                                                       @RequestBody(required = false) AdminConfigDtos.UrgeRequest request) {
        String content = request == null ? null : request.getContent();
        return ApiResponse.success(adminOpsService.urgeHazard(id, content));
    }

    @GetMapping("/system-config")
    public ApiResponse<Map<String, String>> getSystemConfig() {
        return ApiResponse.success(adminOpsService.getSystemConfig());
    }

    @PutMapping("/system-config")
    @OperationLoggable(module = "AdminConfig", operation = "Save System Config")
    public ApiResponse<Map<String, String>> saveSystemConfig(@Valid @RequestBody AdminConfigDtos.SystemConfigRequest request) {
        return ApiResponse.success(adminOpsService.saveSystemConfig(request));
    }

    @GetMapping("/risk-model")
    public ApiResponse<Map<String, String>> getRiskModel() {
        return ApiResponse.success(adminOpsService.getRiskModel());
    }

    @PostMapping("/risk-warnings/scan")
    @OperationLoggable(module = "AdminRiskModel", operation = "Scan Risk Warnings")
    public ApiResponse<AdminConfigDtos.RiskWarningScanResponse> scanRiskWarnings() {
        return ApiResponse.success(adminOpsService.scanRiskWarnings());
    }

    @PutMapping("/risk-model")
    @OperationLoggable(module = "AdminRiskModel", operation = "Save Risk Model")
    public ApiResponse<Map<String, String>> saveRiskModel(@Valid @RequestBody AdminConfigDtos.RiskModelRequest request) {
        return ApiResponse.success(adminOpsService.saveRiskModel(request));
    }

    @GetMapping("/dict-items")
    public ApiResponse<List<DataDictionaryItem>> listDictItems() {
        return ApiResponse.success(adminOpsService.listDict());
    }

    @PostMapping("/dict-items")
    @OperationLoggable(module = "AdminDict", operation = "Add Dict Item")
    public ApiResponse<DataDictionaryItem> addDictItem(@Valid @RequestBody AdminConfigDtos.DictItemRequest request) {
        return ApiResponse.success(adminOpsService.addDict(request));
    }

    @DeleteMapping("/dict-items/{id}")
    @OperationLoggable(module = "AdminDict", operation = "Delete Dict Item")
    public ApiResponse<Void> deleteDictItem(@PathVariable Long id) {
        adminOpsService.deleteDict(id);
        return ApiResponse.success(null);
    }

    @GetMapping("/push-rules")
    public ApiResponse<List<PushRule>> listPushRules() {
        return ApiResponse.success(adminOpsService.listPushRules());
    }

    @PostMapping("/push-rules")
    @OperationLoggable(module = "AdminPushRule", operation = "Create Push Rule")
    public ApiResponse<PushRule> createPushRule(@Valid @RequestBody AdminConfigDtos.PushRuleRequest request) {
        return ApiResponse.success(adminOpsService.addPushRule(request));
    }

    @PatchMapping("/push-rules/{id}/enabled")
    @OperationLoggable(module = "AdminPushRule", operation = "Toggle Push Rule")
    public ApiResponse<PushRule> togglePushRule(@PathVariable Long id,
                                                @Valid @RequestBody AdminConfigDtos.PushRuleToggleRequest request) {
        return ApiResponse.success(adminOpsService.togglePushRule(id, request.getEnabled()));
    }

    @GetMapping("/audit-logs")
    public ApiResponse<List<OperationLog>> listAuditLogs(@RequestParam(required = false) String keyword) {
        return ApiResponse.success(adminOpsService.listAuditLogs(keyword));
    }

    @DeleteMapping("/audit-logs")
    @OperationLoggable(module = "AdminAudit", operation = "Clear Audit Logs")
    public ApiResponse<Void> clearAuditLogs() {
        adminOpsService.clearAuditLogs();
        return ApiResponse.success(null);
    }

    @GetMapping("/knowledge-articles")
    public ApiResponse<List<KnowledgeArticle>> listKnowledgeArticles() {
        return ApiResponse.success(adminOpsService.listKnowledgeArticles());
    }

    @PostMapping("/knowledge-articles")
    @OperationLoggable(module = "AdminKnowledge", operation = "Create Knowledge")
    public ApiResponse<KnowledgeArticle> createKnowledge(@Valid @RequestBody AdminConfigDtos.KnowledgeUpsertRequest request) {
        return ApiResponse.success(adminOpsService.createKnowledge(request));
    }

    @PutMapping("/knowledge-articles/{id}")
    @OperationLoggable(module = "AdminKnowledge", operation = "Update Knowledge")
    public ApiResponse<KnowledgeArticle> updateKnowledge(@PathVariable Long id,
                                                         @Valid @RequestBody AdminConfigDtos.KnowledgeUpsertRequest request) {
        return ApiResponse.success(adminOpsService.updateKnowledge(id, request));
    }
}
