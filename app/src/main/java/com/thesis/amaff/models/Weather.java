package com.thesis.amaff.models;

public class Weather {
    public String status;
    public float temperature;

    public Weather() {
    }

    public Weather(String status, float temperature) {
        this.status = status;
        this.temperature = temperature;
    }
}
