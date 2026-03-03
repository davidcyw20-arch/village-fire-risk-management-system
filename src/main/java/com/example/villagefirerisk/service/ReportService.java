package com.example.villagefirerisk.service;

import com.example.villagefirerisk.entity.Hazard;
import com.example.villagefirerisk.repository.HazardRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportService {

    private final HazardRepository hazardRepository;

    public ReportService(HazardRepository hazardRepository) {
        this.hazardRepository = hazardRepository;
    }

    public byte[] exportHazards() throws IOException {
        List<Hazard> hazards = hazardRepository.findAll();
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("hazards");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Title");
        header.createCell(2).setCellValue("Type");
        header.createCell(3).setCellValue("Area");
        header.createCell(4).setCellValue("Status");
        header.createCell(5).setCellValue("CreatedAt");

        int i = 1;
        for (Hazard hazard : hazards) {
            Row row = sheet.createRow(i++);
            row.createCell(0).setCellValue(hazard.getId());
            row.createCell(1).setCellValue(hazard.getTitle());
            row.createCell(2).setCellValue(hazard.getHazardType());
            row.createCell(3).setCellValue(hazard.getAreaCode());
            row.createCell(4).setCellValue(hazard.getStatus().name());
            row.createCell(5).setCellValue(hazard.getCreatedAt().toString());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }
}
