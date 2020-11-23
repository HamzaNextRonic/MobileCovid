package com.learntodroid.androidqrcodescanner.model;

public class Device {
    private String codeQR;
    private String idDevice;

    public Device() {
    }

    public Device(String codeQR, String idDevice) {
        this.codeQR = codeQR;
        this.idDevice = idDevice;
    }

    public String getCodeQR() {
        return codeQR;
    }

    public void setCodeQR(String codeQR) {
        this.codeQR = codeQR;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }
}
