package com.example.villagefirerisk.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "housing_info")
public class HousingInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "building_no", nullable = false)
    private String buildingNo;
    @Column(name = "unit_no")
    private String unitNo;
    @Column(name = "room_no")
    private String roomNo;
    @Column(name = "area_code", nullable = false)
    private String areaCode;
    @Column(nullable = false)
    private String address;
    @Column(name = "owner_name")
    private String ownerName;
    @Column(name = "owner_phone")
    private String ownerPhone;
    @Column(name = "occupancy_count")
    private Integer occupancyCount;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBuildingNo() { return buildingNo; }
    public void setBuildingNo(String buildingNo) { this.buildingNo = buildingNo; }
    public String getUnitNo() { return unitNo; }
    public void setUnitNo(String unitNo) { this.unitNo = unitNo; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public String getAreaCode() { return areaCode; }
    public void setAreaCode(String areaCode) { this.areaCode = areaCode; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getOwnerPhone() { return ownerPhone; }
    public void setOwnerPhone(String ownerPhone) { this.ownerPhone = ownerPhone; }
    public Integer getOccupancyCount() { return occupancyCount; }
    public void setOccupancyCount(Integer occupancyCount) { this.occupancyCount = occupancyCount; }
}
