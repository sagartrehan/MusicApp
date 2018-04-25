package com.sagar.music_provider.rest.response;

import java.util.ArrayList;
import java.util.List;

public class SongsResponseData {

    public List<SongData> results;

    public SongsResponseData() {
        results = new ArrayList<>();
    }
}
