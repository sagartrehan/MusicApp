package com.sagar.musicapp;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.sagar.music_provider.cache.AppDatabase;
import com.sagar.music_provider.cache.SongCache;
import com.sagar.music_provider.rest.ServiceGenerator;
import com.sagar.music_provider.rest.ServiceSettings;
import com.sagar.music_provider.rest.SongApi;
import com.sagar.music_provider.song_provider.SongDataProvider;
import com.sagar.music_provider.song_provider.SongDataProviderImpl;

public class AppModels {

    private static final String DB_NAME = "song_db.sqlite";

    private SongDataProvider songDataProvider;

    AppModels(Context context) {
        ServiceSettings serviceSettings = new ServiceSettings("https", "rss.itunes.apple.com", "");
        ServiceGenerator serviceGenerator = new ServiceGenerator(serviceSettings);
        SongApi songApi = serviceGenerator.getApi(SongApi.class);

        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
        SongCache songCache = database.getSongCache();

        songDataProvider = new SongDataProviderImpl(songApi, songCache);
    }

    public SongDataProvider getSongDataProvider() {
        return songDataProvider;
    }
}
