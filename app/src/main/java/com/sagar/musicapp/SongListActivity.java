package com.sagar.musicapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.sagar.music_provider.Response;
import com.sagar.music_provider.song_provider.Song;
import com.sagar.music_provider.song_provider.SongRequest;
import com.sagar.music_provider.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SongListActivity extends AppCompatActivity {

    private List<Song> songList = new ArrayList<>();
    private SongsRecyclerAdapter songsRecyclerAdapter;
    private Disposable songsLoaderDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        songsRecyclerAdapter = new SongsRecyclerAdapter(this, songList);
        recyclerView.setAdapter(songsRecyclerAdapter);

        loadData();
    }

    private void loadData() {
        songsLoaderDisposable = AppManager.getAppModels()
                .getSongDataProvider()
                .fetchSongs(new SongRequest("HK", 10))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<List<Song>>>() {
                    @Override
                    public void accept(Response<List<Song>> listResponse) throws Exception {
                        if (listResponse.getData() != null && ValidationUtil.isListNotEmpty(listResponse.getData())) {
                            songList.clear();
                            songList.addAll(listResponse.getData());
                            songsRecyclerAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), listResponse.getThrowable().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (songsLoaderDisposable != null) {
            songsLoaderDisposable.dispose();
        }
    }
}
