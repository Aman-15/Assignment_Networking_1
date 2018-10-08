package com.example.amanagarwal.assignment_networking_1.network;

import models.Quake;
import retrofit2.Call;
import retrofit2.http.GET;

public interface EarthquakeAPI {

    @GET("query?format=geojson&eventtype=earthquake&orderby=time&minmag=4&limit=20")
    Call<Quake> getEarthquakes();
}
