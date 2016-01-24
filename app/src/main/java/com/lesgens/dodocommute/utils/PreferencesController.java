package com.lesgens.dodocommute.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by marc-antoinehinse on 2016-01-23.
 */
public class PreferencesController {
    private static final String PREF_NAME = "dodo_prefs";

    public static final String FIRST_TIME_USE = "USER_PREF_FIRST_TIME_USE";

    public static void setPreference(final Context context, final String preference, final String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(preference, value);
        editor.commit();
    }

    public static void setPreference(final Context context, final String preference, final boolean value){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(preference, value);
        editor.commit();
    }

    public static boolean isFirstUse(final Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean firstUse = prefs.getBoolean(FIRST_TIME_USE, true);
        return firstUse;
    }
}