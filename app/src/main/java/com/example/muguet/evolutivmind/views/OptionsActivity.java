package com.example.muguet.evolutivmind.views;

import android.animation.Animator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.airbnb.lottie.LottieAnimationView;
import com.example.muguet.evolutivmind.R;
import com.example.muguet.evolutivmind.utils.AlarmReceiver;
import com.example.muguet.evolutivmind.utils.NotificationScheduler;

import java.util.Calendar;

public class OptionsActivity extends AppCompatActivity {

    ImageView retour;
    Switch notifSwitch;
    TextView heureNotif;
    TextView valHeureNotif;
    SharedPreferences sharedpreferences;
    ImageView btnLogout;
    LottieAnimationView lottieAnimationView;
    ImageView btnModifHeure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        sharedpreferences = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);

        btnLogout = findViewById(R.id.btn_logout);
        notifSwitch = findViewById(R.id.notifPush);
        heureNotif = findViewById(R.id.textHeure);
        valHeureNotif = findViewById(R.id.textTime);
        btnModifHeure = findViewById(R.id.btnModifHeure);

        btnModifHeure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(OptionsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        valHeureNotif.setText(selectedHour + ":" + selectedMinute);
                        sharedpreferences.edit().putString("hourNotificationsPush", selectedHour + ":" + selectedMinute).apply();
                        NotificationScheduler.setReminder(getBaseContext(),OptionsActivity.class, sharedpreferences);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        notifSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setVisibiForHour();
                sharedpreferences.edit().putBoolean("notificationsPush", notifSwitch.isChecked()).apply();
                String hourNotif = sharedpreferences.getString("hourNotificationsPush", "");
                // afficher le timePicker si l'heure n'a pas été choisi
                if (notifSwitch.isChecked()) {
                    if (hourNotif.equals("")) {
                        // heure non choisi
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(OptionsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                valHeureNotif.setText(selectedHour + ":" + selectedMinute);
                                sharedpreferences.edit().putString("hourNotificationsPush", selectedHour + ":" + selectedMinute).apply();
                            }
                        }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
                    } else {
                        // heure choisi, set the notification
                        NotificationScheduler.setReminder(getBaseContext(),AlarmReceiver.class, sharedpreferences);
                    }
                    initHourOfNotif();
                }
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();

        retour = findViewById(R.id.retour);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("login");
                editor.remove("hourNotificationsPush");
                editor.remove("notificationsPush");

                editor.apply();

                Intent intent = new Intent(getBaseContext(), HubActivity.class);
                startActivity(intent);
                finish();
            }
        });

        initComponents();
    }

    public void initComponents() {
        initSlider();
        initHourOfNotif();
        setVisibiForHour();
    }

    public void initSlider() {
        boolean notifPush = sharedpreferences.getBoolean("notificationsPush", false);
        notifSwitch.setChecked(notifPush);
    }

    public void initHourOfNotif() {
        String hourNotifPush = sharedpreferences.getString("hourNotificationsPush", "");
        valHeureNotif.setText(hourNotifPush);
    }

    public void setVisibiForHour() {
        if (notifSwitch.isChecked()) {
            heureNotif.setVisibility(View.VISIBLE);
            valHeureNotif.setVisibility(View.VISIBLE);
            btnModifHeure.setVisibility(View.VISIBLE);
        } else {
            heureNotif.setVisibility(View.INVISIBLE);
            valHeureNotif.setVisibility(View.INVISIBLE);
            btnModifHeure.setVisibility(View.INVISIBLE);
        }
    }
}
