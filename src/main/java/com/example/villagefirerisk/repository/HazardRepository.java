package com.example.villagefirerisk.repository;

import com.example.villagefirerisk.entity.Hazard;
import com.example.villagefirerisk.entity.HazardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HazardRepository extends JpaRepository<Hazard, Long> {
    List<Hazard> findByAreaCode(String areaCode);
    List<Hazard> findByAssignedToId(Long assignedTo);
    List<Hazard> findByStatusIn(List<HazardStatus> statuses);
}
