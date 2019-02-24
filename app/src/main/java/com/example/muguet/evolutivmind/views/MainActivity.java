package com.example.muguet.evolutivmind.views;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.room.Room;
import com.example.muguet.evolutivmind.R;
import com.example.muguet.evolutivmind.models.AppDatabase;
import com.example.muguet.evolutivmind.models.Profil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialisation de la bdd local
        AppDatabase db_loc = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "evolutivmind").allowMainThreadQueries().build();

        //Création d'un nouveau profil
        Profil test = new Profil();
        test.setExperience(10);
        test.setNiveau(5);
        test.setNom("toto");

        //Insertion d'un nouveau profil
        db_loc.profilDao().insert(test);

        //Exemple récupération de tous les profils de firestore
        db.collection("profil")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("trouve", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("non trouve","");
                        }
                    }
                });

        //Exemple ajout d'un nouveau profil dans firestore à partir de la bdd locale avec id locale = id firestore
        List<Profil> list = db_loc.profilDao().getAllProfil();
        db.collection("profil").document(String.valueOf(list.get(0).id)).set(list.get(0));

        //Exemple récupération de tous les statistiques d'un joueur en particulier
        db.collection("statistique")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("trouve","sdsq"+document.get("userId"));
                                if((Long)document.get("userId") == 1){
                                    Log.d("trouve","sdsq"+document.getData());
                                }
                            }
                        } else {
                            Log.d("non trouve","");
                        }
                    }
                });
    }
}
