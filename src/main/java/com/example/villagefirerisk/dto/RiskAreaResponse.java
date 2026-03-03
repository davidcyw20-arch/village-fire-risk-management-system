package com.example.villagefirerisk.dto;

public class RiskAreaResponse {
    private String areaCode;
    private int riskScore;
    private String riskLevel;

    public RiskAreaResponse(String areaCode, int riskScore, String riskLevel) {
        this.areaCode = areaCode;
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
    }

    public String getAreaCode() { return areaCode; }
    public void setAreaCode(String areaCode) { this.areaCode = areaCode; }
    public int getRiskScore() { return riskScore; }
    public void setRiskScore(int riskScore) { this.riskScore = riskScore; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
}
