package com.sagar.musicapp.song;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sagar.music_provider.Response;
import com.sagar.music_provider.song_provider.Song;
import com.sagar.music_provider.song_provider.SongRequest;
import com.sagar.musicapp.AppManager;
import com.sagar.musicapp.R;

import java.util.ArrayList;
import java.util.List;

public class SongListActivity extends AppCompatActivity {

    private static final String COUNTRY_CODE = "HK";
    private static final int LIMIT = 10;

    private List<Song> songList = new ArrayList<>();
    private SongsRecyclerAdapter songsRecyclerAdapter;
    private Button refreshButton;
    private SongListViewModel viewModel;
    private ProgressBar progressBar;
    private TextView bottomMessageView;
    private Handler handler = new Handler();

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
        bottomMessageView = findViewById(R.id.message_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        songsRecyclerAdapter = new SongsRecyclerAdapter(this, songList);
        recyclerView.setAdapter(songsRecyclerAdapter);

        SongRequest songRequest = new SongRequest(COUNTRY_CODE, LIMIT);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(bottomMessageDismissRunnable);
    }

    private void loadData() {
        viewModel.getSongListObservable()
                .observe(this, new Observer<Response<List<Song>>>() {
                    @Override
                    public void onChanged(@Nullable Response<List<Song>> listResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (listResponse == null || listResponse.isError()) {
                            showMessage(
                                    listResponse != null ? listResponse.getThrowable().getMessage() : "Error loading song list"
                            );
                        } else {
                            songList.clear();
                            songList.addAll(listResponse.getData());
                            songsRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                });
        viewModel.getNetworkStateObservable(this)
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean isConnected) {
                        refreshButton.setEnabled(isConnected != null && isConnected);

                        showMessage(
                                isConnected != null && isConnected ? "Network is available" : "No internet connection"
                        );
                    }
                });
    }

    private void showMessage(String message) {
        handler.removeCallbacks(bottomMessageDismissRunnable);
        bottomMessageView.setVisibility(View.VISIBLE);
        bottomMessageView.setText(message);
        handler.postDelayed(bottomMessageDismissRunnable, 5000);
    }

    private Runnable bottomMessageDismissRunnable = new Runnable() {
        @Override
        public void run() {
            bottomMessageView.setVisibility(View.GONE);
        }
    };
}
