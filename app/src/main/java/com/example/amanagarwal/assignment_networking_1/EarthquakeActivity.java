package com.example.amanagarwal.assignment_networking_1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import models.EarthquakeData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EarthquakeActivity extends AppCompatActivity {
    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    RecyclerView recyclerView;
    TextView emptyView;
    EarthquakeAdaptor adaptor;

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        emptyView = findViewById(R.id.emptyTextView);

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        Realm.init(this);
        mRealm = Realm.getDefaultInstance();
        RealmResults<EarthquakeData> realmResults = mRealm.where(EarthquakeData.class).findAll();

        adaptor = new EarthquakeAdaptor(null, false, this);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(LOG_TAG, "Refreshing");
                recyclerView.getRecycledViewPool().clear();
                adaptor.notifyDataSetChanged();
                deleteData();
                buildQuakes();
                pullToRefresh.setRefreshing(false);
            }

            void deleteData() {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(EarthquakeData.class);
                    }
                });
            }
        });

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            if (realmResults == null || realmResults.isEmpty()) {
                Log.e(LOG_TAG, "No earthquakes were found");
                buildQuakes();
            }
            else {
                Log.e(LOG_TAG, "Earthquakes found, using cached data");
                adaptor.setData(realmResults);

                Log.e(LOG_TAG, "earthquakeList is neither empty nor null");
                recyclerView.setAdapter(adaptor);
                recyclerView.setLayoutManager(new LinearLayoutManager(EarthquakeActivity.this));
            }
        }
        else {
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            emptyView.setText("NO INTERNET!");
        }
    }

    void buildQuakes() {
        Log.e(LOG_TAG, "Building Earthquakes");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EarthquakeAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

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

                insertEarthquakeData(earthquakeDataList);

                RealmResults<EarthquakeData> realmResults = mRealm.where(EarthquakeData.class).findAll();

                adaptor.setData(realmResults);

                if (realmResults != null && !realmResults.isEmpty()) {
                    Log.e(LOG_TAG, "earthquakeList is neither empty nor null");
                    recyclerView.setAdapter(adaptor);
                    recyclerView.setLayoutManager(new LinearLayoutManager(EarthquakeActivity.this));
                }
                else {
                    Log.e(LOG_TAG, "earthquakeList is either empty or null");
                }

                findViewById(R.id.progressBar).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Quake> call, Throwable t) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                emptyView.setText("Error fetching Earthquakes");
            }

            void insertEarthquakeData(final List<EarthquakeData> earthquakeDataList) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        mRealm.copyToRealm(earthquakeDataList);
                    }
                });
            }
        });

        Log.e(LOG_TAG, "Done Building Earthquakes");
    }
}
