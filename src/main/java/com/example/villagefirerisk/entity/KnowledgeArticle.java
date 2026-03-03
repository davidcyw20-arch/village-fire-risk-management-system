package com.example.villagefirerisk.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "knowledge_articles")
public class KnowledgeArticle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 200)
    private String title;
    @Column(nullable = false, length = 50)
    private String category;
    @Enumerated(EnumType.STRING)
    @Column(name = "target_role", nullable = false)
    private Role targetRole;
    @Enumerated(EnumType.STRING)
    @Column(name = "target_risk_level", nullable = false)
    private RiskLevel targetRiskLevel;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    @Column(length = 255)
    private String source;
    @Column(nullable = false)
    private Boolean published = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Role getTargetRole() { return targetRole; }
    public void setTargetRole(Role targetRole) { this.targetRole = targetRole; }
    public RiskLevel getTargetRiskLevel() { return targetRiskLevel; }
    public void setTargetRiskLevel(RiskLevel targetRiskLevel) { this.targetRiskLevel = targetRiskLevel; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public Boolean getPublished() { return published; }
    public void setPublished(Boolean published) { this.published = published; }
}
