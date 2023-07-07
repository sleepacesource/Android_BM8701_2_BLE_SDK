package com.sleepace.sdkdemo.bm8701_2_ble.bean;

public class DeviceInfo {
    private String deviceId;
    private String deviceName;
    private String version;
    private int communityMode = -1;
    private int realtimeInterval;
    private int  reportTime = -1;
    private int bedThickness = -1;
    private int material = -1;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;



    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getCommunityMode() {
        return communityMode;
    }

    public void setCommunityMode(int communityMode) {
        this.communityMode = communityMode;
    }

    public int getRealtimeInterval() {
        return realtimeInterval;
    }

    public void setRealtimeInterval(int realtimeInterval) {
        this.realtimeInterval = realtimeInterval;
    }

    public int getReportTime() {
        return reportTime;
    }

    public void setReportTime(int reportTime) {
        this.reportTime = reportTime;
    }


    public int getBedThickness() {
        return bedThickness;
    }

    public void setBedThickness(int bedThickness) {
        this.bedThickness = bedThickness;
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }
}
