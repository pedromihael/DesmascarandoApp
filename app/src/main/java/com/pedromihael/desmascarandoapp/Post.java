package com.pedromihael.desmascarandoapp;

public class Post {

    private Double latitude;
    private Double longitude;
    private String time;
    private String author;
    private String post_id;

    public Post(String author, Double latitude, Double longitude, String time, String post_id) {
        this.author = author;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.post_id = post_id;
    }

    public Post() {
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public String getPost_id() { return post_id; }

    public void setPost_id(String post_id) { this.post_id = post_id; }


}
