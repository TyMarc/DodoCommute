package com.lesgens.dodocommute.db;


        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

        import com.google.android.gms.maps.model.LatLng;
        import com.lesgens.dodocommute.enums.TransitMode;
        import com.lesgens.dodocommute.enums.TravelModes;
        import com.lesgens.dodocommute.model.AlarmProfile;
        import com.lesgens.dodocommute.model.Location;

        import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String TAG = "DatabaseHelper";
    private static DatabaseHelper instance;

    private DatabaseHelper(Context context) {
        super(context, "db", null, 2);
    }

    public static void init(final Context context){
        if(instance != null){
            return;
        }
        instance = new DatabaseHelper(context);
    }

    public static DatabaseHelper getInstance(){
        return instance;
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE alarms (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, isOn INTEGER DEFAULT 1, origin_lat TEXT, " +
                "origin_lon TEXT, dest_lat TEXT, dest_lon TEXT, arrival_hour INTEGER, arrival_minute INTEGER, " +
                "routine_hour INTEGER, routine_minute INTEGER, travel_mode TEXT, transit_mode TEXT, departure_time INTEGER);");
        db.execSQL("CREATE TABLE places (id INTEGER PRIMARY KEY AUTOINCREMENT, location_name TEXT, lat TEXT, lon TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if exists alarms");
        db.execSQL("DROP TABLE if exists places");
        onCreate(db);
    }

    public void addPlace(final Location location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("location_name", location.getName());
        cv.put("lat", location.getLocation().latitude);
        cv.put("lon", location.getLocation().longitude);

        final long id = db.insert("places", null, cv);
        location.setId(id);
    }

    public void addOrUpdateAlarm(AlarmProfile ap){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("name", ap.getName());
        cv.put("isOn", ap.isOn() ? 1 : 0);
        cv.put("origin_lat", ap.getOrigin().latitude);
        cv.put("origin_lon", ap.getOrigin().longitude);
        cv.put("dest_lat", ap.getDestination().latitude);
        cv.put("dest_lon", ap.getDestination().longitude);
        cv.put("arrival_hour", ap.getArrivalHour());
        cv.put("arrival_minute", ap.getArrivalMinute());
        cv.put("routine_hour", ap.getRoutineHour());
        cv.put("routine_minute", ap.getRoutineMinute());
        cv.put("transit_mode", ap.getTransitMode().toString());
        cv.put("travel_mode", ap.getTravelMode().toString());
        cv.put("departure_time", ap.getDepartureTime());

        if(ap.getId() == -1) {
            final long id = db.insert("alarms", null, cv);
            ap.setId(id);
            Log.i(TAG, "Succesfully inserted new alarm");
        } else {
            db.update("alarms", cv, "id=?", new String[]{String.valueOf(ap.getId())});
            Log.i(TAG, "Succesfully updated alarm");
        }

    }

    public AlarmProfile getAlarmById(final long id){
        SQLiteDatabase db = this.getReadableDatabase();
        AlarmProfile alarmProfile = new AlarmProfile();
        Cursor c = db.rawQuery("SELECT * FROM alarms WHERE id = ?;", new String[]{String.valueOf(id)} );

        while(c.moveToNext()){
            Log.i(TAG, "Alarm found");
            String name = c.getString(1);
            boolean isOn = c.getInt(2) == 1;
            String originLat = c.getString(3);
            String originLon = c.getString(4);
            String destLat = c.getString(5);
            String destLon = c.getString(6);
            int arrivalHour = c.getInt(7);
            int arrivalMinute = c.getInt(8);
            int routineHour = c.getInt(9);
            int routineMinute = c.getInt(10);
            String travelMode = c.getString(11);
            String transitMode = c.getString(12);
            int departureTime = c.getInt(13);
            alarmProfile.setOrigin(new LatLng(Double.parseDouble(originLat), Double.parseDouble(originLon)));
            alarmProfile.setDestination(new LatLng(Double.parseDouble(destLat), Double.parseDouble(destLon)));
            alarmProfile.setArrivalHour(arrivalHour);
            alarmProfile.setArrivalMinute(arrivalMinute);
            alarmProfile.setRoutineHour(routineHour);
            alarmProfile.setRoutineMinute(routineMinute);
            alarmProfile.setTravelMode(TravelModes.fromString(travelMode));
            alarmProfile.setTransitMode(TransitMode.fromString(transitMode));
            alarmProfile.setDepartureTime(departureTime);
            alarmProfile.setName(name);
            alarmProfile.setIsOn(isOn);
            alarmProfile.setId(id);
            break;
        }

        c.close();

        return alarmProfile;
    }

    public ArrayList<AlarmProfile> getAlarms(){
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<AlarmProfile> alarms = new ArrayList<AlarmProfile>();
        AlarmProfile alarmProfile;
        Cursor c = db.rawQuery("SELECT * FROM alarms;", null );

        while(c.moveToNext()){
            alarmProfile = new AlarmProfile();
            long id = c.getLong(0);
            String name = c.getString(1);
            boolean isOn = c.getInt(2) == 1;
            String originLat = c.getString(3);
            String originLon = c.getString(4);
            String destLat = c.getString(5);
            String destLon = c.getString(6);
            int arrivalHour = c.getInt(7);
            int arrivalMinute = c.getInt(8);
            int routineHour = c.getInt(9);
            int routineMinute = c.getInt(10);
            String travelMode = c.getString(11);
            String transitMode = c.getString(12);
            int departureTime = c.getInt(13);
            alarmProfile.setOrigin(new LatLng(Double.parseDouble(originLat), Double.parseDouble(originLon)));
            alarmProfile.setDestination(new LatLng(Double.parseDouble(destLat), Double.parseDouble(destLon)));
            alarmProfile.setArrivalHour(arrivalHour);
            alarmProfile.setArrivalMinute(arrivalMinute);
            alarmProfile.setRoutineHour(routineHour);
            alarmProfile.setRoutineMinute(routineMinute);
            alarmProfile.setTravelMode(TravelModes.fromString(travelMode));
            alarmProfile.setTransitMode(TransitMode.fromString(transitMode));
            alarmProfile.setDepartureTime(departureTime);
            alarmProfile.setName(name);
            alarmProfile.setIsOn(isOn);
            alarmProfile.setId(id);
            alarms.add(alarmProfile);
        }

        c.close();

        return alarms;
    }

    public ArrayList<Location> getLocations(){
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Location> locations = new ArrayList<Location>();
        Location location;
        Cursor c = db.rawQuery("SELECT * FROM places;", null );

        while(c.moveToNext()){
            location = new Location();
            long id = c.getLong(0);
            String name = c.getString(1);
            String lat = c.getString(2);
            String lon = c.getString(3);
            location.setLocationName(name);
            location.setLocation(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)));
            location.setId(id);
            locations.add(location);
        }

        c.close();

        return locations;
    }

    public void deletePlaceById(final long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("places", "id=?", new String[]{String.valueOf(id)});
    }

    public void deleteAlarmById(final long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("alarms", "id=?", new String[]{String.valueOf(id)});
    }
}