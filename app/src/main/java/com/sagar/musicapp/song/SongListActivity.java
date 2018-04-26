package com.sagar.musicapp.song;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sagar.music_provider.Response;
import com.sagar.music_provider.song_provider.Song;
import com.sagar.music_provider.song_provider.SongRequest;
import com.sagar.music_provider.util.ValidationUtil;
import com.sagar.musicapp.AppManager;
import com.sagar.musicapp.R;

import java.util.ArrayList;
import java.util.List;

public class SongListActivity extends AppCompatActivity {

    private List<Song> songList = new ArrayList<>();
    private SongsRecyclerAdapter songsRecyclerAdapter;
    private Button refreshButton;
    private SongListViewModel viewModel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Top Song List");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        refreshButton = findViewById(R.id.refresh_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        songsRecyclerAdapter = new SongsRecyclerAdapter(this, songList);
        recyclerView.setAdapter(songsRecyclerAdapter);


        SongRequest songRequest = new SongRequest("HK", 10);
        SongListViewModelFactory songListViewModelFactory = new SongListViewModelFactory(
                AppManager.getAppModels(),
                songRequest
        );
        viewModel = ViewModelProviders.of(this, songListViewModelFactory).get(SongListViewModel.class);
        loadData();

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                viewModel.refreshData();
            }
        });
    }

    private void loadData() {
        viewModel.getSongListObservable()
                .observe(this, new Observer<Response<List<Song>>>() {
                    @Override
                    public void onChanged(@Nullable Response<List<Song>> listResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (listResponse != null && listResponse.getData() != null && ValidationUtil.isListNotEmpty(listResponse.getData())) {
                            songList.clear();
                            songList.addAll(listResponse.getData());
                            songsRecyclerAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), listResponse.getThrowable().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        viewModel.getNetworkStateObservable(this)
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean isConnected) {
                        refreshButton.setEnabled(isConnected != null && isConnected);
                    }
                });
    }
}
