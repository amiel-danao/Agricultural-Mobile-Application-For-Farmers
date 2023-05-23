package com.thesis.amaff.ui.models;

import com.google.firebase.Timestamp;

public class UserProfile {
    private String email;
    private String uid;
    private String firstName;
    private String lastName;
    private Timestamp dateCreated;

    public UserProfile() {
        // Empty constructor required for Firestore deserialization
    }

    public UserProfile(String email, String uid, String firstName, String lastName, Timestamp dateCreated) {
        this.email = email;
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateCreated = dateCreated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
}
