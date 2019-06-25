package com.example.muguet.evolutivmind.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.example.muguet.evolutivmind.models.Profil;
import com.example.muguet.evolutivmind.models.Statistique;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.*;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean recup = false;
        ImageView fb = findViewById(R.id.fb);
        ImageView tw = findViewById(R.id.twitter);
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
        shake.setFillAfter(true);
        fb.startAnimation(shake);
        tw.startAnimation(shake);

        if(isNetworkAvailable()){
            final AppDatabase db_loc = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "evolutivmind").allowMainThreadQueries().build();
            SharedPreferences sharedpreferences = getSharedPreferences("MyPref",
                    Context.MODE_PRIVATE);
            final String loginFromSP = sharedpreferences.getString("login", null);
            final String age = sharedpreferences.getString("age", null);
            final int[] userId = {db_loc.profilDao().getProfil(loginFromSP).getId()};
            FirebaseApp.initializeApp(this);
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            //Sauvegarde sur firestore
            db.collection("profil").document(String.valueOf(db_loc.profilDao().getProfil(loginFromSP).id))
                    .set(db_loc.profilDao().getProfil(loginFromSP)).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

            List<Statistique> statsJoueur = db_loc.statistiqueDao().findStatistiqueForUser(userId[0]);
            for(int i = 0; i < statsJoueur.size(); i++){
                db.collection("statistique").document(String.valueOf(statsJoueur.get(i).id))
                        .set(statsJoueur.get(i)).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }

            //Récupération compte à partir de firestore
            if(db_loc.profilDao().getProfil(loginFromSP) == null && recup == true) {
                CollectionReference profil = db.collection("profil");
                Query query = profil.whereEqualTo("nom", loginFromSP).whereEqualTo("age", Integer.parseInt(age));
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Profil profil = new Profil();
                            profil.setNom(loginFromSP);
                            profil.setAge(Integer.parseInt(age));
                            profil.setNiveau(Integer.parseInt(document.get("niveau").toString()));
                            profil.setExperience(Integer.parseInt(document.get("experience").toString()));
                            db_loc.profilDao().insert(profil);

                            final String id = document.get("id").toString();
                            CollectionReference stats = db.collection("statistique");
                            Query query = stats.whereEqualTo("userId", Integer.parseInt(id));
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.get("jeu").toString() == "ColorWords") {
                                            Statistique cw = new Statistique();
                                            cw.setJeu("ColorWords");
                                            cw.setVictoires(Integer.parseInt(document.get("victoires").toString()));
                                            cw.setDefaites(Integer.parseInt(document.get("defaites").toString()));
                                            cw.setUserId(db_loc.profilDao().getProfil(loginFromSP).id);
                                            db_loc.statistiqueDao().insert(cw);
                                        }
                                        if (document.get("jeu").toString() == "Memorize") {
                                            Statistique m = new Statistique();
                                            m.setJeu("Memorize");
                                            m.setVictoires(Integer.parseInt(document.get("victoires").toString()));
                                            m.setDefaites(Integer.parseInt(document.get("defaites").toString()));
                                            m.setUserId(db_loc.profilDao().getProfil(loginFromSP).id);
                                            db_loc.statistiqueDao().insert(m);
                                        }
                                    }
                                }
                            });
                        }
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
