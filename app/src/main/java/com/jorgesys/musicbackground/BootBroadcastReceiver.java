package com.jorgesys.musicbackground;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BootBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction().toString();
        Log.i(TAG, "onReceive(" + action + ")" );

        // u can start your service here
        Toast.makeText(context, "boot completed action has got", Toast.LENGTH_LONG).show();

        Intent myService = new Intent(context, BackgroundSoundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(myService);
        } else {
            context.startService(myService);
        }
    }
}
