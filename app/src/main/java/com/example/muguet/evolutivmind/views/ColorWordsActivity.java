package com.example.muguet.evolutivmind.views;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.room.Room;
import com.airbnb.lottie.LottieAnimationView;
import com.example.muguet.evolutivmind.R;
import com.example.muguet.evolutivmind.ia.AlgoGen;
import com.example.muguet.evolutivmind.ia.Regle;
import com.example.muguet.evolutivmind.models.AppDatabase;
import com.example.muguet.evolutivmind.models.Profil;
import com.example.muguet.evolutivmind.models.Statistique;
import pl.droidsonroids.gif.GifImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ColorWordsActivity extends AppCompatActivity {

    private int nb_victoire = 0;
    private int nb_defaite = 0;

    private List<String> list;
    private List<Regle> reglesValide;
    private List<Regle> listRegle;
    private String regleJoue;

    private HashMap<String, Integer> listColor = new HashMap<>();
    private int correct_color;
    private GifImageView levelUpAnim;
    private LottieAnimationView gameAnimationView;
    private int color2;
    private int color3;

    private int variante;
    private boolean resPartiePrecedente;
    private long timeleft;

    private boolean timerActif;
    private int experienceGagne = 0;
    private int levelUp = 0;
    private long maxtime = 10000;
    private long tempsexposition;
    private int userId;

    //Variables pour stocker les informations des conditions des règles
    private int userIdR;
    private int idJeuR;
    private float timeleftR;
    private int varianteR;
    private int maxPoids;
    private boolean resPartiePrecedenteR;

    private boolean isReady = false;

    private CountDownTimer new_ti;
    private CountDownTimer timerExpo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorwords);
        TextView regleText = findViewById(R.id.regle);

        levelUpAnim = findViewById(R.id.levelup);

        resPartiePrecedente = true;
        SharedPreferences sharedpreferences = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        String loginFromSP = sharedpreferences.getString("login", null);

        AppDatabase db_loc = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "evolutivmind").allowMainThreadQueries().build();
        userId = db_loc.profilDao().getProfil(loginFromSP).getId();

        ImageView rect = findViewById(R.id.rectangle);
        ImageView rect2 = findViewById(R.id.rectangle2);
        ImageView rect3 = findViewById(R.id.rectangle3);
        //Création d'un timer
        final TextView timer = findViewById(R.id.timer);

        gameAnimationView = findViewById(R.id.animation_view_game);
        gameAnimationView.setVisibility(View.INVISIBLE);

        listColor.put("Bleu", Color.BLUE);
        listColor.put("Rouge", Color.RED);
        listColor.put("Noir", Color.BLACK);
        listColor.put("Vert", Color.GREEN);
        listColor.put("Jaune", Color.YELLOW);
        listColor.put("Gris", Color.GRAY);

        list = new ArrayList<>(listColor.keySet());
        reglesValide = new ArrayList<>();

        new_ti = new CountDownTimer(maxtime, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Temps restant: " + millisUntilFinished / 1000);
                timeleft = millisUntilFinished / 1000;
            }

            public void onFinish() {
                nb_defaite++;
                lottieDisplay(gameAnimationView, R.raw.unapproved_cross);
            }
        };
        new_ti.start();

        gameAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                new_ti.cancel();
                Log.d("tick","canceled");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        gameAnimationView.setVisibility(View.INVISIBLE);
                        resetGame();
                    }
                }, 500);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                gameAnimationView.pauseAnimation();
            }
        });

        isReady = true;
        newGame();
        verif(rect, rect2, rect3);

    }

    private void lottieDisplay(LottieAnimationView _loLottieAnimationView, int _rawRes) {
        _loLottieAnimationView.setVisibility(View.VISIBLE);
        _loLottieAnimationView.setAnimation(_rawRes);
        _loLottieAnimationView.playAnimation();
    }

    private void resetGame() {
        changeGame();
        resetTimer();
    }

    /**
     * Fonction vérifiant le choix du joueur
     * @param rect
     * @param rect2
     * @param rect3
     */
    private void verif(final ImageView rect, final ImageView rect2, final ImageView rect3){

        final LottieAnimationView gameAnimationView = findViewById(R.id.animation_view_game);
        final TextView regleText = findViewById(R.id.regle);

        (rect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isReady) {
                    isReady = false;
                    if (timerActif == true) {
                        timerExpo.cancel();
                    }
                    nb_victoire++;
                    if (experienceGagne >= 100) {
                        levelUpAnim.setVisibility(View.VISIBLE);
                        levelUpAnim.setBackgroundResource(R.drawable.levelup);
                        experienceGagne = 0;
                        levelUp += 1;
                    } else {
                        if (variante == 1) {
                            experienceGagne += 10;
                        } else {
                            experienceGagne += 20;
                        }
                    }
                    Log.d("victoires: ", "" + nb_victoire);
                    lottieDisplay(gameAnimationView, R.raw.check_orange);
                    Log.d("lottie", "lottieDisplayed");
                    verificationRegle();
                    resPartiePrecedente = true;
                }
            }
        });

        (rect2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isReady) {
                    isReady = false;
                    if (timerActif == true) {
                        timerExpo.cancel();
                    }
                    nb_defaite++;
                    Log.d("defaites: ", "" + nb_defaite);
                    lottieDisplay(gameAnimationView, R.raw.unapproved_cross);
                    Log.d("lottie", "lottieDisplayed");
                    verificationRegle();
                    resPartiePrecedente = false;
                }
            }
        });

        (rect3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isReady) {
                    isReady = false;
                    if (timerActif == true) {
                        timerExpo.cancel();
                    }
                    nb_defaite++;
                    Log.d("defaites: ", "" + nb_defaite);
                    lottieDisplay(gameAnimationView, R.raw.unapproved_cross);
                    Log.d("lottie", "lottieDisplayed");
                    verificationRegle();
                    resPartiePrecedente = false;
                }
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

        int nb = new Random().nextInt(6);

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
        levelUpAnim.setVisibility(View.INVISIBLE);
        isReady = true;
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
                timeleft = millisUntilFinished/1000;
            }

            public void onFinish() {
                nb_defaite++;
                lottieDisplay(gameAnimationView, R.raw.unapproved_cross);
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
                timeleft = millisUntilFinished/1000;
            }

            public void onFinish() {
                nb_defaite++;
                lottieDisplay(gameAnimationView, R.raw.unapproved_cross);
            }
        };
        new_ti.start();
    }

    private void reduireTempsExposition(){

        tempsexposition = 5000;
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

    private void jouerRegle(final Regle regle){

        final Handler handler = new Handler();
        final TextView regleText = findViewById(R.id.regle);

        switch(regle.getAction()){
            case "Augmentation du temps consacré au timer":
                changeTimer(true);
                regleJoue = "Timer augmenté!";
                regleText.setText(regleJoue);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        regleText.setText("");
                    }
                }, 1000);
                break;
            case "Diminution du temps consacré au timer":
                changeTimer(false);
                regleJoue = "Timer diminué!";
                regleText.setText(regleJoue);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        regleText.setText("");
                    }
                }, 1000);
                break;
            case "Réduire le temps d'exposition du mot":
                reduireTempsExposition();
                regleJoue = "Temps d'exposition du mot diminué!";
                regleText.setText(regleJoue);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        regleText.setText("");
                    }
                }, 1000);
                break;
            default:
                break;
        }
        Log.d("Regle joue: ",""+regle.getAction());
    }

    private void creerRegle(){
        AppDatabase db_loc = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "evolutivmind").allowMainThreadQueries().build();

        Regle regle = new Regle(userId,
                1,
                (int)timeleft,
                variante,
                resPartiePrecedente,
                5,
                "Augmentation du temps consacré au timer");

        Regle regle2 = new Regle(userId,
                1,
                (int)timeleft,
                variante,
                resPartiePrecedente,
                5,
                "Diminution du temps consacré au timer");

        Regle regle3 = new Regle(userId,
                1,
                (int)timeleft,
                variante,
                resPartiePrecedente,
                5,
                "Réduire le temps d'exposition du mot");


        db_loc.regleDao().insert(AlgoGen.mutationRegle(regle));
        db_loc.regleDao().insert(regle);
        db_loc.regleDao().insert(regle2);
        db_loc.regleDao().insert(regle3);
    }

    private void verificationRegle(){
        maxPoids = 0;
        AppDatabase db_loc = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "evolutivmind").allowMainThreadQueries().build();
        listRegle = db_loc.regleDao().getAllRegle();
        for(int i = 0; i < listRegle.size(); i++){
            userIdR = listRegle.get(i).idJoueur;
            idJeuR = listRegle.get(i).idJeu;
            timeleftR = listRegle.get(i).timeRestant;
            varianteR = listRegle.get(i).varianteJeu;
            resPartiePrecedenteR = listRegle.get(i).resPartiePrecedente;
            if(userId == userIdR && idJeuR == 1 && timeleft == timeleftR && variante == varianteR && resPartiePrecedente == resPartiePrecedenteR){
                reglesValide.add(listRegle.get(i));
            }
        }
        if(reglesValide.size() != 0){
            if(reglesValide.size() == 1){
                jouerRegle(reglesValide.get(0));
            }else{
                //Obtenir le poids le plus élevé
                for(int i = 0; i < reglesValide.size(); i++){
                    if(reglesValide.get(i).poids > maxPoids){
                        maxPoids =reglesValide.get(i).poids;
                    }
                }
                //Supprimer les règles ayant un poids faible
                for(int i = 0; i < reglesValide.size(); i++){
                    if(reglesValide.get(i).poids != maxPoids){
                        reglesValide.remove(i);
                    }
                }
                //Jouer une regle aléatoire parmi les restantes
                Random rnd = new Random();
                int posRegle = rnd.nextInt(reglesValide.size());
                jouerRegle(reglesValide.get(posRegle));
            }
            Log.d("Passe","");
        }else{
            Log.d("Passe2","");
            creerRegle();
        }
        if(reglesValide.size() != 0){
            reglesValide.clear();
        }
        listRegle.clear();
    }

    public String randomAction(){
        String action = "";
        Random random = new Random();
        int res = random.nextInt(3);
        if(res == 0){
            action = "Augmentation du temps consacré au timer";
        }
        if (res == 1){
            action = "Diminution du temps consacré au timer";
        }
        if (res == 2){
            action = "Réduire le temps d'exposition du mot";
        }
        return action;
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
                        AppDatabase db_loc = Room.databaseBuilder(getApplicationContext(),
                                AppDatabase.class, "evolutivmind").allowMainThreadQueries().build();

                        //S'il n'y pas de statistique pour ce joueur, on la crée
                        if(db_loc.statistiqueDao().countStatistique(userId, "ColorWords") == 0) {
                            Statistique statistiqueColorwords = new Statistique();
                            statistiqueColorwords.setVictoires(nb_victoire);
                            statistiqueColorwords.setDefaites(nb_defaite);
                            statistiqueColorwords.setJeu("ColorWords");
                            statistiqueColorwords.setUserId(userId);
                            db_loc.statistiqueDao().insert(statistiqueColorwords);
                        }else{
                            //S'il y a déjà des statistiques pour ce joueur, on la met à jour
                            Statistique statistiqueJoueur = db_loc.statistiqueDao().findStatistiqueJeuForUser(userId, "ColorWords");
                            statistiqueJoueur.setVictoires(statistiqueJoueur.getVictoires()+nb_victoire);
                            statistiqueJoueur.setDefaites(statistiqueJoueur.getDefaites()+nb_defaite);
                            db_loc.statistiqueDao().update(statistiqueJoueur);
                        }
                        //On met à jour le profil du joueur
                        Profil joueur = db_loc.profilDao().getProfilById(userId);
                        joueur.setExperience(joueur.getExperience()+experienceGagne);
                        joueur.setNiveau(joueur.getNiveau()+levelUp);
                        db_loc.profilDao().update(joueur);
                        ColorWordsActivity.this.finish();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new_ti = new CountDownTimer(timeleft*1000+1000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                timer.setText("Temps restant: " + millisUntilFinished / 1000);
                                timeleft = millisUntilFinished / 1000;
                            }

                            public void onFinish() {
                                nb_defaite++;
                                lottieDisplay(gameAnimationView, R.raw.unapproved_cross);
                            }
                        };
                        new_ti.start();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
