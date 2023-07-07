package com.example.schoolproject.ui.news.models;

public class NewsModel {
    private int id;
    private String title;
    private String body;
    private String image;
    private String createdAt;

    public NewsModel(String title, String body, String image, String createdAt) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.image = image;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getImage() {
        return image;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}

