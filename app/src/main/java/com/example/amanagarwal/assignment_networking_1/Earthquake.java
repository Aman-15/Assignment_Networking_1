package com.example.amanagarwal.assignment_networking_1;

public class Earthquake {

    int id;
    private double magnitude;
    private String place;
    private long time;
    private String url;

    public double getMagnitude() {
        return magnitude;
    }

    public String getPlace() { return place; }

    public long getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

    public int getId() { return id; }

    public Earthquake(int id, double magnitude, String place, long time, String url) {
        this.id = id;
        this.magnitude = magnitude;
        this.place = place;
        this.time = time;
        this.url = url;
    }
}