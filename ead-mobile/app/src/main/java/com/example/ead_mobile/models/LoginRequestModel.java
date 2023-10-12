package com.example.ead_mobile.models;

public class LoginRequestModel {
    public String nic;
    public String password;

    public LoginRequestModel(String nic, String password) {
        this.nic = nic;
        this.password = password;
    }
}
