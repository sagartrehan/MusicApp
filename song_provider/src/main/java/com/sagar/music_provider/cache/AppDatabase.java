package com.sagar.music_provider.cache;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.sagar.music_provider.song_provider.Song;

@Database(entities = {Song.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SongCache getSongCache();

}
