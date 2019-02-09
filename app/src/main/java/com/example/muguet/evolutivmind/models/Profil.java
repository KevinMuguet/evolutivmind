package com.example.muguet.evolutivmind.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Profil {

    @PrimaryKey public final int id;
    public final String nom;
    public final int niveau;
    public final int experience;

    public Profil(final int id, String nom, int niveau,
                       final int experience) {
        this.id = id;
        this.nom = nom;
        this.niveau = niveau;
        this.experience = experience;
    }
}
