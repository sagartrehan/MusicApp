package com.sagar.musicapp.song;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.sagar.music_provider.song_provider.SongDataProvider;
import com.sagar.music_provider.song_provider.SongRequest;
import com.sagar.musicapp.AppModels;

public class SongListViewModelFactory implements ViewModelProvider.Factory {

    private final SongDataProvider songDataProvider;
    private final SongRequest songRequest;

    SongListViewModelFactory(AppModels appModels, SongRequest songRequest) {
        songDataProvider = appModels.getSongDataProvider();
        this.songRequest = songRequest;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SongListViewModel.class)) {
            return (T) new SongListViewModel(songDataProvider, songRequest);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
