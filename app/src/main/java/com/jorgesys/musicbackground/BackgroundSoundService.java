package com.jorgesys.musicbackground;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by jorgesys on 02/02/2015.
 */

/* Add declaration of this service into the AndroidManifest.xml inside application tag*/

public class BackgroundSoundService extends Service {
    private static final int FOREGROUND_SERVICE_ID = 111;
    private static final String TAG = "BackgroundSoundService";
    MediaPlayer player;

    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "onBind()" );
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
        player = MediaPlayer.create(this, R.raw.bensound_funnysong);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
        Toast.makeText(this, "Service started...", Toast.LENGTH_SHORT).show();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand()");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = "com.jorgesys.musicbackground";
            String channelName = "My BackgroundSound Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            Notification.Builder builder = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("BackgroundSoundService Running")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true);
            Notification notification = builder.build();
            startForeground(FOREGROUND_SERVICE_ID, notification);
            Log.e(TAG,"startForeground >= Build.VERSION_CODES.O");
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("BackgroundSoundService Running")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true);
            Notification notification = builder.build();
            startForeground(FOREGROUND_SERVICE_ID, notification);
            Log.e(TAG,"startForeground < Build.VERSION_CODES.O");
        }

        if(Preferences.getMediaPosition(getApplicationContext())>0){
            Log.i(TAG, "onStartCommand(), position stored, continue from position : " + Preferences.getMediaPosition(getApplicationContext()));
            player.start();
            player.seekTo(Preferences.getMediaPosition(getApplicationContext()));
        }else {
            Log.i(TAG, "onStartCommand() Start!...");
            player.start();
        }
        //re-create the service if it is killed.
        return Service.START_STICKY;
    }

    public IBinder onUnBind(Intent arg0) {
        Log.i(TAG, "onUnBind()");
        return null;
    }

    public void onStop() {
        Log.i(TAG, "onStop()");
        Preferences.setMediaPosition(getApplicationContext(), player.getCurrentPosition());
    }

    public void onPause() {
        Log.i(TAG, "onPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy() , service stopped! Media position: " + player.getCurrentPosition());
        //Save current position before destruction.
        Preferences.setMediaPosition(getApplicationContext(), player.getCurrentPosition());
        player.pause();
        player.release();
    }

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory()");
        Preferences.setMediaPosition(getApplicationContext(), player.getCurrentPosition());
    }

    //Inside AndroidManifest.xml add android:stopWithTask="false" to the Service definition.
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i(TAG, "onTaskRemoved(), save current position: " + player.getCurrentPosition());
        //instead of stop service, save the current position.
        //stopSelf();
        Preferences.setMediaPosition(getApplicationContext(), player.getCurrentPosition());
    }

}
