package com.sagar.music_provider;

import com.sagar.music_provider.rest.response.GenreData;
import com.sagar.music_provider.rest.response.SongData;

import java.util.ArrayList;
import java.util.List;

public class Song {

    private String name;
    private String artistName;
    private String thumbnail;
    private String url;
    private List<String> genres;

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

    public List<String> getGenres() {
        return genres;
    }

    public static Song from(SongData songData) {
        List<String> genres = new ArrayList<>();
        for (GenreData genreData : songData.genres) {
            genres.add(genreData.name);
        }

        Song song = new Song();
        song.name = songData.name;
        song.artistName = songData.artistName;
        song.thumbnail = songData.artworkUrl100;
        song.url = songData.url;
        song.genres = genres;
        return song;
    }

}
