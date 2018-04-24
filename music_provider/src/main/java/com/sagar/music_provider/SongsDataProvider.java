package com.sagar.music_provider;

import java.util.List;

import io.reactivex.Flowable;

public interface SongsDataProvider {

    Flowable<List<Song>> fetchSongs(SearchRequest searchRequest);

}
