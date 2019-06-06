package com.example.muguet.evolutivmind.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.airbnb.lottie.LottieAnimationView;
import com.example.muguet.evolutivmind.R;
import com.example.muguet.evolutivmind.models.AppDatabase;
import com.example.muguet.evolutivmind.models.Statistique;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView fb = findViewById(R.id.fb);
        ImageView tw = findViewById(R.id.twitter);
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
        shake.setFillAfter(true);
        fb.startAnimation(shake);
        tw.startAnimation(shake);

        if(isNetworkAvailable()){
            AppDatabase db_loc = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "evolutivmind").allowMainThreadQueries().build();
            SharedPreferences sharedpreferences = getSharedPreferences("MyPref",
                    Context.MODE_PRIVATE);
            String loginFromSP = sharedpreferences.getString("login", null);
            int userId = db_loc.profilDao().getProfil(loginFromSP).getId();
            FirebaseApp.initializeApp(this);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("profil").document(String.valueOf(db_loc.profilDao().getProfil(loginFromSP).id))
                    .set(db_loc.profilDao().getProfil(loginFromSP)).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

            List<Statistique> statsJoueur = db_loc.statistiqueDao().findStatistiqueForUser(userId);
            for(int i = 0; i < statsJoueur.size(); i++){
                db.collection("statistique").document(String.valueOf(statsJoueur.get(i).id))
                        .set(statsJoueur.get(i)).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        }

        ImageView b_play = findViewById(R.id.btnJouer);
        b_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), GamesActivity.class);
                startActivity(intent);
            }
        });

        // Bouton qui permet d'accéder aux paramètres de l'application
        ImageView b_options = findViewById(R.id.btnOption);
        b_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), OptionsActivity.class);
                startActivity(intent);
            }
        });

        // Bouton qui permet d'accéder au profil
        ImageView b_profil = findViewById(R.id.btnProfil);
        b_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ProfilActivity.class);
                startActivity(intent);
            }
        });


        // Bouton de déconnexion
        ImageView b_quitter = findViewById(R.id.btnQuitter);
        b_quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        // Bouton FB
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.facebook.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        // Bouton Twitter
        tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.twitter.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void lottieDisplay(LottieAnimationView _loLottieAnimationView, int _rawRes) {
        _loLottieAnimationView.setVisibility(View.VISIBLE);
        _loLottieAnimationView.setAnimation(_rawRes);
        _loLottieAnimationView.playAnimation();
    }

}
