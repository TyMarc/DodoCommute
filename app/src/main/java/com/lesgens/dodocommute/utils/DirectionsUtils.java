package com.lesgens.dodocommute.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lesgens.dodocommute.R;
import com.lesgens.dodocommute.model.AlarmProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by marc-antoinehinse on 2016-01-23.
 */
public class DirectionsUtils {

    private static final String TAG = "DirectionsUtils";
    private static DirectionsUtils instance;
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json";
    private static final String ORIGIN = "origin";
    private static final String DESTINATION = "destination";
    private static final String KEY = "key";
    private static final String ARRIVAL_TIME = "arrival_time";


    public interface DirectionsListener {
        void onSuccess(final AlarmProfile alarmProfile);

        void onError(final String error);
    }


    private DirectionsUtils() {}

    public static final DirectionsUtils getInstance() {
        if (instance == null) {
            instance = new DirectionsUtils();
        }

        return instance;
    }

    public void execute(final Context context, final AlarmProfile alarmProfile, final DirectionsListener listener) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        HashMap<String,String> params = new HashMap<String, String>();
        params.put(ORIGIN, alarmProfile.getOrigin().latitude + "," + alarmProfile.getOrigin().longitude);
        params.put(DESTINATION, alarmProfile.getDestination().latitude + "," + alarmProfile.getDestination().longitude);
        params.put(ARRIVAL_TIME, alarmProfile.getArrivalTime());
        params.put("mode", "transit");
        params.put(KEY, context.getResources().getString(R.string.maps_api_key));

        final String url = BASE_URL + getParams(params);
        Log.i(TAG, url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            if (listener != null) {
                                alarmProfile.parseDeparture(jo);
                                listener.onSuccess(alarmProfile);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (listener != null) {
                                listener.onError(e.toString());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.toString());
                if (listener != null) {
                    listener.onError(error.toString());
                }
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private String getParams(final HashMap<String, String> params) {
        StringBuilder builder = new StringBuilder();
        for (String key : params.keySet())
        {
            Object value = params.get(key);
            if (value != null)
            {
                try
                {
                    value = URLEncoder.encode(String.valueOf(value), "utf-8");


                    if (builder.length() > 0)
                        builder.append("&");
                    builder.append(key).append("=").append(value);
                }
                catch (UnsupportedEncodingException e)
                {
                }
            }
        }

        return "?" + builder.toString();
    }
}
