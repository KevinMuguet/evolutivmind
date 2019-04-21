package com.example.muguet.evolutivmind.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.room.Room;
import com.example.muguet.evolutivmind.R;
import com.example.muguet.evolutivmind.ia.Regle;
import com.example.muguet.evolutivmind.models.AppDatabase;
import com.example.muguet.evolutivmind.models.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ColorWordsActivity extends AppCompatActivity {

    private int nb_victoire = 0;
    private int nb_defaite = 0;
    private List<String> list;
    private HashMap<String, Integer> listColor = new HashMap<>();
    private int correct_color;
    private int color2;
    private int color3;
    private Session session;
    private int variante;
    private boolean resPartiePrecedente;
    private boolean timerActif;
    private long timeleft;
    private long maxtime = 5000;
    private long tempsexposition;
    private int userId;

    private CountDownTimer new_ti;
    private CountDownTimer timerExpo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorwords);

        AppDatabase db_loc = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "evolutivmind").allowMainThreadQueries().build();

        SharedPreferences sharedpreferences = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        String loginFromSP = sharedpreferences.getString("login", null);

        userId = db_loc.profilDao().getProfil(loginFromSP).getId();

        ImageView rect = findViewById(R.id.rectangle);
        ImageView rect2 = findViewById(R.id.rectangle2);
        ImageView rect3 = findViewById(R.id.rectangle3);
        //Création d'un timer
        final TextView timer = findViewById(R.id.timer);

        listColor.put("Bleu", Color.BLUE);
        listColor.put("Rouge", Color.RED);
        listColor.put("Noir", Color.BLACK);
        listColor.put("Vert", Color.GREEN);
        listColor.put("Jaune", Color.YELLOW);
        listColor.put("Gris", Color.GRAY);

        list = new ArrayList<>(listColor.keySet());

        new_ti = new CountDownTimer(maxtime, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Temps restant: " + millisUntilFinished / 1000);
                timeleft = millisUntilFinished;
            }

            public void onFinish() {
                nb_defaite++;
                changeGame();
                resetTimer();
            }
        };
        new_ti.start();

        newGame();
        verif(rect, rect2, rect3);

    }

    /**
     * Fonction vérifiant le choix du joueur
     * @param rect
     * @param rect2
     * @param rect3
     */
    private void verif(final ImageView rect, final ImageView rect2, final ImageView rect3){

        (rect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerActif == true){
                    timerExpo.cancel();
                }
                resPartiePrecedente = true;
                Toast.makeText(ColorWordsActivity.this, "Correct", Toast.LENGTH_LONG).show();
                nb_victoire++;
                Log.d("victoires: ",""+nb_victoire);
                changeGame();
                resetTimer();
                //reduireTempsExposition();
            }
        });

        (rect2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerActif == true){
                    timerExpo.cancel();
                }
                resPartiePrecedente = false;
                Toast.makeText(ColorWordsActivity.this, "Incorrect", Toast.LENGTH_LONG).show();
                nb_defaite++;
                Log.d("defaites: ",""+nb_defaite);
                changeGame();
                resetTimer();
                //reduireTempsExposition();
            }
        });

        (rect3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerActif == true){
                    timerExpo.cancel();
                }
                resPartiePrecedente = false;
                Toast.makeText(ColorWordsActivity.this, "Incorrect", Toast.LENGTH_LONG).show();
                nb_defaite++;
                Log.d("defaites: ",""+nb_defaite);
                changeGame();
                resetTimer();
                //reduireTempsExposition();
            }
        });
    }

    /**
     * Fonction retournant une couleur aléatoire
     * @return
     */
    private int setRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256));
    }

    /**
     * Fonction retournant un string de couleur aléatoire
     * @return
     */
    private String setRandomColorString(){
        Random random = new Random();
        String randomColor = list.get(random.nextInt(list.size()));
        return randomColor;
    }

    /**
     * Fonction changeant aléatoirement la position des trois rectangles
     * @param rect
     * @param rect2
     * @param rect3
     */
    private void switchPosition(ImageView rect,ImageView rect2,ImageView rect3){

        float posXimg = rect.getX();
        float posYimg = rect.getY();

        float posXimg2 = rect2.getX();
        float posYimg2 = rect2.getY();

        float posXimg3 = rect3.getX();
        float posYimg3 = rect3.getY();

        int nb = new Random().nextInt(5);

        switch (nb){
            case 1:
                rect2.setX(posXimg3);
                rect2.setY(posYimg3);
                rect3.setX(posXimg2);
                rect3.setY(posYimg2);
                break;
            case 2:
                rect2.setX(posXimg);
                rect2.setY(posYimg);
                rect3.setX(posXimg2);
                rect3.setY(posYimg2);
                rect.setX(posXimg3);
                rect.setY(posYimg3);
                break;
            case 3:
                rect2.setX(posXimg);
                rect2.setY(posYimg);
                rect.setX(posXimg2);
                rect.setY(posYimg2);
                break;
            case 4:
                rect3.setX(posXimg);
                rect3.setY(posYimg);
                rect.setX(posXimg2);
                rect.setY(posYimg2);
                rect2.setX(posXimg3);
                rect2.setY(posYimg3);
                break;
            case 5:
                rect3.setX(posXimg);
                rect3.setY(posYimg);
                rect.setX(posXimg3);
                rect.setY(posYimg3);
                break;
            case 0:
                break;
        }
    }

    /**
     * Fonction démarrant la première variante du jeu (couleur mot)
     */
    private void newGame(){

        variante = 1;

        TextView mot = findViewById(R.id.mot);
        TextView question = findViewById(R.id.question);
        TextView reponse = findViewById(R.id.reponse);
        ImageView rect = findViewById(R.id.rectangle);
        ImageView rect2 = findViewById(R.id.rectangle2);
        ImageView rect3 = findViewById(R.id.rectangle3);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.rectangle);
        Drawable drawable2 = res.getDrawable(R.drawable.rectangle2);
        Drawable drawable3 = res.getDrawable(R.drawable.rectangle3);

        mot.setVisibility(View.VISIBLE);
        correct_color = setRandomColor();
        color2 = setRandomColor();
        color3 = setRandomColor();

        question.setText("De quelle couleur le mot est-il écrit?");
        mot.setTextColor(correct_color);
        mot.setText(setRandomColorString());

        reponse.setText("");

        drawable.setColorFilter(correct_color, PorterDuff.Mode.SRC_ATOP);
        rect.setBackground(drawable);

        drawable2.setColorFilter(color2, PorterDuff.Mode.SRC_ATOP);
        rect2.setBackground(drawable2);

        drawable3.setColorFilter(color3, PorterDuff.Mode.SRC_ATOP);
        rect3.setBackground(drawable3);

        switchPosition(rect, rect2, rect3);

    }

    /**
     * Fonction démarrant la seconde variante du jeu (couleur représentée par mot)
     */
    private void newGame2(){

        variante = 2;

        TextView mot = findViewById(R.id.mot);
        TextView question = findViewById(R.id.question);
        TextView reponse = findViewById(R.id.reponse);
        ImageView rect = findViewById(R.id.rectangle);
        ImageView rect2 = findViewById(R.id.rectangle2);
        ImageView rect3 = findViewById(R.id.rectangle3);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.rectangle);
        Drawable drawable2 = res.getDrawable(R.drawable.rectangle2);
        Drawable drawable3 = res.getDrawable(R.drawable.rectangle3);
        String rndColorString = setRandomColorString();

        mot.setVisibility(View.VISIBLE);
        correct_color = listColor.get(rndColorString);
        color2 = setRandomColor();
        color3 = setRandomColor();

        question.setText("Quelle est la couleur représentée par le mot écrit?");
        mot.setTextColor(color2);
        mot.setText(rndColorString);

        reponse.setText("");

        drawable.setColorFilter(correct_color, PorterDuff.Mode.SRC_ATOP);
        rect.setBackground(drawable);

        drawable2.setColorFilter(color2, PorterDuff.Mode.SRC_ATOP);
        rect2.setBackground(drawable2);

        drawable3.setColorFilter(color3, PorterDuff.Mode.SRC_ATOP);
        rect3.setBackground(drawable3);

        switchPosition(rect, rect2, rect3);

    }

    /**
     * Fonction choisissant aléatoirement le jeu
     */
    private void changeGame(){
        Random rnd = new Random();
        int game = rnd.nextInt(2);
        if(game == 1){
            newGame();
        }else{
            newGame2();
        }
    }

    private void changeTimer(boolean increase){
        //Création d'un timer
        final TextView timer = findViewById(R.id.timer);

        new_ti.cancel();
        if(increase == true) {
            maxtime += 1000;
        }else{
            maxtime = maxtime - 1000;
        }

        new_ti = new CountDownTimer(maxtime, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Temps restant: " + millisUntilFinished / 1000);
                timeleft = millisUntilFinished;
            }

            public void onFinish() {
                nb_defaite++;
                changeGame();
                resetTimer();
            }
        };
        new_ti.start();
    }

    private void resetTimer(){
        //Création d'un timer
        final TextView timer = findViewById(R.id.timer);

        new_ti.cancel();
        new_ti = new CountDownTimer(maxtime, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Temps restant: " + millisUntilFinished / 1000);
                timeleft = millisUntilFinished;
            }

            public void onFinish() {
                nb_defaite++;
                changeGame();
                resetTimer();
            }
        };
        new_ti.start();
    }

    private void reduireTempsExposition(){

        tempsexposition = 2000;
        timerActif = true;
        timerExpo = new CountDownTimer(tempsexposition, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                TextView mot = findViewById(R.id.mot);
                mot.setVisibility(View.GONE);
                timerActif = false;
            }
        };
        timerExpo.start();
    }

    private void jouerRegle(Regle regle){

        switch(regle.getAction()){
            case "Augmentation du temps consacré au timer":
                changeTimer(true);
                break;
            case "Diminution du temps consacré au timer":
                changeTimer(false);
                reduireTempsExposition();
                break;
            default:
                break;
        }
    }

    private void creerRegle(){
        Regle regle = new Regle(userId,
                1,
                timeleft,
                variante,
                resPartiePrecedente,
                5,
                "Test");
    }

    @Override
    public void onBackPressed() {
        final TextView timer = findViewById(R.id.timer);
        new_ti.cancel();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Quitter le jeu?")
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ColorWordsActivity.this.finish();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new_ti = new CountDownTimer(timeleft, 1000) {

                            public void onTick(long millisUntilFinished) {
                                timer.setText("Temps restant: " + millisUntilFinished / 1000);
                                timeleft = millisUntilFinished;
                            }

                            public void onFinish() {
                                nb_defaite++;
                                changeGame();
                                resetTimer();
                            }
                        };
                        new_ti.start();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

//    /**
//     * Fonction sauvegardant les victoires et défaites du joueur
//     */
//    @Override
//    public void onBackPressed() {
//        AppDatabase db_loc = Room.databaseBuilder(getApplicationContext(),
//                AppDatabase.class, "evolutivmind").allowMainThreadQueries().build();
//
//        //S'il n'y pas de statistique pour ce joueur, on la crée
//        if(db_loc.statistiqueDao().countStatistique(session.getUserId(), "ColorWords") == 0) {
//            Statistique statistique_colorwords = new Statistique();
//            statistique_colorwords.setVictoires(nb_victoire);
//            statistique_colorwords.setDefaites(nb_defaite);
//            statistique_colorwords.setJeu("ColorWords");
//            statistique_colorwords.setUserId(session.getUserId());
//            db_loc.statistiqueDao().insert(statistique_colorwords);
//        }else{
//            //S'il y a déjà des statistiques pour ce joueur, on la modifie
//            Statistique statistique_joueur = db_loc.statistiqueDao().findStatistiqueJeuForUser(session.getUserId(), "ColorWords");
//            statistique_joueur.setVictoires(statistique_joueur.getVictoires()+nb_victoire);
//            statistique_joueur.setDefaites(statistique_joueur.getDefaites()+nb_defaite);
//            db_loc.statistiqueDao().update(statistique_joueur);
//        }
//        finish();
//        return;
//    }
}
