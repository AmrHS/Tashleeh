package com.autoparts.tashleeh.Model;

public class SearchDataModel {
    private String title,image,brand;

    public SearchDataModel(String title, String image, String brand) {
        this.title = title;
        this.image = image;
        this.brand = brand;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
    public String getBrand() {
        return brand;
    }
}