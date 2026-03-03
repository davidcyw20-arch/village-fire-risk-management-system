package com.example.villagefirerisk.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "facility_info")
public class FacilityInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "facility_name", nullable = false)
    private String facilityName;
    @Column(name = "facility_type", nullable = false)
    private String facilityType;
    @Column(name = "area_code", nullable = false)
    private String areaCode;
    @Column
    private String address;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityStatus status;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFacilityName() { return facilityName; }
    public void setFacilityName(String facilityName) { this.facilityName = facilityName; }
    public String getFacilityType() { return facilityType; }
    public void setFacilityType(String facilityType) { this.facilityType = facilityType; }
    public String getAreaCode() { return areaCode; }
    public void setAreaCode(String areaCode) { this.areaCode = areaCode; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public FacilityStatus getStatus() { return status; }
    public void setStatus(FacilityStatus status) { this.status = status; }
}
