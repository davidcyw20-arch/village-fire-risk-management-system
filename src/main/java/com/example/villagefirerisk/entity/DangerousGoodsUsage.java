package com.example.villagefirerisk.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "dangerous_goods_usage")
public class DangerousGoodsUsage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id", nullable = false)
    private DangerousGoods goods;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "housing_id")
    private HousingInfo housingInfo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private FacilityInfo facilityInfo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false)
    private BigDecimal quantity;
    @Column(length = 20)
    private String unit;
    @Column(name = "usage_purpose", length = 255)
    private String usagePurpose;
    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public DangerousGoods getGoods() { return goods; }
    public void setGoods(DangerousGoods goods) { this.goods = goods; }
    public HousingInfo getHousingInfo() { return housingInfo; }
    public void setHousingInfo(HousingInfo housingInfo) { this.housingInfo = housingInfo; }
    public FacilityInfo getFacilityInfo() { return facilityInfo; }
    public void setFacilityInfo(FacilityInfo facilityInfo) { this.facilityInfo = facilityInfo; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getUsagePurpose() { return usagePurpose; }
    public void setUsagePurpose(String usagePurpose) { this.usagePurpose = usagePurpose; }
    public LocalDateTime getLastUsedAt() { return lastUsedAt; }
    public void setLastUsedAt(LocalDateTime lastUsedAt) { this.lastUsedAt = lastUsedAt; }
}
