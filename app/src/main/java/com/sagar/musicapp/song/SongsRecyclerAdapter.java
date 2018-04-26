package com.sagar.musicapp.song;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sagar.music_provider.song_provider.Song;
import com.sagar.musicapp.R;

import java.util.List;

class SongsRecyclerAdapter extends RecyclerView.Adapter<SongsRecyclerAdapter.SongViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<Song> songList;

    SongsRecyclerAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.songIconView.setImageURI(Uri.parse(song.getThumbnail()));
        holder.songNameView.setText(song.getName());
        holder.artistNameView.setText(song.getArtistName());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder {

        private TextView songNameView, artistNameView;
        private ImageView songIconView;

        SongViewHolder(View itemView) {
            super(itemView);
            songNameView = itemView.findViewById(R.id.song_name);
            artistNameView = itemView.findViewById(R.id.artist_name);
            songIconView = itemView.findViewById(R.id.song_icon);
        }

    }

}
