package com.example.muguet.evolutivmind.ia;

import androidx.room.*;

import java.util.List;

@Dao
public interface RegleDAO {

    @Insert
    void insert(Regle regle);

    @Update
    void update(Regle... regle);

    @Delete
    void delete(Regle... regle);

    @Query("SELECT * FROM regle")
    List<Regle> getAllRegle();

    @Query("SELECT `action` FROM regle WHERE id=:id")
    String getAction(final int id);
}
