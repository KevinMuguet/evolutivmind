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
    void delete(Profil... profil);

    @Query("SELECT * FROM profil")
    List<Profil> getAllProfil();

    @Query("SELECT * FROM profil WHERE nom=:username")
    Profil getProfil(final String username);

    @Query("SELECT * FROM profil WHERE id=:id")
    Profil getProfilById(final int id);

}
