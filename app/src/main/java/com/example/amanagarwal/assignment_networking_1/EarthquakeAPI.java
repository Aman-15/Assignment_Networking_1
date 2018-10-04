package com.example.amanagarwal.assignment_networking_1;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EarthquakeAPI {

    String BASE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/";

    @GET("query?format=geojson&eventtype=earthquake&orderby=time&minmag=4&limit=20")
    Call<Quake> getEarthquakes();
}
