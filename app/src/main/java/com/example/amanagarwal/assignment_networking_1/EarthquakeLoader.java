package com.example.amanagarwal.assignment_networking_1;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class EarthquakeLoader  extends AsyncTaskLoader<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    EarthquakeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        Log.e(LOG_TAG, "onStartLoading");
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        Log.e(LOG_TAG, "loadInBackground");
        return buildQuakes();
    }

    private List<Earthquake> buildQuakes() {

        Log.e(LOG_TAG, "Building Earthquakes");

        ArrayList<Earthquake> earthquakeList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EarthquakeAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EarthquakeAPI api = retrofit.create(EarthquakeAPI.class);

        Call<Quake> call = api.getEarthquakes();

        try {
            Quake earthquakes = call.execute().body();

            List<Quake.Features> features = earthquakes.getFeatures();

            for (Quake.Features feature: features) {
                Quake.Features.Properties quake = feature.getProperties();
                Earthquake earthquake = new Earthquake(quake.getMag(), quake.getPlace(), quake.getTime(), quake.getUrl());
                earthquakeList.add(earthquake);
            }
        }
        catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
            return earthquakeList;
        }

        Log.e(LOG_TAG, "Done Building Earthquakes");

        return earthquakeList;
    }
}
