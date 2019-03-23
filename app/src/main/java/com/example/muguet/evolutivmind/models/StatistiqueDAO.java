package com.example.muguet.evolutivmind.models;

import androidx.room.*;

import java.util.List;

@Dao
public interface StatistiqueDAO {

    @Insert
    void insert(Statistique statistique);

    @Update
    void update(Statistique... statistique);

    @Delete
    void delete(Statistique... statistiques);

    @Query("SELECT * FROM statistique")
    List<Statistique> getAllStatistique();

    @Query("SELECT * FROM statistique WHERE userId=:userId")
    List<Statistique> findStatistiqueForUser(final int userId);

    @Query("SELECT * FROM statistique WHERE userId=:userId AND jeu=:jeu")
    Statistique findStatistiqueJeuForUser(final int userId, final String jeu);

    @Query("SELECT count(*) FROM statistique WHERE userId=:userId AND jeu=:jeu")
    int countStatistique(final int userId, final String jeu);
}