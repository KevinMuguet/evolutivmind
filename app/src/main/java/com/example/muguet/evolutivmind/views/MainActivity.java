package com.example.muguet.evolutivmind.views;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.muguet.evolutivmind.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private List<String> list;
    private HashMap<String, Integer> listColor = new HashMap<>();
    private int correct_color;
    private int color2;
    private int color3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mot = (TextView) findViewById(R.id.mot);
        ImageView img = (ImageView)findViewById(R.id.rectangle);
        ImageView img2 = (ImageView)findViewById(R.id.rectangle2);
        ImageView img3 = (ImageView)findViewById(R.id.rectangle3);

        listColor.put("Bleu", Color.BLUE);
        listColor.put("Rouge", Color.RED);
        listColor.put("Noir", Color.BLACK);

        list = new ArrayList<String>(listColor.keySet());

        newGame();
        verif(img, img2, img3);

    }

    private void verif(final ImageView img, final ImageView img2, final ImageView img3){
        (img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "YES", Toast.LENGTH_LONG).show();
                changeGame();
            }
        });

        (img2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "YES", Toast.LENGTH_LONG).show();
                changeGame();
            }
        });

        (img3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "YES", Toast.LENGTH_LONG).show();
                changeGame();
            }
        });
    }

    private int setRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256));
    }

    private String setRandomColorString(){
        Random random = new Random();
        String randomColor = list.get(random.nextInt(list.size()));
        return randomColor;
    }

    private void switchPosition(ImageView img,ImageView img2,ImageView img3){

        float posXimg = img.getX();
        float posYimg = img.getY();

        float posXimg2 = img2.getX();
        float posYimg2 = img2.getY();

        float posXimg3 = img3.getX();
        float posYimg3 = img3.getY();

        int nb = new Random().nextInt(5);

        switch (nb){
            case 1:
                img2.setX(posXimg3);
                img2.setY(posYimg3);
                img3.setX(posXimg2);
                img3.setY(posYimg2);
                break;
            case 2:
                img2.setX(posXimg);
                img2.setY(posYimg);
                img3.setX(posXimg2);
                img3.setY(posYimg2);
                img.setX(posXimg3);
                img.setY(posYimg3);
                break;
            case 3:
                img2.setX(posXimg);
                img2.setY(posYimg);
                img.setX(posXimg2);
                img.setY(posYimg2);
                break;
            case 4:
                img3.setX(posXimg);
                img3.setY(posYimg);
                img.setX(posXimg2);
                img.setY(posYimg2);
                img2.setX(posXimg3);
                img2.setY(posYimg3);
                break;
            case 5:
                img3.setX(posXimg);
                img3.setY(posYimg);
                img.setX(posXimg3);
                img.setY(posYimg3);
                break;
            case 0:
                break;
        }
    }

    private void newGame(){

        TextView mot = (TextView) findViewById(R.id.mot);
        ImageView img = (ImageView)findViewById(R.id.rectangle);
        ImageView img2 = (ImageView)findViewById(R.id.rectangle2);
        ImageView img3 = (ImageView)findViewById(R.id.rectangle3);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.rectangle);
        Drawable drawable2 = res.getDrawable(R.drawable.rectangle2);
        Drawable drawable3 = res.getDrawable(R.drawable.rectangle3);

        correct_color = setRandomColor();
        color2 = setRandomColor();
        color3 = setRandomColor();

        mot.setTextColor(correct_color);
        mot.setText("Reset");

        drawable.setColorFilter(correct_color, PorterDuff.Mode.SRC_ATOP);
        img.setBackground(drawable);

        drawable2.setColorFilter(color2, PorterDuff.Mode.SRC_ATOP);
        img2.setBackground(drawable2);

        drawable3.setColorFilter(color3, PorterDuff.Mode.SRC_ATOP);
        img3.setBackground(drawable3);

        switchPosition(img, img2, img3);

    }

    private void newGame2(){

        TextView mot = (TextView) findViewById(R.id.mot);
        ImageView img = (ImageView)findViewById(R.id.rectangle);
        ImageView img2 = (ImageView)findViewById(R.id.rectangle2);
        ImageView img3 = (ImageView)findViewById(R.id.rectangle3);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.rectangle);
        Drawable drawable2 = res.getDrawable(R.drawable.rectangle2);
        Drawable drawable3 = res.getDrawable(R.drawable.rectangle3);
        String rndColorString = setRandomColorString();

        correct_color = listColor.get(rndColorString);
        color2 = setRandomColor();
        color3 = setRandomColor();

        mot.setTextColor(color2);
        mot.setText(rndColorString);

        drawable.setColorFilter(correct_color, PorterDuff.Mode.SRC_ATOP);
        img.setBackground(drawable);

        drawable2.setColorFilter(color2, PorterDuff.Mode.SRC_ATOP);
        img2.setBackground(drawable2);

        drawable3.setColorFilter(color3, PorterDuff.Mode.SRC_ATOP);
        img3.setBackground(drawable3);

        switchPosition(img, img2, img3);

    }

    private void changeGame(){
        Random rnd = new Random();
        int game = rnd.nextInt(2);
        Log.d("Couleur ", Integer.toString(game));
        if(game == 1){
            newGame();
        }else{
            newGame2();
        }
    }
}
