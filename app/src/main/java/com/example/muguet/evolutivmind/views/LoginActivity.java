package com.example.muguet.evolutivmind.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.room.Room;
import com.example.muguet.evolutivmind.R;
import com.example.muguet.evolutivmind.models.AppDatabase;
import com.example.muguet.evolutivmind.models.Profil;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = findViewById(R.id.btn_login);

        final EditText login = findViewById(R.id.login);
        final EditText age = findViewById(R.id.age);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppDatabase db_loc = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "evolutivmind").allowMainThreadQueries().build();

                editor.putString("login", login.getText().toString());
                editor.putString("age", age.getText().toString());
                Log.d("debug", login.getText().toString());

                editor.apply();

                if(db_loc.profilDao().getProfil(login.getText().toString()) == null){
                    Profil nouveauProfil = new Profil();
                    nouveauProfil.setExperience(0);
                    nouveauProfil.setNiveau(1);
                    nouveauProfil.setNom(login.getText().toString());
                    nouveauProfil.setAge(Integer.parseInt(age.getText().toString()));
                    db_loc.profilDao().insert(nouveauProfil);
                }

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
