package com.example.amanagarwal.assignment_networking_1.util;

import android.support.v4.content.ContextCompat;

import com.example.amanagarwal.assignment_networking_1.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TextUtil {

    public static String[] getOffsetAndPlace(String location) {
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

    public static String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, y", Locale.US);
        return formatter.format(date);
    }

    public static String formatTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.US);
        return formatter.format(date);
    }

    public static int getMagnitudeColor(double magnitude) {
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
        return magnitudeColorResourceId;
    }
}
