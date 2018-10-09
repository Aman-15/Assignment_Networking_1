package com.example.amanagarwal.assignment_networking_1.network;

import models.Quake2;

public interface EarthquakeDataListener {
    void onFailure();
    void onDataFetch(Quake2 quake2);
}
