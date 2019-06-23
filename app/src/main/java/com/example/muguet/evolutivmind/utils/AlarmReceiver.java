package com.example.muguet.evolutivmind.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context.getSharedPreferences("MyPref",
                Context.MODE_PRIVATE).getBoolean("notificationsPush", false)) {
            // show toast
            Toast.makeText(context, "Alarm running", Toast.LENGTH_SHORT).show();
            Log.d("debug", "alarm Received");
            // create the notification
        }
    }
}
