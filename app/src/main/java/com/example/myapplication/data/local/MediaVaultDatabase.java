package com.example.myapplication.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = { WatchlistItem.class }, version = 2, exportSchema = false)
public abstract class MediaVaultDatabase extends RoomDatabase {

    private static volatile MediaVaultDatabase INSTANCE;

    public abstract WatchlistDao watchlistDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE watchlist ADD COLUMN status TEXT DEFAULT 'Plan to Watch'");
        }
    };

    public static MediaVaultDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MediaVaultDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MediaVaultDatabase.class,
                            "mediavault_database")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
