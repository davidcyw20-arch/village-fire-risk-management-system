package com.example.villagefirerisk.controller;

import com.example.villagefirerisk.dto.ApiResponse;
import com.example.villagefirerisk.entity.DataDictionaryItem;
import com.example.villagefirerisk.repository.DataDictionaryItemRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dictionaries")
public class DictionaryController {

    private final DataDictionaryItemRepository dataDictionaryItemRepository;

    public DictionaryController(DataDictionaryItemRepository dataDictionaryItemRepository) {
        this.dataDictionaryItemRepository = dataDictionaryItemRepository;
    }

    @GetMapping("/items")
    @PreAuthorize("hasAnyRole('RESIDENT','GRID','ADMIN')")
    public ApiResponse<List<DataDictionaryItem>> listByType(@RequestParam String dictType) {
        return ApiResponse.success(dataDictionaryItemRepository
                .findByEnabledTrueAndDictTypeIgnoreCaseOrderByIdDesc(dictType));
    }
}
