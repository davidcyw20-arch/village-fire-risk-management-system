package com.example.villagefirerisk.controller;

import com.example.villagefirerisk.aop.OperationLoggable;
import com.example.villagefirerisk.dto.AdminUserDtos;
import com.example.villagefirerisk.dto.ApiResponse;
import com.example.villagefirerisk.service.AdminUserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public ApiResponse<List<AdminUserDtos.UserItem>> listUsers() {
        return ApiResponse.success(adminUserService.listUsers());
    }

    @PostMapping
    @OperationLoggable(module = "AdminUser", operation = "Create User")
    public ApiResponse<AdminUserDtos.UserItem> create(@Valid @RequestBody AdminUserDtos.CreateUserRequest request) {
        return ApiResponse.success(adminUserService.create(request));
    }

    @PatchMapping("/{id}/enabled")
    @OperationLoggable(module = "AdminUser", operation = "Update User Enabled")
    public ApiResponse<AdminUserDtos.UserItem> updateEnabled(@PathVariable Long id,
                                                             @Valid @RequestBody AdminUserDtos.UpdateEnabledRequest request) {
        return ApiResponse.success(adminUserService.updateEnabled(id, request.getEnabled()));
    }
}
