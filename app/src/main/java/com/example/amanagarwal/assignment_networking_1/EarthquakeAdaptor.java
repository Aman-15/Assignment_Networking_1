package com.example.amanagarwal.assignment_networking_1;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EarthquakeAdaptor extends RecyclerView.Adapter<EarthquakeAdaptor.EarthquakeViewHolder> {

    private Context context;
    private List<Earthquake> earthquakeList;

    public EarthquakeAdaptor(Context context, List<Earthquake> earthquakeList) {
        this.context = context;
        this.earthquakeList = earthquakeList;
    }

    @NonNull
    @Override
    public EarthquakeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.earthquake_list, null);
        return new EarthquakeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EarthquakeViewHolder earthquakeViewHolder, int i) {
        Earthquake currentEarthquake = earthquakeList.get(i);

        earthquakeViewHolder.magnitudeView.setText(formatMagnitude(currentEarthquake.getMagnitude()));

        String[] location = getOffsetAndPlace(currentEarthquake.getPlace());

        earthquakeViewHolder.offsetView.setText(location[0]);

        earthquakeViewHolder.placeView.setText(location[1]);

        Date dateObject = new Date(currentEarthquake.getTime());
        String formattedDate = formatDate(dateObject);
        earthquakeViewHolder.dateView.setText(formattedDate);

        String formattedTime = formatTime(dateObject);
        earthquakeViewHolder.timeView.setText(formattedTime);

        GradientDrawable magnitudeCircle = (GradientDrawable)earthquakeViewHolder.magnitudeView.getBackground();
        magnitudeCircle.setColor(getMagnitudeColor(currentEarthquake.getMagnitude()));
    }

    private String[] getOffsetAndPlace(String location) {
        String[] out = new String[2];
        if (location.contains(" of")) {
            out[0] = location.substring(0, location.indexOf("of") + 2);
            out[1] = location.substring(location.indexOf("of") + 3, location.length());
        }
        else {
            out[0] = "Near the";
            out[1] = location;
        }

        return out;
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, y", Locale.US);
        return formatter.format(date);
    }

    private String formatTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.US);
        return formatter.format(date);
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int)Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(context, magnitudeColorResourceId);
    }


    @Override
    public int getItemCount() {
        return earthquakeList.size();
    }

    public class EarthquakeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView magnitudeView, offsetView, placeView, dateView, timeView;

        public EarthquakeViewHolder(@NonNull View itemView) {
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
            Earthquake currentEarthquake = earthquakeList.get(getAdapterPosition());

            Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
            context.startActivity(websiteIntent);
        }
    }
}
