package com.example.amanagarwal.assignment_networking_1;

public class Earthquake {
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

    @Override
    public String toString() {
        return "Earthquake{" +
                ", magnitude=" + magnitude +
                ", place='" + place + '\'' +
                ", time=" + time +
                ", url='" + url + '\'' +
                '}';
    }
}