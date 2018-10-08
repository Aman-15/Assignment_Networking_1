package com.example.amanagarwal.assignment_networking_1.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.amanagarwal.assignment_networking_1.adapter.EarthquakeAdaptor;
import com.example.amanagarwal.assignment_networking_1.R;
import com.example.amanagarwal.assignment_networking_1.network.EarthquakeDataListener;
import com.example.amanagarwal.assignment_networking_1.network.NetworkInformation;
import com.example.amanagarwal.assignment_networking_1.network.RetrofitClient;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import models.EarthquakeData;

public class EarthquakeActivity extends AppCompatActivity implements EarthquakeDataListener {
    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private RecyclerView recyclerView;
    private TextView emptyView;
    private EarthquakeAdaptor adaptor;
    private SwipeRefreshLayout pullToRefresh;

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        emptyView = findViewById(R.id.emptyTextView);

        Realm.init(this);
        mRealm = Realm.getDefaultInstance();
        RealmResults<EarthquakeData> realmResults = mRealm.where(EarthquakeData.class).findAll();

        adaptor = new EarthquakeAdaptor(null, false, this);

        initialisePullToRefresh();

        // If there is a network connection, fetch data
        if (NetworkInformation.isConnectedToNetwork(getApplicationContext())) {

            // Cache is Empty
            if (realmResults == null || realmResults.isEmpty()) {
                Log.e(LOG_TAG, "No earthquakes were found");
                RetrofitClient.buildQuakes(this);
            }

            // Cache
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

    private void initialisePullToRefresh() {
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(LOG_TAG, "Refreshing");
//                deleteData();
                pullToRefresh.setRefreshing(true);
                RetrofitClient.buildQuakes(EarthquakeActivity.this);
                pullToRefresh.setRefreshing(false);
            }

        });

        pullToRefresh.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_red_light);
    }

    @Override
    public void onFailure() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        emptyView.setText("Error fetching Earthquakes");
    }

    @Override
    public void onDataFetch(List<EarthquakeData> earthquakeDataList) {
//        mRealm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.deleteAll();
//            }
//        });
        //delete
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

    void insertEarthquakeData(final List<EarthquakeData> earthquakeDataList) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(earthquakeDataList);
        mRealm.commitTransaction();
    }
}
