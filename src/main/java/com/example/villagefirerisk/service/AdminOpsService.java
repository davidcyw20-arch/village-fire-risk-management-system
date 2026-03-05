package com.example.villagefirerisk.service;

import com.example.villagefirerisk.dto.AdminConfigDtos;
import com.example.villagefirerisk.entity.*;
import com.example.villagefirerisk.repository.*;
import com.example.villagefirerisk.util.BusinessException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminOpsService {

    private final SystemConfigItemRepository systemConfigItemRepository;
    private final DataDictionaryItemRepository dataDictionaryItemRepository;
    private final PushRuleRepository pushRuleRepository;
    private final OperationLogRepository operationLogRepository;
    private final KnowledgeArticleRepository knowledgeArticleRepository;
    private final HazardRepository hazardRepository;
    private final NotificationService notificationService;

    public AdminOpsService(SystemConfigItemRepository systemConfigItemRepository,
                           DataDictionaryItemRepository dataDictionaryItemRepository,
                           PushRuleRepository pushRuleRepository,
                           OperationLogRepository operationLogRepository,
                           KnowledgeArticleRepository knowledgeArticleRepository,
                           HazardRepository hazardRepository,
                           NotificationService notificationService) {
        this.systemConfigItemRepository = systemConfigItemRepository;
        this.dataDictionaryItemRepository = dataDictionaryItemRepository;
        this.pushRuleRepository = pushRuleRepository;
        this.operationLogRepository = operationLogRepository;
        this.knowledgeArticleRepository = knowledgeArticleRepository;
        this.hazardRepository = hazardRepository;
        this.notificationService = notificationService;
    }

    public List<Hazard> listHazardsForMonitor() {
        List<Hazard> hazards = hazardRepository.findAll();
        hazards.sort(Comparator.comparing(Hazard::getId).reversed());
        return hazards;
    }

    public NotificationLog urgeHazard(Long hazardId, String customContent) {
        Hazard hazard = hazardRepository.findById(hazardId).orElseThrow(() -> new BusinessException("隐患不存在"));
        if (hazard.getAssignedTo() == null) {
            throw new BusinessException("该隐患尚未分派网格员，无法督办");
        }
        String content = (customContent == null || customContent.isBlank())
                ? String.format("【隐患督办】请尽快处理隐患[%d-%s]，当前状态：%s，辖区：%s。", hazard.getId(), hazard.getTitle(), hazard.getStatus(), hazard.getAreaCode())
                : customContent;
        return notificationService.mockSend(hazard.getAssignedTo().getId(), content);
    }

    public Map<String, String> getSystemConfig() {
        return toMap(List.of("apiBase", "defaultArea", "rolePolicy"));
    }

    public Map<String, String> saveSystemConfig(AdminConfigDtos.SystemConfigRequest request) {
        upsert("apiBase", request.getApiBase(), "系统API地址");
        upsert("defaultArea", request.getDefaultArea().toUpperCase(), "默认辖区编码");
        upsert("rolePolicy", request.getRolePolicy(), "权限分配策略");
        return getSystemConfig();
    }

    public Map<String, String> getRiskModel() {
        return toMap(List.of("modelName", "wHazard", "wPending", "wCritical", "warningThreshold"));
    }

    public Map<String, String> saveRiskModel(AdminConfigDtos.RiskModelRequest request) {
        upsert("modelName", request.getModelName(), "风险模型名称");
        upsert("wHazard", String.valueOf(request.getWHazard()), "隐患数量权重");
        upsert("wPending", String.valueOf(request.getWPending()), "待处理权重");
        upsert("wCritical", String.valueOf(request.getWCritical()), "高风险权重");
        upsert("warningThreshold", String.valueOf(request.getWarningThreshold()), "预警阈值");
        return getRiskModel();
    }

    public List<DataDictionaryItem> listDict() {
        return dataDictionaryItemRepository.findByOrderByIdDesc();
    }

    public DataDictionaryItem addDict(AdminConfigDtos.DictItemRequest request) {
        DataDictionaryItem item = new DataDictionaryItem();
        item.setDictType(request.getDictType().trim());
        item.setDictValue(request.getDictValue().trim());
        item.setEnabled(true);
        return dataDictionaryItemRepository.save(item);
    }

    public void deleteDict(Long id) {
        dataDictionaryItemRepository.deleteById(id);
    }

    public List<PushRule> listPushRules() {
        return pushRuleRepository.findByOrderByIdDesc();
    }

    public PushRule addPushRule(AdminConfigDtos.PushRuleRequest request) {
        PushRule rule = new PushRule();
        rule.setName(request.getName().trim());
        rule.setRuleContent(request.getRuleContent());
        rule.setEnabled(request.getEnabled() == null || request.getEnabled());
        return pushRuleRepository.save(rule);
    }

    public PushRule togglePushRule(Long id, Boolean enabled) {
        PushRule rule = pushRuleRepository.findById(id).orElseThrow(() -> new BusinessException("推送规则不存在"));
        rule.setEnabled(Boolean.TRUE.equals(enabled));
        return pushRuleRepository.save(rule);
    }

    public List<OperationLog> listAuditLogs(String keyword) {
        List<OperationLog> all = operationLogRepository.findAll();
        all.sort(Comparator.comparing(OperationLog::getId).reversed());
        if (keyword == null || keyword.isBlank()) {
            return all;
        }
        String k = keyword.toLowerCase();
        return all.stream().filter(x -> String.join(" ",
                Objects.toString(x.getModule(), ""),
                Objects.toString(x.getOperation(), ""),
                Objects.toString(x.getRequestParams(), ""),
                Objects.toString(x.getRequestUri(), ""),
                Objects.toString(x.getUsername(), "")).toLowerCase().contains(k)
        ).toList();
    }

    public void clearAuditLogs() {
        operationLogRepository.deleteAllInBatch();
    }

    public List<KnowledgeArticle> listKnowledgeArticles() {
        return knowledgeArticleRepository.findAll();
    }

    public KnowledgeArticle createKnowledge(AdminConfigDtos.KnowledgeUpsertRequest request) {
        KnowledgeArticle article = new KnowledgeArticle();
        applyKnowledge(article, request);
        return knowledgeArticleRepository.save(article);
    }

    public KnowledgeArticle updateKnowledge(Long id, AdminConfigDtos.KnowledgeUpsertRequest request) {
        KnowledgeArticle article = knowledgeArticleRepository.findById(id).orElseThrow(() -> new BusinessException("知识文章不存在"));
        applyKnowledge(article, request);
        return knowledgeArticleRepository.save(article);
    }

    private void applyKnowledge(KnowledgeArticle article, AdminConfigDtos.KnowledgeUpsertRequest request) {
        article.setTitle(request.getTitle().trim());
        article.setCategory(request.getCategory().trim());
        article.setTargetRole(request.getTargetRole());
        article.setTargetRiskLevel(request.getTargetRiskLevel());
        article.setContent(request.getContent().trim());
        article.setSource(request.getSource());
        article.setPublished(request.getPublished() == null || request.getPublished());
    }

    private Map<String, String> toMap(List<String> keys) {
        Map<String, String> map = new LinkedHashMap<>();
        keys.forEach(k -> map.put(k, ""));
        systemConfigItemRepository.findAll().forEach(item -> {
            if (map.containsKey(item.getConfigKey())) {
                map.put(item.getConfigKey(), item.getConfigValue());
            }
        });
        return map;
    }

    private void upsert(String key, String value, String desc) {
        SystemConfigItem item = systemConfigItemRepository.findByConfigKey(key).orElseGet(SystemConfigItem::new);
        item.setConfigKey(key);
        item.setConfigValue(value);
        item.setDescription(desc);
        systemConfigItemRepository.save(item);
    }
}
