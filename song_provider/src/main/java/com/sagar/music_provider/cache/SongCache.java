package com.sagar.music_provider.cache;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.sagar.music_provider.song_provider.Song;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface SongCache {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveSongs(List<Song> songs);

    @Query("SELECT * FROM songs_list")
    Flowable<List<Song>> getSongs();

    @Query("DELETE from songs_list")
    void deleteAllSongs();
}
