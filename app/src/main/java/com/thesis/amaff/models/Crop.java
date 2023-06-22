package com.thesis.amaff.models;

public class Crop {
    private String name;
    private String description;
    private int count;

    public Crop() {
        // Empty constructor required for Firestore
    }

    public Crop(String name, String description, int count) {
        this.name = name;
        this.description = description;
        this.count = count;
    }

    // Getter and setter methods for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter methods for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and setter methods for count
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
