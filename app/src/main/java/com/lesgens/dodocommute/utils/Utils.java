package com.lesgens.dodocommute.utils;

import android.content.Context;
import android.util.TypedValue;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by marc-antoinehinse on 2016-01-23.
 */
public class Utils {

    public static String getTimeAsText(final int hour, final int minute) {
        StringBuilder sb = new StringBuilder();

        if(hour < 10) {
            sb.append("0");
        }

        sb.append(hour);

        sb.append("h");

        if(minute < 10) {
            sb.append("0");
        }

        sb.append(minute);

        return sb.toString();
    }

    public static int dpInPixels(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources()
                .getDisplayMetrics());
    }

    public static String getGoogleMapImageUrl(final LatLng location) {
        return "http://maps.googleapis.com/maps/api/staticmap?center="  + location.latitude + "," + location.longitude + "&zoom=13&scale=false&size=600x300&maptype=roadmap&format=png&visual_refresh=true&markers=size:mid%7Ccolor:0xff0000%7C%7C" + location.latitude + "," + location.longitude;
    }
}
