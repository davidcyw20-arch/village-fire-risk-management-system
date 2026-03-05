package com.example.villagefirerisk.repository;

import com.example.villagefirerisk.entity.SystemConfigItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemConfigItemRepository extends JpaRepository<SystemConfigItem, Long> {
    Optional<SystemConfigItem> findByConfigKey(String configKey);
}
