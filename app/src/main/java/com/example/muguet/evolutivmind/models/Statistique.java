package com.example.muguet.evolutivmind.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(foreignKeys = @ForeignKey(entity = Profil.class,
        parentColumns = "id",
        childColumns = "userId"))

public class Statistique {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String jeu;

    public int getVictoires() {
        return victoires;
    }

    public int getDefaites() {
        return defaites;
    }

    public int victoires;
    public int defaites;
    public int userId;

    public void setVictoires(int victoires){
        this.victoires = victoires;
    }

    public void setDefaites(int defaites){
        this.defaites = defaites;
    }

    public void setJeu(String jeu){
        this.jeu = jeu;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }
}
