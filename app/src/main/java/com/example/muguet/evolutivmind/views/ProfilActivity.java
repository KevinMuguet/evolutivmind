package com.example.muguet.evolutivmind.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    PieChartView pieChartView_c;
    PieChartView pieChartView_m;
    TextView experience;
    TextView niveau;
    TextView username;
    Button retour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        experience = findViewById(R.id.experience);
        niveau = findViewById(R.id.niveau);
        username = findViewById(R.id.username);
        retour = findViewById(R.id.retour);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

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

        //Colorwords
        if(db_loc.statistiqueDao().countStatistique(userId, "ColorWords") == 0) {
            pieChartView_c = findViewById(R.id.chart_colorwords);
            List pieDataC = new ArrayList<>();
            pieDataC.add(new SliceValue(0, Color.YELLOW).setLabel(0 + "V"));
            pieDataC.add(new SliceValue(0, Color.GRAY).setLabel(0 + "D"));
            PieChartData pieChartDataC = new PieChartData(pieDataC);
            pieChartDataC.setHasLabels(true).setValueLabelTextSize(14);
            pieChartDataC.setHasCenterCircle(true);
            pieChartView_c.setPieChartData(pieChartDataC);
        }else{
            Statistique statC = db_loc.statistiqueDao().findStatistiqueJeuForUser(userId, "ColorWords");
            int nbVictoireC = statC.getVictoires();
            int nbDefaitesC = statC.getDefaites();
            pieChartView_c = findViewById(R.id.chart_colorwords);
            List pieDataC = new ArrayList<>();
            pieDataC.add(new SliceValue(nbVictoireC, Color.YELLOW).setLabel(nbVictoireC + "V"));
            pieDataC.add(new SliceValue(nbDefaitesC, Color.GRAY).setLabel(nbDefaitesC + "D"));
            PieChartData pieChartDataC = new PieChartData(pieDataC);
            pieChartDataC.setHasLabels(true).setValueLabelTextSize(14);
            pieChartDataC.setHasCenterCircle(true);
            pieChartView_c.setPieChartData(pieChartDataC);
        }


        //Memorize
        if(db_loc.statistiqueDao().countStatistique(userId, "Memorize") == 0) {
            pieChartView_m = findViewById(R.id.chart_memorize);
            List pieDataM = new ArrayList<>();
            pieDataM.add(new SliceValue(0, Color.YELLOW).setLabel(0 + "V"));
            pieDataM.add(new SliceValue(0, Color.GRAY).setLabel(0 + "D"));
            PieChartData pieChartDataM = new PieChartData(pieDataM);
            pieChartDataM.setHasLabels(true).setValueLabelTextSize(14);
            pieChartDataM.setHasCenterCircle(true);
            pieChartView_m.setPieChartData(pieChartDataM);
        }else{
            Statistique statM = db_loc.statistiqueDao().findStatistiqueJeuForUser(userId, "Memorize");

            int nbVictoireM = statM.getVictoires();
            int nbDefaitesM = statM.getDefaites();
            pieChartView_m = findViewById(R.id.chart_memorize);
            List pieDataM = new ArrayList<>();
            pieDataM.add(new SliceValue(nbVictoireM, Color.YELLOW).setLabel(nbVictoireM + "V"));
            pieDataM.add(new SliceValue(nbDefaitesM, Color.GRAY).setLabel(nbDefaitesM + "D"));
            PieChartData pieChartDataM = new PieChartData(pieDataM);
            pieChartDataM.setHasLabels(true).setValueLabelTextSize(14);
            pieChartDataM.setHasCenterCircle(true);
            pieChartView_m.setPieChartData(pieChartDataM);
        }

    }
}
