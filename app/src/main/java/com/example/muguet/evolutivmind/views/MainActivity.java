package com.example.muguet.evolutivmind.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
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

        Button b_play = findViewById(R.id.btnJouer);
        b_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence text = "Veuillez sélectionnez un jeu";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Intent intent = new Intent(getBaseContext(), GamesActivity.class);
                startActivity(intent);
            }
        });

        // Bouton qui permet d'accéder aux paramètres de l'application
        Button b_options = findViewById(R.id.btnOption);
        b_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), OptionsActivity.class);
                startActivity(intent);
            }
        });

        // Bouton qui permet d'accéder au profil
        Button b_profil = findViewById(R.id.btnProfil);
        b_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ProfilActivity.class);
                startActivity(intent);
            }
        });


        // Bouton de déconnexion
        Button b_quitter = findViewById(R.id.btnQuitter);
        b_quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
