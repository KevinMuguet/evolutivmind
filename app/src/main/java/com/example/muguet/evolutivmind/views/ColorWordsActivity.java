package com.example.muguet.evolutivmind.views;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.room.Room;
import com.example.muguet.evolutivmind.R;
import com.example.muguet.evolutivmind.models.AppDatabase;
import com.example.muguet.evolutivmind.models.Session;
import com.example.muguet.evolutivmind.models.Statistique;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorwords);
        session = new Session(getApplicationContext());

        ImageView rect = (ImageView)findViewById(R.id.rectangle);
        ImageView rect2 = (ImageView)findViewById(R.id.rectangle2);
        ImageView rect3 = (ImageView)findViewById(R.id.rectangle3);

        listColor.put("Bleu", Color.BLUE);
        listColor.put("Rouge", Color.RED);
        listColor.put("Noir", Color.BLACK);
        listColor.put("Vert", Color.GREEN);
        listColor.put("Jaune", Color.YELLOW);
        listColor.put("Gris", Color.GRAY);

        list = new ArrayList<String>(listColor.keySet());

        //Création d'un timer
        final TextView timer = (TextView) findViewById(R.id.timer);
        CountDownTimer ti = new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Temps restant: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                nb_defaite = nb_defaite+1;
                changeGame();
                this.cancel();
                this.start();
            }
        };
        ti.start();

        newGame();
        verif(rect, rect2, rect3, ti);

    }

    /**
     * Fonction vérifiant le choix du joueur
     * @param rect
     * @param rect2
     * @param rect3
     */
    private void verif(final ImageView rect, final ImageView rect2, final ImageView rect3, final CountDownTimer ti){

        (rect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ti.cancel();
                Toast.makeText(ColorWordsActivity.this, "Correct", Toast.LENGTH_LONG).show();
                nb_victoire = nb_victoire+1;
                Log.d("victoires: ",""+nb_victoire);
                changeGame();
                ti.start();
            }
        });

        (rect2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ti.cancel();
                Toast.makeText(ColorWordsActivity.this, "Incorrect", Toast.LENGTH_LONG).show();
                nb_defaite = nb_defaite+1;
                Log.d("defaites: ",""+nb_defaite);
                changeGame();
                ti.start();
            }
        });

        (rect3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ti.cancel();
                Toast.makeText(ColorWordsActivity.this, "Incorrect", Toast.LENGTH_LONG).show();
                nb_defaite = nb_defaite+1;
                Log.d("defaites: ",""+nb_defaite);
                changeGame();
                ti.start();
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

        TextView mot = (TextView) findViewById(R.id.mot);
        TextView question = (TextView) findViewById(R.id.question);
        ImageView rect = (ImageView)findViewById(R.id.rectangle);
        ImageView rect2 = (ImageView)findViewById(R.id.rectangle2);
        ImageView rect3 = (ImageView)findViewById(R.id.rectangle3);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.rectangle);
        Drawable drawable2 = res.getDrawable(R.drawable.rectangle2);
        Drawable drawable3 = res.getDrawable(R.drawable.rectangle3);

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

        TextView mot = (TextView) findViewById(R.id.mot);
        TextView question = (TextView) findViewById(R.id.question);
        ImageView rect = (ImageView)findViewById(R.id.rectangle);
        ImageView rect2 = (ImageView)findViewById(R.id.rectangle2);
        ImageView rect3 = (ImageView)findViewById(R.id.rectangle3);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.rectangle);
        Drawable drawable2 = res.getDrawable(R.drawable.rectangle2);
        Drawable drawable3 = res.getDrawable(R.drawable.rectangle3);
        String rndColorString = setRandomColorString();

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
        Random rnd = new Random();
        int game = rnd.nextInt(2);
        if(game == 1){
            newGame();
        }else{
            newGame2();
        }
    }

    /**
     * Fonction sauvegardant les victoires et défaites du joueur
     */
    @Override
    public void onBackPressed() {
        AppDatabase db_loc = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "evolutivmind").allowMainThreadQueries().build();

        //S'il n'y pas de statistique pour ce joueur, on la crée
        if(db_loc.statistiqueDao().countStatistique(session.getUserId(), "ColorWords") == 0) {
            Statistique statistique_colorwords = new Statistique();
            statistique_colorwords.setVictoires(nb_victoire);
            statistique_colorwords.setDefaites(nb_defaite);
            statistique_colorwords.setJeu("ColorWords");
            statistique_colorwords.setUserId(session.getUserId());
            db_loc.statistiqueDao().insert(statistique_colorwords);
        }else{
            //S'il y a déjà des statistiques pour ce joueur, on la modifie
            Statistique statistique_joueur = db_loc.statistiqueDao().findStatistiqueJeuForUser(session.getUserId(), "ColorWords");
            statistique_joueur.setVictoires(statistique_joueur.getVictoires()+nb_victoire);
            statistique_joueur.setDefaites(statistique_joueur.getDefaites()+nb_defaite);
            db_loc.statistiqueDao().update(statistique_joueur);
        }
        finish();
        return;
    }
}
