package com.example.villagefirerisk.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dangerous_goods")
public class DangerousGoods extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "goods_name", nullable = false)
    private String goodsName;
    @Column(name = "goods_type", nullable = false)
    private String goodsType;
    @Enumerated(EnumType.STRING)
    @Column(name = "hazard_level", nullable = false)
    private RiskLevel hazardLevel;
    @Column(length = 500)
    private String description;
    @Column(name = "storage_requirement", length = 500)
    private String storageRequirement;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getGoodsName() { return goodsName; }
    public void setGoodsName(String goodsName) { this.goodsName = goodsName; }
    public String getGoodsType() { return goodsType; }
    public void setGoodsType(String goodsType) { this.goodsType = goodsType; }
    public RiskLevel getHazardLevel() { return hazardLevel; }
    public void setHazardLevel(RiskLevel hazardLevel) { this.hazardLevel = hazardLevel; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStorageRequirement() { return storageRequirement; }
    public void setStorageRequirement(String storageRequirement) { this.storageRequirement = storageRequirement; }
}
