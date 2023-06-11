package com.example.schoolproject.ui.notification.utils;

public class UserNotificationModel {

    private String alertName;
    private String userMessage;
    private String upperLimit;
    private String lowerLimit;
    private String notification_timestamp;
    private String notificationId;
    private String readAt;

    public UserNotificationModel(String alertName, String userMessage, String upperLimit, String lowerLimit, String notification_timestamp, String notificationId, String readAt) {
        this.alertName = alertName;
        this.userMessage = userMessage;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.notification_timestamp = notification_timestamp;
        this.notificationId = notificationId;
        this.readAt = readAt;
    }

    public String getReadAt() {
        return readAt;
    }

    public void setReadAt(String readAt) {
        this.readAt = readAt;
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

    public String getNotification_timestamp() {
        return notification_timestamp;
    }

    public void setNotification_timestamp(String notification_timestamp) {
        this.notification_timestamp = notification_timestamp;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }
}
