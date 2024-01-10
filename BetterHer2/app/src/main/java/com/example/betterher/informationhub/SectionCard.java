package com.example.betterher.informationhub;

import com.example.betterher.R;


public class SectionCard {
    private String sectionCardTitle;
    private String sectionCardAuthor;

    private int imageId;
    private int likeImageId;
    private int starImageId;

    // Add a reference to the Content object
    private Content content;

    // Modify the constructor to accept a Content object
    public SectionCard(Content content) {
        this.content = content;
        this.imageId = R.drawable.img_1;
        this.likeImageId = R.drawable.love_unfilled;
        this.starImageId = R.drawable.love_unfilled;

        // Initialize the title and author from the Content object
        this.sectionCardTitle = content.getTitle(); // Assuming Content has a getTitle method
        this.sectionCardAuthor = content.getAuthor(); // Assuming Content has a getAuthor method
    }

    // Getters and setters for sectionCardTitle and sectionCardAuthor are now redundant
    // You might want to remove them or modify them to reflect changes in the Content object

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
        this.sectionCardTitle = content.getTitle();
        this.sectionCardAuthor = content.getAuthor();
    }

    // Existing getters and setters for image IDs...

    public int getLikeImageId() {
        return likeImageId;
    }

    public void setLikeImageId(int likeImageId) {
        this.likeImageId = likeImageId;
    }

    public int getStarImageId() {
        return starImageId;
    }

    public void setStarImageId(int starImageId) {
        this.starImageId = starImageId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getUrl() {
        return content.getUrl();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof SectionCard)) return false;
        SectionCard sectionCard = (SectionCard) obj;
        return sectionCard.getContent().equals(this.getContent()) &&
                sectionCard.getImageId() == this.getImageId() &&
                sectionCard.getLikeImageId() == this.getLikeImageId() &&
                sectionCard.getStarImageId() == this.getStarImageId();
    }
}

