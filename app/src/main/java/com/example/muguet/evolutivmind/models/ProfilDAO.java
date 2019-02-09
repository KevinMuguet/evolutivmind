package com.example.muguet.evolutivmind.models;

import androidx.room.*;

import java.util.List;

@Dao
public interface ProfilDAO {

    @Insert
    void insert(Profil profil);

    @Update
    void update(Profil... profil);

    @Delete
    void delete(Statistique... profil);

    @Query("SELECT * FROM profil")
    List<Profil> getAllProfil();

}
