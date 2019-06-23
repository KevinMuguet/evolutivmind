package com.example.muguet.evolutivmind.views;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.airbnb.lottie.LottieAnimationView;
import com.example.muguet.evolutivmind.R;
import com.example.muguet.evolutivmind.ia.Regle;
import com.example.muguet.evolutivmind.models.AppDatabase;
import com.example.muguet.evolutivmind.models.Profil;
import com.example.muguet.evolutivmind.models.Statistique;
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

    private HashMap<String, Integer> listDrawable = new HashMap<>();
    private ArrayList<Integer> listFigure = new ArrayList<Integer>();
    private Integer correct_figure;
    private GifImageView levelUpAnim;
    private LottieAnimationView gameAnimationView;
    private int figure2;
    private int figure3;
    private int indice;
    private int variante;
    private boolean resPartiePrecedente;
    private long timeleft;

    private boolean timerActif;
    private int experienceGagne = 0;
    private int levelUp = 0;
    private long maxtime = 10000;
    private long tempsexposition;
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

    private Handler myHandler;
    private int randomFigure;
    private int cpt = 0; // compteur qui va nous permettre de savoir
    private ArrayList<Integer> listFormes = new ArrayList<Integer>();
    private int forme1;
    private int forme2;
    private int forme3;

    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            ImageView figure = findViewById(R.id.figure);

            if (cpt == 0) {
                forme1 = setRandomFigure();
                figure.setImageResource(forme1);
                listFigure.add(forme1);
            }
            else if (cpt == 1) {
                forme2 = setRandomFigure();
                while(forme1 == forme2) {
                    forme2 = setRandomFigure();
                }
                figure.setImageResource(forme2);
                listFigure.add(forme2);
            }
            else {
                forme3 = setRandomFigure();
                while (forme3 == forme2 || forme3 == forme1) {
                    forme3 = setRandomFigure();
                }
                figure.setImageResource(forme3);
                listFigure.add(forme3);
            }
            myHandler.postDelayed(this,1000);
            cpt++;
            if (cpt == 3) {
                /*Random aleat = new Random();
                int hasard = aleat.nextInt(3-0) + 0 ;

                Log.d("chiffre hasard ",Integer.toString(hasard));
                correct_figure = listFigure.get(hasard);
                Log.d("chiffre res ",Integer.toString(correct_figure));*/

                onPause();
                cpt =0;
            }
        }
    };

    public void onPause() {
        super.onPause();
        if(myHandler != null)
            myHandler.removeCallbacks(myRunnable); // On arrete le callback
    }


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
        newGame2();
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
                    //verificationRegle();
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
                    //verificationRegle();
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
                    //verificationRegle();
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

        int nb = new Random().nextInt(5);

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

        timerExpo = new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Memorisez! : " + millisUntilFinished / 1000);
            }

            public void onFinish() {

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
        Log.d("newgame2","je suis la ");
        final TextView timer = findViewById(R.id.timer);
        ImageView figure = findViewById(R.id.figure);
        final TextView question = findViewById(R.id.question);
        ImageView rep1 = findViewById(R.id.reponse1);
        ImageView rep2 = findViewById(R.id.reponse2);
        ImageView rep3 = findViewById(R.id.reponse3);

        rep1.setVisibility(View.INVISIBLE);
        rep2.setVisibility(View.INVISIBLE);
        rep3.setVisibility(View.INVISIBLE);

        figure.setVisibility(View.VISIBLE);


        correct_figure = setRandomFigure();
        Log.d("Log correct figure ", String.valueOf(correct_figure));
        //AFFICHAGE D'UNE SUCESSION DE FORMES
        myHandler = new Handler();
        myHandler.postDelayed(myRunnable,1000);





        figure2 = setRandomFigure();
        while(figure2 == correct_figure){
            figure2 = setRandomFigure();
        }
        figure3 = setRandomFigure();
        while (figure3 == figure2 || figure3 == correct_figure) {
            figure3 = setRandomFigure();
        }

        //Random rnd = new Random();
        //pos = rnd.nextInt(cpt);

        //question.setText("Quel est la "+ pos+"eme forme affichée ?");


        // FIN AFFICHAGE SUCESSION DE FORMES
        //figure.setImageResource(correct_figure);
        //Log.d("correct_figure",Integer.toString(correct_figure));
        timerExpo = new CountDownTimer(7000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Memorisez! : " + millisUntilFinished / 1000);
            }

            public void onFinish() {


                for (int i = 0; i < listFigure.size(); i++) {
                    if (listFigure.get(i).equals(correct_figure)) {
                        indice = i;
                    }
                }
                Log.d("Données 1er position",Integer.toString(listFigure.get(0)));
                Log.d("Données 2eme position",Integer.toString(listFigure.get(1)));
                Log.d("Données 3eme position",Integer.toString(listFigure.get(2)));
                Log.d("Données correct figure ",Integer.toString(correct_figure));

                question.setText("Quel est la "+ (indice +1) +"eme forme affichée ?");
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

                /*for(int i = 0; i < listFigure.size(); i++) {
                    Log.d("Données tab", Integer.toString(listFigure.get(i)));
                    if (correct_figure.equals(listFigure.get(i))) {
                        Log.d("Données ordre",Integer.toString(i));
                    }
                }*/
                listFigure.clear();
                switchPosition(rep1, rep2, rep3);
                resetTimer();
            }
        };
        timerExpo.start();
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
                break;
            case "Réduire le temps d'exposition du mot":
                reduireTempsExposition();
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
                timeleft,
                variante,
                resPartiePrecedente,
                5,
                randomAction());

        Log.d("Regle cree: ",""+userId+"/2/"+timeleft+"/"+variante+"/"+resPartiePrecedente);
        db_loc.regleDao().insert(regle);
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

