package com.example.amanagarwal.assignment_networking_1.network;

import java.util.List;

import models.EarthquakeData;

public interface EarthquakeDataListener {
    void onFailure();
    void onDataFetch(List<EarthquakeData> earthquakeDataList);
}
