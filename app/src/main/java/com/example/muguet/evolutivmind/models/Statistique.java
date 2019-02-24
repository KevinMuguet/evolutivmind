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
    public int victoires;
    public int defaites;
    public int userId;

}
