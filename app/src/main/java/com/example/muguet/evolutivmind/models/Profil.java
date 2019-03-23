package com.example.muguet.evolutivmind.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Profil {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nom;
    public int niveau;
    public int experience;

    public int getId() {
        return id;
    }

    public void setNom(String nom){
        this.nom = nom;
    }

    public void setNiveau(int niveau){
        this.niveau = niveau;
    }

    public void setExperience(int experience){
        this.experience = experience;
    }

}
