package com.example.villagefirerisk.repository;

import com.example.villagefirerisk.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    java.util.List<NotificationLog> findByUserUsernameOrderByIdDesc(String username);
}

