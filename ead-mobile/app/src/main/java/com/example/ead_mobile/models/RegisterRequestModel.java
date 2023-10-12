package com.example.ead_mobile.models;

public class RegisterRequestModel {
    public String nic;
    public String password;
    public String name;
    public String mobile;
    public String email;

    public RegisterRequestModel (String nic, String password,String mobile, String email, String name) {
        this.nic = nic;
        this.password = password;
        this.mobile = mobile;
        this.name = name;
        this.email = email;
    }
}
