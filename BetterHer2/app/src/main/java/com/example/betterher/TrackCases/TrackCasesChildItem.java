package com.example.betterher.TrackCases;

public class TrackCasesChildItem {
    private String progressDate;
    private String progressTime;
    private String progressTitle;
    private String progressDesc;
    private String caseStatus;

    public TrackCasesChildItem() {}

    public TrackCasesChildItem(String progressDate, String progressTime, String progressTitle, String progressDesc, String caseStatus) {
        this.progressDate = progressDate;
        this.progressTime = progressTime;
        this.progressTitle = progressTitle;
        this.progressDesc = progressDesc;
        this.caseStatus = caseStatus;
    }

    public String getProgressDate() {
        return progressDate;
    }

    public String getProgressTime() {
        return progressTime;
    }

    public String getProgressTitle() {
        return progressTitle;
    }

    public String getProgressDesc() {
        return progressDesc;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setProgressDate(String progressDate) {
        this.progressDate = progressDate;
    }

    public void setProgressTime(String progressTime) {
        this.progressTime = progressTime;
    }

    public void setProgressTitle(String progressTitle) {
        this.progressTitle = progressTitle;
    }

    public void setProgressDesc(String progressDesc) {
        this.progressDesc = progressDesc;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }
}

