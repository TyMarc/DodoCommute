package com.lesgens.dodocommute.model;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.lesgens.dodocommute.enums.TransitMode;
import com.lesgens.dodocommute.enums.TravelModes;
import com.lesgens.dodocommute.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by marc-antoinehinse on 2016-01-23.
 */
public class AlarmProfile {
    private long id;
    private LatLng origin;
    private LatLng destination;
    private int arrivalHour;
    private int arrivalMinute;
    private int routineHour;
    private int routineMinute;
    private TravelModes travelMode;
    private TransitMode transitMode;
    private long departureTime;
    private String name;
    private boolean isOn;

    public AlarmProfile() {
        id = -1;
        arrivalMinute = -1;
        arrivalHour = -1;
        routineHour = -1;
        routineMinute = -1;
        travelMode = TravelModes.TRANSIT;
        transitMode = TransitMode.SUBWAY;
        isOn = false;
        name = null;
    }

    public int getArrivalHour() {
        return arrivalHour;
    }

    public void setArrivalHour(int arrivalHour) {
        this.arrivalHour = arrivalHour;
    }

    public int getArrivalMinute() {
        return arrivalMinute;
    }

    public void setArrivalMinute(int arrivalMinute) {
        this.arrivalMinute = arrivalMinute;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public int getRoutineHour() {
        return routineHour;
    }

    public void setRoutineHour(int routineHour) {
        this.routineHour = routineHour;
    }

    public int getRoutineMinute() {
        return routineMinute;
    }

    public void setRoutineMinute(int routineMinute) {
        this.routineMinute = routineMinute;
    }

    public TravelModes getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(TravelModes travelMode) {
        this.travelMode = travelMode;
    }

    public TransitMode getTransitMode() {
        return transitMode;
    }

    public void setTransitMode(TransitMode transitMode) {
        this.transitMode = transitMode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(long departureTime) {
        this.departureTime = departureTime;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Alarm profile: ");

        if(origin != null) {
            sb.append("\nOrigin=[" + origin.latitude + ";" + origin.longitude + "]");
        }

        if(destination != null) {
            sb.append("\nDestination=[" + destination.latitude + ";" + destination.longitude + "]");
        }

        sb.append("\nArrival time=" + arrivalHour + "h" + arrivalMinute + "m");
        sb.append("\nRoutine time=" + routineHour + "h" + routineMinute + "m");

        return sb.toString();
    }

    public String getArrivalTime() {
        Calendar now = Calendar.getInstance();
        Calendar arrival = (Calendar) now.clone();
        arrival.set(Calendar.HOUR_OF_DAY, arrivalHour);
        arrival.set(Calendar.MINUTE, arrivalMinute);
        arrival.set(Calendar.SECOND, 0);

        if(now.after(arrival)) {
            arrival.add(Calendar.DAY_OF_YEAR, 1);
        }

        return String.valueOf(arrival.getTimeInMillis() / 1000);
    }

    public long getTimeToWakeUp() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(departureTime * 1000));
        calendar.add(Calendar.HOUR_OF_DAY, -routineHour);
        calendar.add(Calendar.MINUTE, -routineMinute);

        return calendar.getTimeInMillis() / 1000;
    }

    public String getTimeToWakeUpAsText() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(departureTime * 1000));
        calendar.add(Calendar.HOUR_OF_DAY, -routineHour);
        calendar.add(Calendar.MINUTE, -routineMinute);


        Log.i("AlarmProfile", "Departure time=" + departureTime);
        return Utils.getTimeAsText(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    public void parseDeparture(final JSONObject json) {
        try {
            JSONArray routes = json.getJSONArray("routes");
            JSONObject leg = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0);
            JSONObject duration = leg.getJSONObject("departure_time");

            departureTime = duration.getLong("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
