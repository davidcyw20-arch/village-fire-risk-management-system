package com.example.villagefirerisk.service;

import com.example.villagefirerisk.dto.AdminUserDtos;
import com.example.villagefirerisk.entity.Role;
import com.example.villagefirerisk.entity.User;
import com.example.villagefirerisk.repository.UserRepository;
import com.example.villagefirerisk.util.BusinessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AdminUserDtos.UserItem> listUsers() {
        return userRepository.findAllByOrderByIdDesc().stream().map(AdminUserDtos.UserItem::from).toList();
    }

    public AdminUserDtos.UserItem create(AdminUserDtos.CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        if (request.getRole() == Role.ADMIN) {
            throw new BusinessException("系统仅允许保留一个管理员账号，不能新增管理员");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setAreaCode(request.getAreaCode());
        user.setRole(request.getRole());
        user.setEnabled(true);
        return AdminUserDtos.UserItem.from(userRepository.save(user));
    }

    public AdminUserDtos.UserItem updateEnabled(Long userId, Boolean enabled) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("用户不存在"));
        if (user.getRole() == Role.ADMIN && !Boolean.TRUE.equals(enabled)) {
            throw new BusinessException("唯一管理员账号不能被停用");
        }
        user.setEnabled(Boolean.TRUE.equals(enabled));
        return AdminUserDtos.UserItem.from(userRepository.save(user));
    }
}
