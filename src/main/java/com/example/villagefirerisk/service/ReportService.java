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
        Sheet sheet = workbook.createSheet("隐患报表");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("编号");
        header.createCell(1).setCellValue("标题");
        header.createCell(2).setCellValue("隐患类型");
        header.createCell(3).setCellValue("区域编码");
        header.createCell(4).setCellValue("状态");
        header.createCell(5).setCellValue("风险等级");
        header.createCell(6).setCellValue("上报时间");

        int i = 1;
        for (Hazard hazard : hazards) {
            Row row = sheet.createRow(i++);
            row.createCell(0).setCellValue(hazard.getId());
            row.createCell(1).setCellValue(hazard.getTitle());
            row.createCell(2).setCellValue(toHazardTypeLabel(hazard.getHazardType()));
            row.createCell(3).setCellValue(hazard.getAreaCode());
            row.createCell(4).setCellValue(toHazardStatusLabel(hazard.getStatus().name()));
            row.createCell(5).setCellValue(toRiskLevelLabel(hazard.getSeverity().name()));
            row.createCell(6).setCellValue(hazard.getCreatedAt().toString().replace("T", " "));
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }

    private String toHazardTypeLabel(String type) {
        if (type == null) return "-";
        return switch (type) {
            case "BLOCKED_EXIT" -> "消防通道堵塞";
            case "GAS_LEAK" -> "燃气泄漏风险";
            case "ELECTRICAL" -> "电气隐患";
            case "FIRE_FACILITY_DAMAGE" -> "消防设施损坏";
            case "ILLEGAL_STORAGE" -> "违规存放易燃物";
            default -> type;
        };
    }

    private String toHazardStatusLabel(String status) {
        if (status == null) return "-";
        return switch (status) {
            case "REPORTED" -> "待受理";
            case "ASSIGNED" -> "已分派";
            case "IN_PROGRESS" -> "处理中";
            case "RESOLVED" -> "已解决";
            case "REJECTED" -> "已驳回";
            default -> status;
        };
    }

    private String toRiskLevelLabel(String level) {
        if (level == null) return "-";
        return switch (level) {
            case "LOW" -> "低风险";
            case "MID" -> "中风险";
            case "HIGH" -> "高风险";
            case "CRITICAL" -> "极高风险";
            case "ALL" -> "全部风险";
            default -> level;
        };
    }
}
