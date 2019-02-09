package com.example.muguet.evolutivmind.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(foreignKeys = @ForeignKey(entity = Profil.class,
        parentColumns = "id",
        childColumns = "userId"))

public class Statistique {

    @PrimaryKey public final int id;
    public final String jeu;
    public final int victoires;
    public final int defaites;
    public final int userId;

    public Statistique(final int id, final String jeu, final int victoires, final int defaites,
                final int userId) {
        this.id = id;
        this.jeu = jeu;
        this.victoires = victoires;
        this.defaites = defaites;
        this.userId = userId;
    }
}
