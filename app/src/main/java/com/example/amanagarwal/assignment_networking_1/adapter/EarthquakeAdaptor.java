package com.example.amanagarwal.assignment_networking_1.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amanagarwal.assignment_networking_1.R;
import com.example.amanagarwal.assignment_networking_1.util.TextUtil;

import java.util.Date;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import models.Properties;

public class EarthquakeAdaptor extends RealmRecyclerViewAdapter<Properties, EarthquakeAdaptor.EarthquakeViewHolder> {

    private Context context;
    private OrderedRealmCollection<Properties> data;

    public EarthquakeAdaptor(@Nullable OrderedRealmCollection<Properties> data, boolean autoUpdate, Context context) {
        super(data, autoUpdate);
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public EarthquakeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.earthquake_list, null);
        return new EarthquakeViewHolder(view);
    }

    public void setData(OrderedRealmCollection<Properties> data) {
        this.data = data;
    }

    @Override
    public void onBindViewHolder(@NonNull EarthquakeViewHolder earthquakeViewHolder, int i) {
        Properties properties = data.get(i);
        String[] location = TextUtil.getOffsetAndPlace(properties.getPlace());
        Date dateObject = new Date(properties.getTime());
        String formattedDate = TextUtil.formatDate(dateObject);
        String formattedTime = TextUtil.formatTime(dateObject);
        GradientDrawable magnitudeCircle = (GradientDrawable)earthquakeViewHolder.magnitudeView.getBackground();
        int magnitudeColorId = TextUtil.getMagnitudeColor(properties.getMagnitude());

        earthquakeViewHolder.magnitudeView.setText(TextUtil.formatMagnitude(properties.getMagnitude()));
        earthquakeViewHolder.offsetView.setText(location[0]);
        earthquakeViewHolder.placeView.setText(location[1]);
        earthquakeViewHolder.dateView.setText(formattedDate);
        earthquakeViewHolder.timeView.setText(formattedTime);
        magnitudeCircle.setColor(ContextCompat.getColor(context, magnitudeColorId));
    }

    @Override
    public int getItemCount() { return data.size(); }

    public class EarthquakeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView magnitudeView, offsetView, placeView, dateView, timeView;

        EarthquakeViewHolder(@NonNull View itemView) {
            super(itemView);

            magnitudeView = itemView.findViewById(R.id.magnitude);
            offsetView = itemView.findViewById(R.id.offset);
            placeView = itemView.findViewById(R.id.place);
            dateView = itemView.findViewById(R.id.date);
            timeView = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Properties properties = data.get(getAdapterPosition());

            Uri earthquakeUri = Uri.parse(properties.getUrl());
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
            context.startActivity(websiteIntent);
        }
    }
}
