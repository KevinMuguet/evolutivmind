package com.example.muguet.evolutivmind.views;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.airbnb.lottie.LottieAnimationView;
import com.example.muguet.evolutivmind.R;
import android.util.Log;

public class OptionsActivity extends AppCompatActivity {

    Button retour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Button button = findViewById(R.id.btn_logout);

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
    }
}
