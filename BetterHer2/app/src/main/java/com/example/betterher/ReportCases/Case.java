package com.example.betterher.ReportCases;

import java.util.ArrayList;

public class Case {

    String role;
    String incidentType;
    String incidentDate;
    String incidentTime;
    String incidentLocation;
    String description;
    ArrayList<String> imageUrls;
    String caseId;
    String caseStatus;

    public Case() {}

    public Case(String role, String incidentType, String incidentDate, String incidentTime, String incidentLocation, String description, ArrayList<String> imageUrls, String caseId, String caseStatus) {
        this.role = role;
        this.incidentType = incidentType;
        this.incidentDate = incidentDate;
        this.incidentTime = incidentTime;
        this.incidentLocation = incidentLocation;
        this.description = description;
        this.imageUrls = imageUrls;
        this.caseId = caseId;
        this.caseStatus = caseStatus;
    }

    public String getRole() {
        return role;
    }

    // Note: Do not delete. All getters and setters are required to allow read from firestore
    public void setRole(String role) {
        this.role = role;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public String getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(String incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getIncidentTime() {
        return incidentTime;
    }

    public void setIncidentTime(String incidentTime) {
        this.incidentTime = incidentTime;
    }

    public String getIncidentLocation() {
        return incidentLocation;
    }

    public void setIncidentLocation(String incidentLocation) {
        this.incidentLocation = incidentLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }
}
