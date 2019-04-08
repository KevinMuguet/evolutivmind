package com.example.muguet.evolutivmind.ia;

import androidx.room.*;
import com.example.muguet.evolutivmind.models.Statistique;

import java.util.List;

@Dao
public interface RegleDAO {

    @Insert
    void insert(Regle regle);

    @Update
    void update(Regle... regle);

    @Delete
    void delete(Statistique... regle);

    @Query("SELECT * FROM regle")
    List<Regle> getAllProfil();
}
