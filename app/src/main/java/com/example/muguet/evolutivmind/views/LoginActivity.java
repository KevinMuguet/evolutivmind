package com.example.muguet.evolutivmind.views;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.example.muguet.evolutivmind.R;
import com.example.muguet.evolutivmind.models.AppDatabase;
import com.example.muguet.evolutivmind.models.Profil;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.username) EditText _username;
    @InjectView(R.id.age) EditText _age;
    @InjectView(R.id.btn_login) Button _loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Connexion...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        String username = _username.getText().toString();
        String age = _age.getText().toString();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();


        AppDatabase db_loc = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "evolutivmind").allowMainThreadQueries().build();

        editor.putString("login", username);
        editor.putString("age", age);

        editor.apply();

        Profil nouveauProfil = new Profil();
        nouveauProfil.setExperience(0);
        nouveauProfil.setNiveau(1);
        nouveauProfil.setNom(username);
        nouveauProfil.setAge(Integer.parseInt(age));
        db_loc.profilDao().insert(nouveauProfil);

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _username.getText().toString();
        String age = _age.getText().toString();

        /**
        if (username.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            _username.setError("enter a valid email address");
            valid = false;
        } else {
            _username.setError(null);
        }

        if (age.isEmpty() || age.length() < 4 || age.length() > 10) {
            _age.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _age.setError(null);
        }
         */

        return valid;
    }
}
