package com.example.mohammedabdullah3296.helpme.models;

/**
 * Created by Mohammed Abdullah on 11/13/2017.
 */

public class User {
    private String ID;
    private String firstName;
    private String secondName;
    private String email;
    private String phone;
    private String profileImage;

    public User() {
    }

    public User(String ID, String firstName, String secondName, String email, String phone, String profileImage) {
        this.ID = ID;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.phone = phone;
        this.profileImage = profileImage;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
