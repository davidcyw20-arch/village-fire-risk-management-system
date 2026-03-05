package com.example.villagefirerisk.repository;

import com.example.villagefirerisk.entity.PushRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PushRuleRepository extends JpaRepository<PushRule, Long> {
    List<PushRule> findByOrderByIdDesc();
}
