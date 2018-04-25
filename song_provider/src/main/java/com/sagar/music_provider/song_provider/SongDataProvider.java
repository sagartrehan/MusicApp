package com.sagar.music_provider.song_provider;

import com.sagar.music_provider.Response;

import java.util.List;

import io.reactivex.Flowable;

public interface SongDataProvider {

    Flowable<Response<List<Song>>> fetchSongs(SongRequest songRequest);

}
