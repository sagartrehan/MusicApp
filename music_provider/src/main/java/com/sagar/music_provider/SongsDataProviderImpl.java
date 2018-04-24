package com.sagar.music_provider;

import java.util.List;

import io.reactivex.Flowable;

public class SongsDataProviderImpl implements SongsDataProvider {

    @Override
    public Flowable<List<Song>> fetchSongs(SearchRequest searchRequest) {
        return null;
    }

}
