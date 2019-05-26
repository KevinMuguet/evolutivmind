package com.example.muguet.evolutivmind.views;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.airbnb.lottie.LottieAnimationView;
import com.example.muguet.evolutivmind.R;
import android.util.Log;
import org.w3c.dom.Text;

public class OptionsActivity extends AppCompatActivity {

    Button retour;
    Switch notifSwitch;
    TextView heureNotif;
    EditText valHeureNotif;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        sharedpreferences = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);

        Button button = findViewById(R.id.btn_logout);
        notifSwitch = findViewById(R.id.notifPush);
        heureNotif = findViewById(R.id.textHeure);
        valHeureNotif = findViewById(R.id.editTime);

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

        final LottieAnimationView lottieAnimationView = findViewById(R.id.animation_view_options);
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

        button.setOnClickListener(new View.OnClickListener() {
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
                Log.d("debugPerso",""+notifSwitch.isChecked());
                setVisibiForHour();
                sharedpreferences.edit().putBoolean("notificationsPush",notifSwitch.isChecked()).apply();
            }
        });
    }

    public void initSlider() {
        boolean notifPush = sharedpreferences.getBoolean("notificationsPush", false);
        notifSwitch.setChecked(notifPush);
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
