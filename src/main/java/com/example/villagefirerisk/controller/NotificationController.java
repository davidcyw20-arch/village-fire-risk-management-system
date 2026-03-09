package com.example.villagefirerisk.controller;

import com.example.villagefirerisk.aop.OperationLoggable;
import com.example.villagefirerisk.dto.ApiResponse;
import com.example.villagefirerisk.entity.NotificationLog;
import com.example.villagefirerisk.service.NotificationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/mock-send")
    @PreAuthorize("hasAnyRole('GRID','ADMIN')")
    @OperationLoggable(module = "Notification", operation = "Mock Send SMS")
    public ApiResponse<NotificationLog> mockSend(@RequestBody Map<String, Object> req) {
        Long userId = Long.parseLong(String.valueOf(req.get("userId")));
        String content = String.valueOf(req.get("content"));
        return ApiResponse.success(notificationService.mockSend(userId, content));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('GRID','ADMIN')")
    public ApiResponse<List<NotificationLog>> list() {
        return ApiResponse.success(notificationService.list());
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('GRID','ADMIN')")
    public ApiResponse<List<NotificationLog>> listMy() {
        return ApiResponse.success(notificationService.listMy());
    }
}
