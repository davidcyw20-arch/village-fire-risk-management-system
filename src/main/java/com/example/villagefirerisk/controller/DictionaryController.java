package com.example.villagefirerisk.controller;

import com.example.villagefirerisk.dto.ApiResponse;
import com.example.villagefirerisk.entity.DataDictionaryItem;
import com.example.villagefirerisk.repository.DataDictionaryItemRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        String normalizedType = dictType == null ? "" : dictType.trim();
        List<DataDictionaryItem> exact = dataDictionaryItemRepository
                .findByEnabledTrueAndDictTypeIgnoreCaseOrderByIdDesc(normalizedType);

        if (!exact.isEmpty()) {
            return ApiResponse.success(exact);
        }

        List<DataDictionaryItem> fuzzy = dataDictionaryItemRepository
                .findByEnabledTrueAndDictTypeContainingIgnoreCaseOrderByIdDesc(normalizedType);

        if (!fuzzy.isEmpty()) {
            return ApiResponse.success(distinctByTypeValue(fuzzy));
        }

        if ("隐患类型".equals(normalizedType)) {
            return ApiResponse.success(listHazardTypeItems());
        }

        return ApiResponse.success(List.of());
    }

    @GetMapping("/hazard-types")
    @PreAuthorize("hasAnyRole('RESIDENT','GRID','ADMIN')")
    public ApiResponse<List<DataDictionaryItem>> listHazardTypes() {
        return ApiResponse.success(listHazardTypeItems());
    }

    private List<DataDictionaryItem> listHazardTypeItems() {
        List<DataDictionaryItem> enabled = dataDictionaryItemRepository.findByEnabledTrueOrderByIdDesc();
        List<DataDictionaryItem> matched = enabled.stream()
                .filter(x -> matchesHazardTypeKey(x.getDictType()))
                .toList();

        if (!matched.isEmpty()) {
            return distinctByTypeValue(matched);
        }

        return distinctByTypeValue(enabled);
    }

    private boolean matchesHazardTypeKey(String dictType) {
        if (dictType == null) {
            return false;
        }
        String t = dictType.trim().toLowerCase(Locale.ROOT);
        return t.contains("隐患") || t.contains("类型") || t.contains("hazard");
    }

    private List<DataDictionaryItem> distinctByTypeValue(List<DataDictionaryItem> items) {
        Map<String, DataDictionaryItem> dedup = new LinkedHashMap<>();
        for (DataDictionaryItem item : items) {
            String key = (item.getDictType() == null ? "" : item.getDictType().trim().toLowerCase()) + "::"
                    + (item.getDictValue() == null ? "" : item.getDictValue().trim().toLowerCase());
            dedup.putIfAbsent(key, item);
        }
        return new ArrayList<>(dedup.values());
    }
}
