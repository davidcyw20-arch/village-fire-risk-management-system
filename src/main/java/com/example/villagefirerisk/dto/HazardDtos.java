package com.example.villagefirerisk.dto;

import com.example.villagefirerisk.entity.HazardStatus;
import com.example.villagefirerisk.entity.RiskLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HazardDtos {
    public static class ReportRequest {
        @NotBlank
        private String title;
        @NotBlank
        private String description;
        @NotBlank
        private String hazardType;
        @NotBlank
        private String areaCode;
        @NotBlank
        private String address;
        @NotNull
        private Double latitude;
        @NotNull
        private Double longitude;
        private String imageUrl;
        private RiskLevel severity;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getHazardType() { return hazardType; }
        public void setHazardType(String hazardType) { this.hazardType = hazardType; }
        public String getAreaCode() { return areaCode; }
        public void setAreaCode(String areaCode) { this.areaCode = areaCode; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public RiskLevel getSeverity() { return severity; }
        public void setSeverity(RiskLevel severity) { this.severity = severity; }
    }

    public static class AssignRequest {
        @NotNull
        private Long gridUserId;
        public Long getGridUserId() { return gridUserId; }
        public void setGridUserId(Long gridUserId) { this.gridUserId = gridUserId; }
    }

    public static class ProcessRequest {
        @NotBlank
        private String note;
        private HazardStatus targetStatus;
        public String getNote() { return note; }
        public void setNote(String note) { this.note = note; }
        public HazardStatus getTargetStatus() { return targetStatus; }
        public void setTargetStatus(HazardStatus targetStatus) { this.targetStatus = targetStatus; }
    }
}
