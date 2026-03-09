package com.example.villagefirerisk.repository;

import com.example.villagefirerisk.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {
}
