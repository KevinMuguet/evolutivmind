package com.example.muguet.evolutivmind.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/*
@Entity(foreignKeys = @ForeignKey(entity = Profil.class,
        parentColumns = "id",
        childColumns = "userId"))
*/
public class Statistique {

    @PrimaryKey public final int id;
    public final String name;
    public final String url;
    public final int userId;

    public Statistique(final int id, String name, String url,
                final int userId) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.userId = userId;
    }
}
