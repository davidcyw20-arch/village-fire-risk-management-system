package com.example.villagefirerisk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AdminConfigDtos {

    public static class SystemConfigRequest {
        @NotBlank
        private String apiBase;
        @NotBlank
        private String defaultArea;
        @NotBlank
        private String rolePolicy;

        public String getApiBase() { return apiBase; }
        public void setApiBase(String apiBase) { this.apiBase = apiBase; }
        public String getDefaultArea() { return defaultArea; }
        public void setDefaultArea(String defaultArea) { this.defaultArea = defaultArea; }
        public String getRolePolicy() { return rolePolicy; }
        public void setRolePolicy(String rolePolicy) { this.rolePolicy = rolePolicy; }
    }

    public static class DictItemRequest {
        @NotBlank
        private String dictType;
        @NotBlank
        private String dictValue;

        public String getDictType() { return dictType; }
        public void setDictType(String dictType) { this.dictType = dictType; }
        public String getDictValue() { return dictValue; }
        public void setDictValue(String dictValue) { this.dictValue = dictValue; }
    }

    public static class PushRuleRequest {
        @NotBlank
        private String name;
        private String ruleContent;
        private Boolean enabled;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getRuleContent() { return ruleContent; }
        public void setRuleContent(String ruleContent) { this.ruleContent = ruleContent; }
        public Boolean getEnabled() { return enabled; }
        public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    }

    public static class PushRuleToggleRequest {
        @NotNull
        private Boolean enabled;
        public Boolean getEnabled() { return enabled; }
        public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    }

    public static class RiskModelRequest {
        @NotBlank
        private String modelName;
        @NotNull
        private Double wHazard;
        @NotNull
        private Double wPending;
        @NotNull
        private Double wCritical;
        @NotNull
        private Integer warningThreshold;

        public String getModelName() { return modelName; }
        public void setModelName(String modelName) { this.modelName = modelName; }
        public Double getWHazard() { return wHazard; }
        public void setWHazard(Double wHazard) { this.wHazard = wHazard; }
        public Double getWPending() { return wPending; }
        public void setWPending(Double wPending) { this.wPending = wPending; }
        public Double getWCritical() { return wCritical; }
        public void setWCritical(Double wCritical) { this.wCritical = wCritical; }
        public Integer getWarningThreshold() { return warningThreshold; }
        public void setWarningThreshold(Integer warningThreshold) { this.warningThreshold = warningThreshold; }
    }

    public static class KnowledgeUpsertRequest {
        @NotBlank
        private String title;
        @NotBlank
        private String category;
        @NotNull
        private com.example.villagefirerisk.entity.Role targetRole;
        @NotNull
        private com.example.villagefirerisk.entity.RiskLevel targetRiskLevel;
        @NotBlank
        private String content;
        private String source;
        private Boolean published;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public com.example.villagefirerisk.entity.Role getTargetRole() { return targetRole; }
        public void setTargetRole(com.example.villagefirerisk.entity.Role targetRole) { this.targetRole = targetRole; }
        public com.example.villagefirerisk.entity.RiskLevel getTargetRiskLevel() { return targetRiskLevel; }
        public void setTargetRiskLevel(com.example.villagefirerisk.entity.RiskLevel targetRiskLevel) { this.targetRiskLevel = targetRiskLevel; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
        public Boolean getPublished() { return published; }
        public void setPublished(Boolean published) { this.published = published; }
    }

    public static class ValueResponse {
        private String key;
        private String value;
        public ValueResponse(String key, String value) { this.key = key; this.value = value; }
        public String getKey() { return key; }
        public String getValue() { return value; }
    }

    public static class BatchResponse {
        private List<ValueResponse> items;
        public BatchResponse(List<ValueResponse> items) { this.items = items; }
        public List<ValueResponse> getItems() { return items; }
    }
}
