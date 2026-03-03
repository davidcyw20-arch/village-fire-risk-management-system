package com.example.villagefirerisk.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resident_info")
public class ResidentInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "housing_id")
    private HousingInfo housingInfo;
    @Column(name = "id_card_no")
    private String idCardNo;
    @Column(name = "emergency_contact")
    private String emergencyContact;
    @Column(name = "emergency_phone")
    private String emergencyPhone;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public HousingInfo getHousingInfo() { return housingInfo; }
    public void setHousingInfo(HousingInfo housingInfo) { this.housingInfo = housingInfo; }
    public String getIdCardNo() { return idCardNo; }
    public void setIdCardNo(String idCardNo) { this.idCardNo = idCardNo; }
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }
}
