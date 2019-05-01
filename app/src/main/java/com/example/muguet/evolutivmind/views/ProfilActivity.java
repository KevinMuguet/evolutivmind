package com.example.muguet.evolutivmind.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.room.Room;
import com.airbnb.lottie.LottieAnimationView;
import com.example.muguet.evolutivmind.R;
import com.example.muguet.evolutivmind.models.AppDatabase;
import com.example.muguet.evolutivmind.models.Statistique;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

import java.util.ArrayList;
import java.util.List;

public class ProfilActivity extends AppCompatActivity {

    PieChartView pieChartView;
    TextView experience;
    TextView niveau;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        experience = findViewById(R.id.experience);
        niveau = findViewById(R.id.niveau);
        username = findViewById(R.id.username);

        SharedPreferences sharedpreferences = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        String loginFromSP = sharedpreferences.getString("login", null);
        username.setText(loginFromSP);

        AppDatabase db_loc = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "evolutivmind").allowMainThreadQueries().build();
        int userId = db_loc.profilDao().getProfil(loginFromSP).getId();
        String playerExp = String.valueOf(db_loc.profilDao().getProfilById(userId).getExperience());
        String playerLevel = String.valueOf(db_loc.profilDao().getProfilById(userId).getNiveau());

        experience.setText(playerExp);
        niveau.setText(playerLevel);
        Statistique stat = db_loc.statistiqueDao().findStatistiqueJeuForUser(userId, "ColorWords");
        int nbVictoire = stat.getVictoires();
        int nbDefaites = stat.getDefaites();

        pieChartView = findViewById(R.id.chart);

        List pieData = new ArrayList<>();

        pieData.add(new SliceValue(nbVictoire, Color.YELLOW).setLabel(nbVictoire+"V"));
        pieData.add(new SliceValue(nbDefaites, Color.GRAY).setLabel(nbDefaites+"D"));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true).setCenterText1("ColorWords").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#130e07"));
        pieChartView.setPieChartData(pieChartData);
    }
}
