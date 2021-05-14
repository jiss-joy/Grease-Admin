package com.rsa.greaseadmin.Models;

public class ModelRegistration {

    private String userGSTIN;

    public ModelRegistration() {
        //Empty Constructor Required.
    }

    public ModelRegistration(String userGSTIN) {
        this.userGSTIN = userGSTIN;
    }

    public String getUserGSTIN() {
        return userGSTIN;
    }
}
