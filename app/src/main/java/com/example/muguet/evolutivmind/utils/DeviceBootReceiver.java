package com.example.muguet.evolutivmind.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean notificationOn = context.getSharedPreferences("MyPref",
                Context.MODE_PRIVATE).getBoolean("notificationsPush", false);
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") && notificationOn) {
            // on device boot compelete, reset the alarm
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            // set time with sharedPref
            String[] splitHourNotif = context.getSharedPreferences("MyPref",
                    Context.MODE_PRIVATE).getString("hourNotificationsPush", "").split(":");
            if (splitHourNotif.length > 1) {
                calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(splitHourNotif[0]));
                calendar.set(Calendar.MINUTE, Integer.valueOf(splitHourNotif[1]));
                calendar.set(Calendar.SECOND, 0);

                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
    }
}
