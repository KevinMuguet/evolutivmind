package com.example.muguet.evolutivmind.views;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.room.Room;
import com.airbnb.lottie.LottieAnimationView;
import com.example.muguet.evolutivmind.ia.AlgoGen;
import com.example.muguet.evolutivmind.ia.Regle;
import com.example.muguet.evolutivmind.models.AppDatabase;
import com.example.muguet.evolutivmind.models.Profil;
import com.example.muguet.evolutivmind.models.Statistique;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import com.example.muguet.evolutivmind.R;
import pl.droidsonroids.gif.GifImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MemorizeActivity extends AppCompatActivity {

    private Animation rotation;

    private int nb_victoire = 0;
    private int nb_defaite = 0;
    private int pos;

    private List<String> list;
    private List<Regle> reglesValide;
    private List<Regle> listRegle;
    private String regleJoue;

    private HashMap<String, Integer> listDrawable = new HashMap<>();
    private ArrayList<Integer> listFigure = new ArrayList<Integer>();
    private int correct_figure;
    private GifImageView levelUpAnim;
    private LottieAnimationView gameAnimationView;
    private int figure2;
    private int figure3;
    private boolean timerExpoActif = true;

    private int variante;
    private boolean resPartiePrecedente;
    private long timeleft;
    private long timeExpoLeft;

    private boolean timerActif;
    private int experienceGagne = 0;
    private int levelUp = 0;
    private long maxtime = 10000;
    private long tempsexposition = 5000;
    private int userId;

    private int correct_pos;

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
        setContentView(R.layout.activity_memorize);

        levelUpAnim = findViewById(R.id.levelup);

        resPartiePrecedente = true;
        SharedPreferences sharedpreferences = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        String loginFromSP = sharedpreferences.getString("login", null);

        AppDatabase db_loc = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "evolutivmind").allowMainThreadQueries().build();
        userId = db_loc.profilDao().getProfil(loginFromSP).getId();

        ImageView rep1 = findViewById(R.id.reponse1);
        ImageView rep2 = findViewById(R.id.reponse2);
        ImageView rep3 = findViewById(R.id.reponse3);
        //Création d'un timer
        final TextView timer = findViewById(R.id.timer);

        gameAnimationView = findViewById(R.id.animation_view_game);
        gameAnimationView.setVisibility(View.INVISIBLE);

        listDrawable.put("Etoile", R.drawable.star);
        listDrawable.put("Triangle", R.drawable.triangle);
        listDrawable.put("Rectangle", R.drawable.rectangle);

        list = new ArrayList<>(listDrawable.keySet());
        reglesValide = new ArrayList<>();

        gameAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                new_ti.cancel();
                Log.d("tick","canceled");
//                Log.d("debugPerso","start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                Log.d("debugPerso","end");
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
//                Log.d("debugPerso","cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                gameAnimationView.pauseAnimation();
//                Log.d("debugPerso","repeat");
            }
        });

        isReady = true;
        newGame();
        verif(rep1, rep2, rep3);
    }

    private void lottieDisplay(LottieAnimationView _loLottieAnimationView, int _rawRes) {
        _loLottieAnimationView.setVisibility(View.VISIBLE);
        _loLottieAnimationView.setAnimation(_rawRes);
        _loLottieAnimationView.playAnimation();
    }

    private void resetGame() {
        new_ti.cancel();
        changeGame();
    }

    /**
     * Fonction vérifiant le choix du joueur
     * @param rep1
     * @param rep2
     * @param rep3
     */
    private void verif(final ImageView rep1, final ImageView rep2, final ImageView rep3){

        final LottieAnimationView gameAnimationView = findViewById(R.id.animation_view_game);

        (rep1).setOnClickListener(new View.OnClickListener() {
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
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException ex) {
//                        android.util.Log.d("Erreur: ", ex.toString());
//                    }
                    lottieDisplay(gameAnimationView, R.raw.check_orange);
                    Log.d("lottie", "lottieDisplayed");
                    verificationRegle();
                    resPartiePrecedente = true;
                }
            }
        });

        (rep2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isReady) {
                    isReady = false;
                    if (timerActif == true) {
                        timerExpo.cancel();
                    }
//                    Toast.makeText(ColorWordsActivity.this, "Incorrect", Toast.LENGTH_LONG).show();
                    nb_defaite++;
                    Log.d("defaites: ", "" + nb_defaite);
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException ex) {
//                        android.util.Log.d("Erreur: ", ex.toString());
//                    }
                    lottieDisplay(gameAnimationView, R.raw.unapproved_cross);
                    Log.d("lottie", "lottieDisplayed");
                    verificationRegle();
                    resPartiePrecedente = false;
                }
            }
        });

        (rep3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isReady) {
                    isReady = false;
                    if (timerActif == true) {
                        timerExpo.cancel();
                    }
//                    Toast.makeText(ColorWordsActivity.this, "Incorrect", Toast.LENGTH_LONG).show();
                    nb_defaite++;
                    Log.d("defaites: ", "" + nb_defaite);
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException ex) {
//                        android.util.Log.d("Erreur: ", ex.toString());
//                    }
                    lottieDisplay(gameAnimationView, R.raw.unapproved_cross);
                    Log.d("lottie", "lottieDisplayed");
                    verificationRegle();
                    resPartiePrecedente = false;
                }
            }
        });
    }

    /**
     * Fonction retournant une figure aléatoire
     * @return
     */
    private int setRandomFigure(){
        Random rnd = new Random();
        String randomFigure = list.get(rnd.nextInt(list.size()));
        return listDrawable.get(randomFigure);
    }

    /**
     * Fonction changeant aléatoirement la position des trois figures
     * @param rep1
     * @param rep2
     * @param rep3
     */
    private void switchPosition(ImageView rep1,ImageView rep2,ImageView rep3){

        float posXimg = rep1.getX();
        float posYimg = rep1.getY();

        float posXimg2 = rep2.getX();
        float posYimg2 = rep2.getY();

        float posXimg3 = rep3.getX();
        float posYimg3 = rep3.getY();

        int nb = new Random().nextInt(6);

        switch (nb){
            case 1:
                rep2.setX(posXimg3);
                rep2.setY(posYimg3);
                rep3.setX(posXimg2);
                rep3.setY(posYimg2);
                break;
            case 2:
                rep2.setX(posXimg);
                rep2.setY(posYimg);
                rep3.setX(posXimg2);
                rep3.setY(posYimg2);
                rep1.setX(posXimg3);
                rep1.setY(posYimg3);
                break;
            case 3:
                rep2.setX(posXimg);
                rep2.setY(posYimg);
                rep1.setX(posXimg2);
                rep1.setY(posYimg2);
                break;
            case 4:
                rep3.setX(posXimg);
                rep3.setY(posYimg);
                rep1.setX(posXimg2);
                rep1.setY(posYimg2);
                rep2.setX(posXimg3);
                rep2.setY(posYimg3);
                break;
            case 5:
                rep3.setX(posXimg);
                rep3.setY(posYimg);
                rep1.setX(posXimg3);
                rep1.setY(posYimg3);
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

        final TextView timer = findViewById(R.id.timer);
        ImageView figure = findViewById(R.id.figure);
        TextView question = findViewById(R.id.question);
        ImageView rep1 = findViewById(R.id.reponse1);
        ImageView rep2 = findViewById(R.id.reponse2);
        ImageView rep3 = findViewById(R.id.reponse3);

        rep1.setVisibility(View.INVISIBLE);
        rep2.setVisibility(View.INVISIBLE);
        rep3.setVisibility(View.INVISIBLE);

        figure.setVisibility(View.VISIBLE);
        correct_figure = setRandomFigure();
        figure2 = setRandomFigure();
        while(figure2 == correct_figure){
            figure2 = setRandomFigure();
        }
        figure3 = setRandomFigure();
        while (figure3 == figure2 || figure3 == correct_figure) {
            figure3 = setRandomFigure();
        }

        question.setText("Quel est la figure représentée?");
        figure.setImageResource(correct_figure);

        timerExpo = new CountDownTimer(tempsexposition, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Memorisez! : " + millisUntilFinished / 1000);
                timerExpoActif = true;
                timeExpoLeft = millisUntilFinished/1000;
            }

            public void onFinish() {

                timerExpoActif = false;

                ImageView figure = findViewById(R.id.figure);
                ImageView rep1 = findViewById(R.id.reponse1);
                ImageView rep2 = findViewById(R.id.reponse2);
                ImageView rep3 = findViewById(R.id.reponse3);

                figure.setVisibility(View.INVISIBLE);
                rep1.setImageResource(correct_figure);
                rep2.setImageResource(figure2);
                rep3.setImageResource(figure3);

                rep1.setVisibility(View.VISIBLE);
                rep2.setVisibility(View.VISIBLE);
                rep3.setVisibility(View.VISIBLE);

                switchPosition(rep1, rep2, rep3);
                resetTimer();
            }
        };
        timerExpo.start();
    }

    /**
     * Fonction démarrant la seconde variante du jeu (couleur représentée par mot)
     */
    private void newGame2(){

        variante = 2;

        final TextView timer = findViewById(R.id.timer);
        final ImageView figure = findViewById(R.id.figure);
        final TextView question = findViewById(R.id.question);
        final ImageView rep1 = findViewById(R.id.reponse1);
        final ImageView rep2 = findViewById(R.id.reponse2);
        final ImageView rep3 = findViewById(R.id.reponse3);

        rep1.setVisibility(View.INVISIBLE);
        rep2.setVisibility(View.INVISIBLE);
        rep3.setVisibility(View.INVISIBLE);

        figure.setVisibility(View.VISIBLE);
        correct_figure = setRandomFigure();
        figure2 = setRandomFigure();
        while(figure2 == correct_figure){
            figure2 = setRandomFigure();
        }
        figure3 = setRandomFigure();
        while (figure3 == figure2 || figure3 == correct_figure) {
            figure3 = setRandomFigure();
        }


        for(int i = 0; i < 2; i++){
            listFigure.add(setRandomFigure());
        }
        listFigure.add(correct_figure);
        Log.d("rfdsdfs",""+listFigure.get(0)+listFigure.get(1)+listFigure.get(2));

        for(int i = 0; i < listFigure.size(); i++) {
            Random rnd = new Random();
            pos = rnd.nextInt(listFigure.size());
            figure.setImageResource(listFigure.get(pos));
            figure.startAnimation(rotation);
        }

        /*
        timerExpo = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText("Memorisez! : " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                if(listFigure.size() == 0){
                    question.setText("Quel était la figure à la "+correct_pos+" position?");
                    figure.setVisibility(View.INVISIBLE);
                    rep1.setImageResource(correct_figure);
                    rep2.setImageResource(figure2);
                    rep3.setImageResource(figure3);

                    rep1.setVisibility(View.VISIBLE);
                    rep2.setVisibility(View.VISIBLE);
                    rep3.setVisibility(View.VISIBLE);

                    switchPosition(rep1, rep2, rep3);
                    resetTimer();
                }
            }
        };
        timerExpo.start();
        */
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
            newGame();
        }
    }

    private void changeTimer(boolean increase){
        new_ti.cancel();
        if(increase == true) {
            maxtime += 1000;
        }else{
            maxtime = maxtime - 1000;
        }
    }

    private void resetTimer(){
        //Création d'un timer
        final TextView timer = findViewById(R.id.timer);

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

        tempsexposition = tempsexposition - 1000;
    }

    private void jouerRegle(Regle regle){

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
            case "Réduire le temps d'exposition de la forme":
                reduireTempsExposition();
                regleJoue = "Temps d'exposition de la forme diminué!";
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
                2,
                (int)timeleft,
                variante,
                resPartiePrecedente,
                5,
                "Augmentation du temps consacré au timer");
        Regle regle2 = new Regle(userId,
                2,
                (int)timeleft,
                variante,
                resPartiePrecedente,
                5,
                "Diminution du temps consacré au timer");
        Regle regle3 = new Regle(userId,
                2,
                (int)timeleft,
                variante,
                resPartiePrecedente,
                5,
                "Réduire le temps d'exposition de la forme");

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
            if(userId == userIdR && idJeuR == 2 && timeleft == timeleftR && variante == varianteR && resPartiePrecedente == resPartiePrecedenteR){
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
            action = "Réduire le temps d'exposition de la forme";
        }
        return action;
    }


    @Override
    public void onBackPressed() {
        final TextView timer = findViewById(R.id.timer);
        if(timerExpoActif == false) {
            new_ti.cancel();
        }else{
            timerExpo.cancel();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Quitter le jeu?")
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AppDatabase db_loc = Room.databaseBuilder(getApplicationContext(),
                                AppDatabase.class, "evolutivmind").allowMainThreadQueries().build();

                        //S'il n'y pas de statistique pour ce joueur, on la crée
                        if(db_loc.statistiqueDao().countStatistique(userId, "Memorize") == 0) {
                            Statistique statistiqueColorwords = new Statistique();
                            statistiqueColorwords.setVictoires(nb_victoire);
                            statistiqueColorwords.setDefaites(nb_defaite);
                            statistiqueColorwords.setJeu("Memorize");
                            statistiqueColorwords.setUserId(userId);
                            db_loc.statistiqueDao().insert(statistiqueColorwords);
                        }else{
                            //S'il y a déjà des statistiques pour ce joueur, on la met à jour
                            Statistique statistiqueJoueur = db_loc.statistiqueDao().findStatistiqueJeuForUser(userId, "Memorize");
                            statistiqueJoueur.setVictoires(statistiqueJoueur.getVictoires()+nb_victoire);
                            statistiqueJoueur.setDefaites(statistiqueJoueur.getDefaites()+nb_defaite);
                            db_loc.statistiqueDao().update(statistiqueJoueur);
                        }
                        //On met à jour le profil du joueur
                        Profil joueur = db_loc.profilDao().getProfilById(userId);
                        joueur.setExperience(joueur.getExperience()+experienceGagne);
                        joueur.setNiveau(joueur.getNiveau()+levelUp);
                        db_loc.profilDao().update(joueur);
                        MemorizeActivity.this.finish();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(timerExpoActif == false) {
                            new_ti = new CountDownTimer(timeleft * 1000 + 1000, 1000) {

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
                        }else{
                            newGame();
                        }
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

