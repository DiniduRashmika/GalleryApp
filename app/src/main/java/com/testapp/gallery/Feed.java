package com.testapp.gallery;

public class Feed {

    private String userid;
    private String  imgPath;
    private String  timestamp;
    private String  lat;
    private String  lon;

    public Feed(String userid, String imgPath, String timestamp, String lat, String lon) {
        this.userid = userid;
        this.imgPath = imgPath;
        this.timestamp = timestamp;
        this.lat = lat;
        this.lon = lon;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
