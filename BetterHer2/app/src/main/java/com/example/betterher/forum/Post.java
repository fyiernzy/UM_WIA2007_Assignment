package com.example.betterher.forum;

import java.util.List;

public class Post {
    private String title;
    private String content;
    private List<String> imageUrls; // Changed to a list of image URLs

    // No-argument constructor required for Firebase deserialization
    public Post() {
    }

    public Post(String title, String content, List<String> imageUrls) {
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getImageUrl() {
        if (imageUrls == null || imageUrls.isEmpty()) return null;
        return imageUrls.get(0);
    }
}


