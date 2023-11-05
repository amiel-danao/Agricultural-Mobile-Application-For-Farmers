package com.thesis.amaff.models;

import com.google.firebase.Timestamp;

public class Employee {
    private String email;
    private String user_id;
    private String mobile_number;
    private String name;
    private String firstName;
    private String lastName;
    private Timestamp dateCreated;

    public Employee() {
        // Default constructor is needed for Firestore to deserialize the data
    }

    public Employee(String email, String user_id, String mobile_number, String name, String firstName, String lastName, Timestamp dateCreated) {
        this.email = email;
        this.user_id = user_id;
        this.mobile_number = mobile_number;
        this.name = name;
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
        return user_id;
    }

    public void setUid(String user_id) {
        this.user_id = user_id;
    }

    public String getMobileNumber() {
        return mobile_number;
    }

    public void setMobileNumber(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Employee{" +
                "email='" + email + '\'' +
                ", user_id='" + user_id + '\'' +
                ", mobile_number='" + mobile_number + '\'' +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
