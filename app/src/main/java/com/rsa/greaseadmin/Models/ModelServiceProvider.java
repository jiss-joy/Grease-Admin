package com.rsa.greaseadmin.Models;

public class ModelServiceProvider {

    private String serviceBusinessName;
    private String serviceDistrict;
    private String serviceState;
    private String servicePinCode;

    public ModelServiceProvider() {
    }

    public ModelServiceProvider(String serviceBusinessName, String serviceDistrict, String serviceState, String servicePinCode) {
        this.serviceBusinessName = serviceBusinessName;
        this.serviceDistrict = serviceDistrict;
        this.serviceState = serviceState;
        this.servicePinCode = servicePinCode;
    }

    public String getServiceBusinessName() {
        return serviceBusinessName;
    }

    public String getServiceDistrict() {
        return serviceDistrict;
    }

    public String getServiceState() {
        return serviceState;
    }

    public String getServicePinCode() {
        return servicePinCode;
    }
}
