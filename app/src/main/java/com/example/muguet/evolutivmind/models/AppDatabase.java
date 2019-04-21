package com.example.muguet.evolutivmind.models;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.muguet.evolutivmind.ia.Regle;
import com.example.muguet.evolutivmind.ia.RegleDAO;

@Database(entities = {Profil.class, Statistique.class, Regle.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract ProfilDAO profilDao();
    public abstract StatistiqueDAO statistiqueDao();
    public abstract RegleDAO regleDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "evolutivmind")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
