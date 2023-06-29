package com.thesis.amaff.models;

import java.util.List;

public class Crop {
    private String name;
    private String description;
    private String variety = "";
    private int count;
    private List<String> variation;
    private String iconUrl;

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

    public List<String> getVariation() {
        return variation;
    }

    public void setVariation(List<String> variety) {
        this.variation = variety;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
