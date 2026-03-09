package com.example.villagefirerisk.repository;

import com.example.villagefirerisk.entity.KnowledgeArticle;
import com.example.villagefirerisk.entity.RiskLevel;
import com.example.villagefirerisk.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KnowledgeArticleRepository extends JpaRepository<KnowledgeArticle, Long> {
    List<KnowledgeArticle> findByPublishedTrueAndTargetRoleInAndTargetRiskLevelIn(List<Role> roles, List<RiskLevel> levels);
}
