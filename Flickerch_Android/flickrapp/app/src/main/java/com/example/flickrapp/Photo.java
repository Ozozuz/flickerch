package com.example.flickrapp;


import java.io.Serializable;

//Class the only exist to represent and hold info about a photo
class Photo implements Serializable {

    private static final long serialVersionUID =1L;

    private String title;
    private String author;
    private String url;
    private String tags;

    public Photo(String title, String author, String url, String tags) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getTags() {
        return tags;
    }
}
