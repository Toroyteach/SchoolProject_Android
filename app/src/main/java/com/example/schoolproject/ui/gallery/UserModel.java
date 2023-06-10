package com.example.schoolproject.ui.gallery;

public class UserModel {

    int id;
    String username;
    String phone;
    String email;
    String deviceToken;

    public UserModel(int id, String username, String phone, String email, String deviceToken) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.deviceToken = deviceToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
