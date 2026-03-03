package com.example.villagefirerisk.service;

import com.example.villagefirerisk.entity.KnowledgeArticle;
import com.example.villagefirerisk.entity.RiskLevel;
import com.example.villagefirerisk.entity.Role;
import com.example.villagefirerisk.repository.KnowledgeArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeService {

    private final KnowledgeArticleRepository knowledgeArticleRepository;

    public KnowledgeService(KnowledgeArticleRepository knowledgeArticleRepository) {
        this.knowledgeArticleRepository = knowledgeArticleRepository;
    }

    public List<KnowledgeArticle> recommend(Role role, RiskLevel riskLevel) {
        return knowledgeArticleRepository.findByPublishedTrueAndTargetRoleInAndTargetRiskLevelIn(
                List.of(role, Role.valueOf("ALL")),
                List.of(riskLevel, RiskLevel.ALL)
        );
    }
}
