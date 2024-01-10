package com.example.betterher.card;
public class Card {
    private int imageResourceId;
    private String title;
    private boolean isLastCard;

    Card() {}

    Card(int imageResourceId, String title) {
        this(imageResourceId, title, false);
    }

    Card(int imageResourceId, String title, boolean isLastCard) {
        this.imageResourceId = imageResourceId;
        this.title = title;
        this.isLastCard = isLastCard;
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

    public boolean isLastCard() {
        return isLastCard;
    }

    public void setLastCard(boolean lastCard) {
        isLastCard = lastCard;
    }
}
