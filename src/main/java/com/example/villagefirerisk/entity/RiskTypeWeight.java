package com.example.villagefirerisk.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "risk_type_weights")
public class RiskTypeWeight extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "hazard_type", nullable = false, unique = true, length = 50)
    private String hazardType;
    @Column(name = "type_weight", nullable = false)
    private Integer typeWeight;
    @Column(name = "is_high_risk", nullable = false)
    private Boolean highRisk = false;
    @Column(nullable = false)
    private Boolean enabled = true;
    @Column(length = 255)
    private String remark;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getHazardType() { return hazardType; }
    public void setHazardType(String hazardType) { this.hazardType = hazardType; }
    public Integer getTypeWeight() { return typeWeight; }
    public void setTypeWeight(Integer typeWeight) { this.typeWeight = typeWeight; }
    public Boolean getHighRisk() { return highRisk; }
    public void setHighRisk(Boolean highRisk) { this.highRisk = highRisk; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
