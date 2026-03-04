package com.example.villagefirerisk.service;

import com.example.villagefirerisk.dto.HazardDtos;
import com.example.villagefirerisk.entity.*;
import com.example.villagefirerisk.repository.HazardProcessRecordRepository;
import com.example.villagefirerisk.repository.HazardRepository;
import com.example.villagefirerisk.repository.NotificationLogRepository;
import com.example.villagefirerisk.repository.UserRepository;
import com.example.villagefirerisk.util.BusinessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HazardService {

    private final HazardRepository hazardRepository;
    private final UserRepository userRepository;
    private final HazardProcessRecordRepository processRecordRepository;
    private final NotificationLogRepository notificationLogRepository;

    public HazardService(HazardRepository hazardRepository, UserRepository userRepository,
                         HazardProcessRecordRepository processRecordRepository,
                         NotificationLogRepository notificationLogRepository) {
        this.hazardRepository = hazardRepository;
        this.userRepository = userRepository;
        this.processRecordRepository = processRecordRepository;
        this.notificationLogRepository = notificationLogRepository;
    }

    public Hazard report(HazardDtos.ReportRequest request, String username) {
        User reporter = userRepository.findByUsername(username).orElseThrow(() -> new BusinessException("用户不存在"));
        Hazard hazard = new Hazard();
        hazard.setTitle(request.getTitle());
        hazard.setDescription(request.getDescription());
        hazard.setHazardType(request.getHazardType());
        hazard.setAreaCode(request.getAreaCode());
        hazard.setAddress(request.getAddress());
        hazard.setLatitude(BigDecimal.valueOf(request.getLatitude()));
        hazard.setLongitude(BigDecimal.valueOf(request.getLongitude()));
        hazard.setImageUrl(request.getImageUrl());
        hazard.setSeverity(request.getSeverity() == null ? RiskLevel.MID : request.getSeverity());
        hazard.setStatus(HazardStatus.REPORTED);
        hazard.setReportedBy(reporter);
        hazard.setDueAt(LocalDateTime.now().plusDays(7));
        Hazard saved = hazardRepository.save(hazard);
        addRecord(saved, reporter, "REPORT", null, HazardStatus.REPORTED.name(), request.getDescription(), request.getImageUrl());
        return saved;
    }

    public Hazard assign(Long hazardId, Long gridUserId, String adminUsername) {
        Hazard hazard = hazardRepository.findById(hazardId).orElseThrow(() -> new BusinessException("隐患不存在"));
        User admin = userRepository.findByUsername(adminUsername).orElseThrow(() -> new BusinessException("用户不存在"));
        User grid = userRepository.findById(gridUserId).orElseThrow(() -> new BusinessException("网格员不存在"));
        if (grid.getRole() != Role.GRID) {
            throw new BusinessException("指派对象不是网格员");
        }
        String before = hazard.getStatus().name();
        hazard.setAssignedTo(grid);
        hazard.setAssignedBy(admin);
        hazard.setAssignedAt(LocalDateTime.now());
        hazard.setStatus(HazardStatus.ASSIGNED);
        Hazard saved = hazardRepository.save(hazard);
        addRecord(saved, admin, "ASSIGN", before, HazardStatus.ASSIGNED.name(), "管理员分派", null);
        sendAssignNotification(grid, saved);
        return saved;
    }

    public Hazard process(Long hazardId, HazardDtos.ProcessRequest request, String gridUsername) {
        Hazard hazard = hazardRepository.findById(hazardId).orElseThrow(() -> new BusinessException("隐患不存在"));
        User processor = userRepository.findByUsername(gridUsername).orElseThrow(() -> new BusinessException("用户不存在"));
        String before = hazard.getStatus().name();
        HazardStatus target = request.getTargetStatus() == null ? HazardStatus.IN_PROGRESS : request.getTargetStatus();
        hazard.setStatus(target);
        if (target == HazardStatus.RESOLVED) {
            hazard.setResolvedAt(LocalDateTime.now());
        }
        Hazard saved = hazardRepository.save(hazard);
        addRecord(saved, processor, "PROCESS", before, target.name(), request.getNote(), null);
        return saved;
    }

    public List<Hazard> list() {
        return hazardRepository.findAll();
    }

    private void addRecord(Hazard hazard, User user, String action, String before, String after, String note, String attachmentUrl) {
        HazardProcessRecord record = new HazardProcessRecord();
        record.setHazard(hazard);
        record.setProcessor(user);
        record.setActionType(action);
        record.setBeforeStatus(before);
        record.setAfterStatus(after);
        record.setProcessNote(note);
        record.setAttachmentUrl(attachmentUrl);
        processRecordRepository.save(record);
    }

    private void sendAssignNotification(User grid, Hazard hazard) {
        NotificationLog log = new NotificationLog();
        log.setUser(grid);
        log.setChannel("SMS");
        log.setTemplateCode("HAZARD_ASSIGN");
        log.setReceiver(grid.getPhone() == null ? grid.getUsername() : grid.getPhone());
        log.setContent("您有新的隐患待处理：#" + hazard.getId());
        log.setStatus("SUCCESS");
        log.setSentAt(LocalDateTime.now());
        log.setBizType("HAZARD_ASSIGN");
        log.setBizId(hazard.getId());
        notificationLogRepository.save(log);
    }
}
