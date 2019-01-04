package com.jorgesys.musicbackground;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jorgesys on 12/09/2016.
 */

public class Preferences {

    private static final String PREFS = "SoundPreferences";

    public static void setMediaPosition(Context ctx, int position) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit().putInt("position", position).apply();
    }

    public static int getMediaPosition(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getInt("position", 0);
    }

}
