package com.example.mohammedabdullah3296.helpme.models;

/**
 * Created by Mohammed Abdullah on 11/14/2017.
 */

public class Feed {
    private String id;
    private String image;
    private String placename;
    private String datetime;
    private String description;
    private String usermail;
    private String userphone;
    private String childname;
    private String childage;
    private String childsex;
    private String height;
    private String weight;
    private String hair;
    private String eyes;
    private String latitude;
    private String longitude;
    private String username;
    private String userImage;

    public Feed() {
    }

    public Feed(String id, String image, String placename, String datetime, String description, String usermail, String userphone, String childname, String childage, String childsex,
                String height, String weight, String hair, String eyes, String latitude, String longitude, String username, String userImage) {
        this.id = id;
        this.image = image;
        this.placename = placename;
        this.datetime = datetime;
        this.description = description;
        this.usermail = usermail;
        this.userphone = userphone;
        this.childname = childname;
        this.childage = childage;
        this.childsex = childsex;
        this.height = height;
        this.weight = weight;
        this.hair = hair;
        this.eyes = eyes;
        this.latitude = latitude;
        this.longitude = longitude;
        this.username = username;
        this.userImage = userImage;
    }

    public String getUserImage() {
        return userImage;

    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPlacename() {
        return placename;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getChildname() {
        return childname;
    }

    public void setChildname(String childname) {
        this.childname = childname;
    }

    public String getChildage() {
        return childage;
    }

    public void setChildage(String childage) {
        this.childage = childage;
    }

    public String getChildsex() {
        return childsex;
    }

    public void setChildsex(String childsex) {
        this.childsex = childsex;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return this.image + "  >>" + this.description;


    }
}
