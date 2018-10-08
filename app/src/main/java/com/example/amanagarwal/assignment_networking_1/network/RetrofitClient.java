package com.example.amanagarwal.assignment_networking_1.network;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.example.amanagarwal.assignment_networking_1.R;
import com.example.amanagarwal.assignment_networking_1.activities.EarthquakeActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import models.EarthquakeData;
import models.Quake;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String LOG_TAG = RetrofitClient.class.getName();
    private static String BASE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/";
    private static Retrofit retrofit = null;

    private static Retrofit getClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static void buildQuakes(final EarthquakeDataListener earthquakeDataListener) {
        Log.e(LOG_TAG, "Building Earthquakes");

        retrofit = getClient();
        EarthquakeAPI api = retrofit.create(EarthquakeAPI.class);

        Call<Quake> call = api.getEarthquakes();

        call.enqueue(new Callback<Quake>() {
            @Override
            public void onResponse(Call<Quake> call, Response<Quake> response) {
                Quake earthquakes = response.body();

                List<Quake.Features> features = earthquakes.getFeatures();
                List<EarthquakeData> earthquakeDataList = new ArrayList<>();

                for (Quake.Features feature: features) {
                    Quake.Features.Properties quake = feature.getProperties();
                    EarthquakeData data = new EarthquakeData();
                    data.setMagnitude(quake.getMag());
                    data.setPlace(quake.getPlace());
                    data.setTime(quake.getTime());
                    data.setUrl(quake.getUrl());
                    earthquakeDataList.add(data);
                }

                earthquakeDataListener.onDataFetch(earthquakeDataList);
            }

            @Override
            public void onFailure(Call<Quake> call, Throwable t) {
                earthquakeDataListener.onFailure();
            }
        });

        Log.e(LOG_TAG, "Done Building Earthquakes");
    }
}
