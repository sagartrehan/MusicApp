package com.sagar.musicapp.song;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.sagar.music_provider.Response;
import com.sagar.music_provider.song_provider.Song;
import com.sagar.music_provider.song_provider.SongDataProvider;
import com.sagar.music_provider.song_provider.SongRequest;
import com.sagar.musicapp.NetworkUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

class SongListViewModel extends ViewModel {

    private final SongDataProvider songDataProvider;
    private final SongRequest songRequest;
    private boolean isLoading;
    private MutableLiveData<Response<List<Song>>> songListResponse;
    private MutableLiveData<Boolean> networkStateResponse;
    private Disposable songListDisposable, networkStateDisposable;

    SongListViewModel(SongDataProvider songDataProvider, SongRequest songRequest) {
        this.songDataProvider = songDataProvider;
        this.songRequest = songRequest;
    }

    MutableLiveData<Response<List<Song>>> getSongListObservable() {
        if (songListResponse == null) {
            songListResponse = new MutableLiveData<>();
            loadData();
        }
        return songListResponse;
    }

    MutableLiveData<Boolean> getNetworkStateObservable(Context appContext) {
        if (networkStateResponse == null) {
            networkStateResponse = new MutableLiveData<>();
            networkStateDisposable = NetworkUtil.createNetworkStateMonitor(appContext.getApplicationContext())
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean isConnected) throws Exception {
                            networkStateResponse.setValue(isConnected);
                        }
                    });
        }
        return networkStateResponse;
    }

    void refreshData() {
        if (!isLoading) {
            loadData();
        }
    }

    private void loadData() {
        if (songListDisposable != null) {
            songListDisposable.dispose();
        }

        isLoading = true;
        songListDisposable = songDataProvider
                .fetchSongs(songRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<List<Song>>>() {
                    @Override
                    public void accept(Response<List<Song>> listResponse) throws Exception {
                        isLoading = false;
                        songListResponse.setValue(listResponse);
                    }
                });
    }

    @Override
    protected void onCleared() {
        if (songListDisposable != null) {
            songListDisposable.dispose();
        }
        if (networkStateDisposable != null) {
            networkStateDisposable.dispose();
        }
    }
}
