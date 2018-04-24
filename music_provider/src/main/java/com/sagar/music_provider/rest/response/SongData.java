package com.sagar.music_provider.rest.response;

import java.util.ArrayList;
import java.util.List;

public class SongData {

    public String name;
    public String artistName;
    public String artworkUrl100;
    public String url;
    public List<GenreData> genres;

    public SongData() {
        name = "";
        artistName = "";
        artworkUrl100 = "";
        url = "";
        genres = new ArrayList<>();
    }

}
