package com.example.muguet.evolutivmind.views;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.airbnb.lottie.LottieAnimationView;
import com.example.muguet.evolutivmind.R;
import android.util.Log;

public class OptionsActivity extends AppCompatActivity {

    Button retour;
    Switch notifSwitch;
    TextView heureNotif;
    EditText valHeureNotif;
    SharedPreferences sharedpreferences;
    InputMethodManager imm;
    TimePicker timePicker;
    Button btnLogout;
    Button btnValidTime;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        sharedpreferences = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        btnLogout = findViewById(R.id.btn_logout);
        notifSwitch = findViewById(R.id.notifPush);
        heureNotif = findViewById(R.id.textHeure);
        valHeureNotif = findViewById(R.id.editTime);
        timePicker = findViewById(R.id.timePicker);
        btnValidTime = findViewById(R.id.btnValidTime);

        initSlider();
        setVisibiForHour();

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
            }
        });

        valHeureNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllComponentsGrp1();
                showAllComponentsGrp2();
                imm.hideSoftInputFromWindow(valHeureNotif.getWindowToken(), 0);
            }
        });

        btnValidTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour;
                int minute;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = 0;
                    minute = 0;
                }
                Log.d("tmeDebug",hour+":"+minute);
                hideAllComponentsGrp2();
                showAllComponentsGrp1();
            }
        });
    }

    public void initSlider() {
        boolean notifPush = sharedpreferences.getBoolean("notificationsPush", false);
        notifSwitch.setChecked(notifPush);
    }

    public void hideAllComponentsGrp1() {
        retour.setVisibility(View.INVISIBLE);
        notifSwitch.setVisibility(View.INVISIBLE);
        heureNotif.setVisibility(View.INVISIBLE);
        valHeureNotif.setVisibility(View.INVISIBLE);
        btnLogout.setVisibility(View.INVISIBLE);
        lottieAnimationView.setVisibility(View.INVISIBLE);
    }

    public void hideAllComponentsGrp2() {
        btnValidTime.setVisibility(View.INVISIBLE);
        timePicker.setVisibility(View.INVISIBLE);

    }

    public void showAllComponentsGrp1() {
        retour.setVisibility(View.VISIBLE);
        notifSwitch.setVisibility(View.VISIBLE);
        setVisibiForHour();
        btnLogout.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
    }

    public void showAllComponentsGrp2() {
        btnValidTime.setVisibility(View.VISIBLE);
        timePicker.setVisibility(View.VISIBLE);
    }

    public void setVisibiForHour() {
        if(notifSwitch.isChecked()) {
            heureNotif.setVisibility(View.VISIBLE);
            valHeureNotif.setVisibility(View.VISIBLE);
        } else {
            heureNotif.setVisibility(View.INVISIBLE);
            valHeureNotif.setVisibility(View.INVISIBLE);
        }
    }
}
