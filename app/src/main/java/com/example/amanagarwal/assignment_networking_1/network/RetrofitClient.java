package com.example.amanagarwal.assignment_networking_1.network;

import android.util.Log;

import models.Quake2;
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

        if (retrofit == null)
            retrofit = getClient();

        EarthquakeAPI api = retrofit.create(EarthquakeAPI.class);

        Call<Quake2> call = api.getEarthquakes();

        call.enqueue(new Callback<Quake2>() {
            @Override
            public void onResponse(Call<Quake2> call, Response<Quake2> response) {
                Quake2 quake2 = response.body();
                earthquakeDataListener.onDataFetch(quake2);
            }

            @Override
            public void onFailure(Call<Quake2> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());
                earthquakeDataListener.onFailure();
            }
        });

        Log.e(LOG_TAG, "Done Building Earthquakes");
    }
}
