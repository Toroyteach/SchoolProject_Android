package com.example.schoolproject.utils;

public class WeatherAlertModal {

    private int id;
    private String alertName;
    private String startTime;
    private String endTime;
    private String description;
    private String userMessage;
    private String upperLimit;
    private String lowerLimit;
    private int thumbnail;


    public WeatherAlertModal(int id, String alertName, String startTime, String endTime, String description, String userMessage, String upperLimit, String lowerLimit, int thumbnail) {
        this.id = id;
        this.alertName = alertName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.upperLimit = upperLimit;
        this.userMessage = userMessage;
        this.lowerLimit = lowerLimit;
        this.thumbnail = thumbnail;
    }

    public String getAlertName() {
        return alertName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
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

    public String getStartTime() {
        return startTime;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
