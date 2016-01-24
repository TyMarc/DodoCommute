package com.lesgens.dodocommute;

import android.app.Application;

import com.lesgens.dodocommute.db.DatabaseHelper;

/**
 * Created by marc-antoinehinse on 2016-01-23.
 */
public class AlarmApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper.init(this);
    }
}
