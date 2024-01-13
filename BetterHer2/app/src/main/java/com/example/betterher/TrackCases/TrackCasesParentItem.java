package com.example.betterher.TrackCases;

import java.util.ArrayList;

public class TrackCasesParentItem {
    private String caseID;
    private String incidentType;
    private String incidentDate;
    private String incidentTime;
    private String incidentLocation;
    private ArrayList<TrackCasesChildItem> trackCasesChildItems;
    private String caseStatus;

    public TrackCasesParentItem(String caseID, String incidentType, String incidentDate, String incidentTime, String incidentLocation, String caseStatus) {
        this.caseID = caseID;
        this.incidentType = incidentType;
        this.incidentDate = incidentDate;
        this.incidentTime = incidentTime;
        this.incidentLocation = incidentLocation;
        this.caseStatus = caseStatus;
    }

    public String getCaseID() {
        return caseID;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public String getIncidentDate() {
        return incidentDate;
    }

    public String getIncidentTime() {
        return incidentTime;
    }

    public String getIncidentLocation() {
        return incidentLocation;
    }

    public ArrayList<TrackCasesChildItem> getTrackCasesChildItems() {
        return trackCasesChildItems;
    }

    public void setTrackCasesChildItems(ArrayList<TrackCasesChildItem> trackCasesChildItems) {
        this.trackCasesChildItems = trackCasesChildItems;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

}
