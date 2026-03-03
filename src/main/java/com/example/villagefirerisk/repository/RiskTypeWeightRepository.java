package com.example.villagefirerisk.repository;

import com.example.villagefirerisk.entity.RiskTypeWeight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskTypeWeightRepository extends JpaRepository<RiskTypeWeight, Long> {
    List<RiskTypeWeight> findByEnabledTrue();
}
