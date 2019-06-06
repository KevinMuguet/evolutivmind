package com.example.muguet.evolutivmind.views;

import android.animation.Animator;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.airbnb.lottie.LottieAnimationView;
import com.example.muguet.evolutivmind.R;

import java.util.Calendar;

public class OptionsActivity extends AppCompatActivity {

    Button retour;
    Switch notifSwitch;
    TextView heureNotif;
    TextView valHeureNotif;
    SharedPreferences sharedpreferences;
    Button btnLogout;
    LottieAnimationView lottieAnimationView;
    Button btnModifHeure;

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
                        valHeureNotif.setText( selectedHour + ":" + selectedMinute);
                        sharedpreferences.edit().putString("hourNotificationsPush",selectedHour + ":" + selectedMinute).apply();
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
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

        lottieAnimationView = findViewById(R.id.animation_view_options);
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
//                Log.d("debugPerso","start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {

//                Log.d("debugPerso","end");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
//                Log.d("debugPerso","cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                lottieAnimationView.pauseAnimation();
//                Log.d("debugPerso","repeat");
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("login");

                editor.apply();

                Intent intent = new Intent(getBaseContext(), HubActivity.class);
                startActivity(intent);
                finish();
            }
        });

        notifSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibiForHour();
                sharedpreferences.edit().putBoolean("notificationsPush",notifSwitch.isChecked()).apply();
                if(notifSwitch.isChecked()) {
                    initHourOfNotif();
                    // activer les notifications
                } else {
                    // desactiver les notifications
                }
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
        if(notifSwitch.isChecked()) {
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
