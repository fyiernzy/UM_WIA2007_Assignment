package com.example.swipablecardtest;
public class Card {
    private int imageResourceId;
    private String title;

    Card() {}

    Card(int imageResourceId, String title) {
        this.imageResourceId = imageResourceId;
        this.title = title;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
