package com.example.muguet.evolutivmind.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.muguet.evolutivmind.R;

public class GamesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        ImageView b_colorwords = findViewById(R.id.colorwords);
        ImageView b_memorize = findViewById(R.id.memorize);
        b_colorwords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ColorWordsActivity.class);
                startActivity(intent);
            }
        });
        b_memorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MemorizeActivity.class);
                startActivity(intent);
            }
        });
    }

}
