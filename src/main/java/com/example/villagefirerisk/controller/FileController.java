package com.example.villagefirerisk.controller;

import com.example.villagefirerisk.dto.ApiResponse;
import com.example.villagefirerisk.service.FileService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('RESIDENT','GRID','ADMIN')")
    public ApiResponse<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        return ApiResponse.success(Map.of("url", fileService.upload(file)));
    }
}
