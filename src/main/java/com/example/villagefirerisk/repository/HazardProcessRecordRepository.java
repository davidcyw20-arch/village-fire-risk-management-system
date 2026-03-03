package com.example.villagefirerisk.repository;

import com.example.villagefirerisk.entity.HazardProcessRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HazardProcessRecordRepository extends JpaRepository<HazardProcessRecord, Long> {
}
