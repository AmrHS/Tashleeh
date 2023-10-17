package com.autoparts.tashleeh.Model;

public class DataModel {
    private String title,image;

    public DataModel(String title,String image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
}