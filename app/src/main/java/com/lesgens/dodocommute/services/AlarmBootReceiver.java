package com.lesgens.dodocommute.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

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
        Log.i(TAG, "onReceive");
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            DatabaseHelper.init(context);
            PendingIntent alarmIntent;

            AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, AlarmBootReceiver.class);

            for(AlarmProfile ap :DatabaseHelper.getInstance().getAlarms()) {
                alarmIntent = PendingIntent.getBroadcast(context, 0, i, 0);
                alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, ap.getDepartureTime() * 1000,
                        AlarmManager.INTERVAL_DAY, alarmIntent);
                Log.i(TAG, "Alarm set for=" + ap.getTimeToWakeUpAsText());
            }
        } else {
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if(alert == null){
                // alert is null, using backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                // I can't see this ever being null (as always have a default notification)
                // but just incase
                if(alert == null) {
                    // alert backup is null, using 2nd backup
                    alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                }
            }
            Ringtone r = RingtoneManager.getRingtone(context, alert);
            r.play();
        }
    }
}