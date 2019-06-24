package com.example.muguet.evolutivmind.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.muguet.evolutivmind.R;
import com.example.muguet.evolutivmind.views.HubActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context.getSharedPreferences("MyPref",
                Context.MODE_PRIVATE).getBoolean("notificationsPush", false)) {

            if (intent.getAction() != null && context != null) {
                if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                    // Set the alarm here.
                    NotificationScheduler.setReminder(context, AlarmReceiver.class, context.getSharedPreferences("MyPref",
                            Context.MODE_PRIVATE));
                    return;
                }
            }
        }

        //Trigger the notification
        NotificationScheduler.showNotification(context, HubActivity.class, context.getString(R.string.title_notification), context.getString(R.string.text_notification));

    }
}
