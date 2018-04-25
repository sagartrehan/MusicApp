package com.sagar.music_provider.song_provider;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.sagar.music_provider.rest.response.GenreData;
import com.sagar.music_provider.rest.response.SongData;
import com.sagar.music_provider.util.Util;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "songs_list")
public class Song {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name  = "id")
    private String id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "artist_name")
    private String artistName;

    @NonNull
    @ColumnInfo(name = "thumbnail")
    private String thumbnail;

    @NonNull
    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "genres")
    private String genres;

    public String getName() {
        return name;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public String getGenres() {
        return genres;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setArtistName(@NonNull String artistName) {
        this.artistName = artistName;
    }

    public void setThumbnail(@NonNull String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public static Song from(SongData songData) {
        List<String> genres = new ArrayList<>();
        for (GenreData genreData : songData.genres) {
            genres.add(genreData.name);
        }

        Song song = new Song();
        song.id = songData.id;
        song.name = songData.name;
        song.artistName = songData.artistName;
        song.thumbnail = songData.artworkUrl100;
        song.url = songData.url;
        song.genres = Util.listToCsv(genres);
        return song;
    }

}
