package com.example.betterher.GetSupport;

public class GetSupportCenterModel {

    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_CARD_VIEW = 1;
    //int viewType;

    String centerAddress;
    String centerName;
    String centerPhoneNumber;

    public GetSupportCenterModel() {
        // Default constructor required for calls to DataSnapshot.getValue(GetSupportCenterModel.class)
    }

    public GetSupportCenterModel(String centerAddress, String centerName, String centerPhoneNumber) {
        this.centerAddress = centerAddress;
        this.centerName = centerName;
        this.centerPhoneNumber = centerPhoneNumber;
    }

    public String getCenterAddress() {
        return centerAddress;
    }

    public String getCenterName() {
        return centerName;
    }

    public String getCenterPhoneNumber() {
        return centerPhoneNumber;
    }
}
