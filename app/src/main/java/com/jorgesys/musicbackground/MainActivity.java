package com.jorgesys.musicbackground;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Class<BackgroundSoundService> BACKGROUND_SOUND_SERVICE_CLASS = BackgroundSoundService.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnPlay = (Button) findViewById(R.id.btnPlay);
        final TextView textView = (TextView) findViewById(R.id.textView);
        updateUI(btnPlay, textView);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMyServiceRunning(BACKGROUND_SOUND_SERVICE_CLASS)) {
                    Intent myService = new Intent(MainActivity.this, BackgroundSoundService.class);
                    startService(myService);
                    updateUI(btnPlay, textView);
                } else {
                    Intent myService = new Intent(MainActivity.this, BackgroundSoundService.class);
                    stopService(myService);
                    updateUI(btnPlay, textView);
                }
            }
        });
    }

    private void updateUI(Button btnPlay, TextView textView) {
        boolean isBackgroundSoundServiceRunning = isMyServiceRunning(BACKGROUND_SOUND_SERVICE_CLASS);
        btnPlay.setText(isBackgroundSoundServiceRunning ? "Pause" : "Start");
        textView.setText(isBackgroundSoundServiceRunning ?
                "Click to stop music in background!" :
                "Click to play music in background!");
        textView.setTextColor(Color.parseColor(isBackgroundSoundServiceRunning ? "#FF0000" : "#0000FF"));
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        Intent myService = new Intent(MainActivity.this, BackgroundSoundService.class);
        stopService(myService);
    }*/

}
