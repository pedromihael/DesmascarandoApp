package com.pedromihael.desmascarandoapp;

import java.util.Date;

public class Post {

    private Double latitude;
    private Double longitude;
    private Date time;

    public Post(Double latitude, Double longitude, Date time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
