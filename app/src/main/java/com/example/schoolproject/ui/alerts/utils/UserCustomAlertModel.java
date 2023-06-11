package com.example.schoolproject.ui.alerts.utils;

public class UserCustomAlertModel {

    private int id;
    private String alertName;
    private String userMessage;
    private String upperLimit;
    private String lowerLimit;

    public UserCustomAlertModel(int id, String alertName, String userMessage, String upperLimit, String lowerLimit) {
        this.id = id;
        this.alertName = alertName;
        this.userMessage = userMessage;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(String upperLimit) {
        this.upperLimit = upperLimit;
    }

    public String getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(String lowerLimit) {
        this.lowerLimit = lowerLimit;
    }
}
