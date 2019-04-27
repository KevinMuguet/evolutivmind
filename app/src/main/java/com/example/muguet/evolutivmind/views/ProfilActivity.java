package com.example.muguet.evolutivmind.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.room.Room;
import com.airbnb.lottie.LottieAnimationView;
import com.example.muguet.evolutivmind.R;
import com.example.muguet.evolutivmind.models.AppDatabase;
import com.example.muguet.evolutivmind.models.Statistique;

public class ProfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        SharedPreferences sharedpreferences = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        String loginFromSP = sharedpreferences.getString("login", null);

        AppDatabase db_loc = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "evolutivmind").allowMainThreadQueries().build();
        int userId = db_loc.profilDao().getProfil(loginFromSP).getId();
        Statistique stat = db_loc.statistiqueDao().findStatistiqueJeuForUser(userId, "ColorWords");

        TextView nbVictoire = findViewById(R.id.nbVictoire);
        nbVictoire.setText(String.valueOf(stat.getVictoires()));

    }
}
