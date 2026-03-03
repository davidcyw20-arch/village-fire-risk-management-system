package com.example.villagefirerisk.service;

import com.example.villagefirerisk.dto.RiskAreaResponse;
import com.example.villagefirerisk.entity.Hazard;
import com.example.villagefirerisk.entity.HazardStatus;
import com.example.villagefirerisk.entity.RiskTypeWeight;
import com.example.villagefirerisk.repository.HazardRepository;
import com.example.villagefirerisk.repository.RiskTypeWeightRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RiskService {

    private final HazardRepository hazardRepository;
    private final RiskTypeWeightRepository riskTypeWeightRepository;

    public RiskService(HazardRepository hazardRepository, RiskTypeWeightRepository riskTypeWeightRepository) {
        this.hazardRepository = hazardRepository;
        this.riskTypeWeightRepository = riskTypeWeightRepository;
    }

    public List<RiskAreaResponse> calculateAreaRisk() {
        List<Hazard> hazards = hazardRepository.findAll();
        List<RiskTypeWeight> weights = riskTypeWeightRepository.findByEnabledTrue();
        Map<String, RiskTypeWeight> weightMap = weights.stream().collect(Collectors.toMap(RiskTypeWeight::getHazardType, w -> w));

        Map<String, List<Hazard>> areaHazardMap = hazards.stream().collect(Collectors.groupingBy(Hazard::getAreaCode));
        List<RiskAreaResponse> result = new ArrayList<>();

        for (Map.Entry<String, List<Hazard>> entry : areaHazardMap.entrySet()) {
            String areaCode = entry.getKey();
            List<Hazard> areaHazards = entry.getValue();
            int score = 0;

            Map<String, Long> countByType = areaHazards.stream()
                    .collect(Collectors.groupingBy(Hazard::getHazardType, Collectors.counting()));
            for (Map.Entry<String, Long> c : countByType.entrySet()) {
                RiskTypeWeight weight = weightMap.get(c.getKey());
                if (weight != null) {
                    score += weight.getTypeWeight() * c.getValue().intValue();
                }
            }

            for (Hazard hazard : areaHazards) {
                if (hazard.getStatus() != HazardStatus.RESOLVED && hazard.getStatus() != HazardStatus.REJECTED) {
                    long days = Duration.between(hazard.getCreatedAt(), LocalDateTime.now()).toDays();
                    if (days > 15) {
                        score += 20;
                    } else if (days > 7) {
                        score += 10;
                    }

                    RiskTypeWeight weight = weightMap.get(hazard.getHazardType());
                    if (weight != null && Boolean.TRUE.equals(weight.getHighRisk())) {
                        score += 5;
                    }
                }
            }

            result.add(new RiskAreaResponse(areaCode, score, resolveRiskLevel(score)));
        }

        result.sort(Comparator.comparing(RiskAreaResponse::getAreaCode));
        return result;
    }

    private String resolveRiskLevel(int score) {
        if (score <= 20) {
            return "LOW";
        }
        if (score <= 50) {
            return "MID";
        }
        if (score <= 80) {
            return "HIGH";
        }
        return "CRITICAL";
    }
}
