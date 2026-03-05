package com.example.villagefirerisk.service;

import com.example.villagefirerisk.entity.NotificationChannel;
import com.example.villagefirerisk.entity.NotificationLog;
import com.example.villagefirerisk.entity.NotificationStatus;
import com.example.villagefirerisk.entity.User;
import com.example.villagefirerisk.repository.NotificationLogRepository;
import com.example.villagefirerisk.repository.UserRepository;
import com.example.villagefirerisk.util.BusinessException;
import com.example.villagefirerisk.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationLogRepository notificationLogRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationLogRepository notificationLogRepository, UserRepository userRepository) {
        this.notificationLogRepository = notificationLogRepository;
        this.userRepository = userRepository;
    }

    public NotificationLog mockSend(Long userId, String content) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("用户不存在"));
        NotificationLog log = new NotificationLog();
        log.setUser(user);
        log.setChannel(NotificationChannel.SMS);
        log.setTemplateCode("CUSTOM");
        log.setReceiver(user.getPhone() == null ? user.getUsername() : user.getPhone());
        log.setContent(content);
        log.setStatus(NotificationStatus.SUCCESS);
        log.setSentAt(LocalDateTime.now());
        log.setBizType("MANUAL");
        log.setBizId(userId);
        return notificationLogRepository.save(log);
    }

    public List<NotificationLog> list() {
        return notificationLogRepository.findAll();
    }

    public List<NotificationLog> listMy() {
        String username = SecurityUtil.getCurrentUsername();
        if (username == null || username.isBlank()) {
            throw new BusinessException("未登录");
        }
        return notificationLogRepository.findByUserUsernameOrderByIdDesc(username);
    }
}
