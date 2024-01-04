package com.example.swipablecardtest.informationhub;

public class Content {
    private String author;
    private String title;
    private String type;
    private String imageUrl;
    private String url;
    private String field;
    public Content() {
    }

    // Constructor with all fields
    public Content(String author, String title, String type, String imageUrl, String url, String field) {
        this.author = author;
        this.title = title;
        this.type = type;
        this.imageUrl = imageUrl;
        this.url = url;
        this.field = field;
    }

    // Getters and setters for all fields
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Content)) return false;
        Content content = (Content) o;
        return author.equals(content.author) &&
                title.equals(content.title) &&
                type.equals(content.type) &&
                imageUrl.equals(content.imageUrl) &&
                url.equals(content.url) &&
                field.equals(content.field);
    }
}
