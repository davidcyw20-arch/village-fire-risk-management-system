package com.example.villagefirerisk.repository;

import com.example.villagefirerisk.entity.DataDictionaryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataDictionaryItemRepository extends JpaRepository<DataDictionaryItem, Long> {
    List<DataDictionaryItem> findByOrderByIdDesc();

    List<DataDictionaryItem> findByEnabledTrueOrderByIdDesc();

    List<DataDictionaryItem> findByEnabledTrueAndDictTypeIgnoreCaseOrderByIdDesc(String dictType);

    List<DataDictionaryItem> findByEnabledTrueAndDictTypeContainingIgnoreCaseOrderByIdDesc(String dictType);
}
