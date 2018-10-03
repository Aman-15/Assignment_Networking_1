package com.example.amanagarwal.assignment_networking_1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<List<Earthquake>> {
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=4&limit=20";

    RecyclerView recyclerView;
    EarthquakeAdaptor earthqakeAdaptor;
    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        emptyView = (TextView)findViewById(R.id.emptyTextView);

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected())
            initLoader();
        else {
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            emptyView.setText("NO INTERNET!");
        }
    }

    public void initLoader() {
        Log.e(LOG_TAG, "Loader initiated");
        getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);
    }

    @Override
    public android.content.Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        Log.e(LOG_TAG, "onCreateLoader");
        return new EarthquakeLoader(this, url);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Earthquake>> loader, List<Earthquake> data) {
        Log.e(LOG_TAG, "onLoadFinished");
        earthqakeAdaptor = new EarthquakeAdaptor(this, data);

        if (data != null && !data.isEmpty()) {
            Log.e(LOG_TAG, "earthquakeList is nor empty nor null");
            recyclerView.setAdapter(earthqakeAdaptor);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        else {
            Log.e(LOG_TAG, "earthquakeList is either empty or null");
        }

        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Earthquake>> loader) {
        Log.e(LOG_TAG, "onLoaderReset");
    }
}
