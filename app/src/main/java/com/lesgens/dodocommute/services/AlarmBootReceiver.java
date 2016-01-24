package com.lesgens.dodocommute.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lesgens.dodocommute.db.DatabaseHelper;
import com.lesgens.dodocommute.model.AlarmProfile;
import com.lesgens.dodocommute.utils.Utils;

/**
 * Created by marc-antoinehinse on 2016-01-24.
 */
public class AlarmBootReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            DatabaseHelper.init(context);
            AlarmManager alarmMgr;
            PendingIntent alarmIntent;

            alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, AlarmBootReceiver.class);

            for(AlarmProfile ap :DatabaseHelper.getInstance().getAlarms()) {
                alarmIntent = PendingIntent.getBroadcast(context, 0, i, 0);
                alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, ap.getDepartureTime() * 1000,
                        AlarmManager.INTERVAL_DAY, alarmIntent);
                Log.i(TAG, "Alarm set for=" + ap.getTimeToWakeUpAsText());
            }
        }
    }
}