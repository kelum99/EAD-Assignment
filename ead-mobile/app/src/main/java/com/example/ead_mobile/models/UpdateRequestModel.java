package com.example.ead_mobile.models;

public class UpdateRequestModel {
    public String nic;
    public String name;
    public String mobile;
    public String email;

    public UpdateRequestModel (String nic,String mobile, String email, String name) {
        this.nic = nic;
        this.mobile = mobile;
        this.name = name;
        this.email = email;
    }
}
