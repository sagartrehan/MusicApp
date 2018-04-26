package com.sagar.music_provider.song_provider;

import com.sagar.music_provider.Constants;
import com.sagar.music_provider.NetworkBoundSource;
import com.sagar.music_provider.Response;
import com.sagar.music_provider.cache.SongCache;
import com.sagar.music_provider.rest.SongApi;
import com.sagar.music_provider.rest.response.SongData;
import com.sagar.music_provider.rest.response.SongsResponse;
import com.sagar.music_provider.rest.response.SongsResponseData;
import com.sagar.music_provider.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class SongDataProviderImpl implements SongDataProvider {

    private SongApi songApi;
    private SongCache songCache;

    public SongDataProviderImpl(SongApi songApi, SongCache songCache) {
        this.songApi = songApi;
        this.songCache = songCache;
    }

    @Override
    public Flowable<Response<List<Song>>> fetchSongs(final SongRequest songRequest) {
        if (ValidationUtil.isStringEmpty(songRequest.getCountryCode())) {
            SongFetchException songFetchException = new SongFetchException(Constants.ErrorType.INVALID_SONG_REQUEST);
            return Flowable.error(songFetchException);
        }

        return Flowable.create(new FlowableOnSubscribe<Response<List<Song>>>() {
            @Override
            public void subscribe(FlowableEmitter<Response<List<Song>>> emitter) throws Exception {
                new NetworkBoundSource<List<Song>, SongsResponse>(emitter) {
                    @Override
                    public Single<SongsResponse> getRemote() {
                        return songApi.getTopMusicListing(
                                songRequest.getCountryCode(),
                                songRequest.getLimit() <= 0 ? Constants.TOP_SONG_LIST_LIMIT : songRequest.getLimit()
                        );
                    }

                    @Override
                    public Flowable<List<Song>> getLocal() {
                        return songCache.getSongs();
                    }

                    @Override
                    public void saveCallResult(List<Song> songList) {
                        songCache.deleteAllSongs();
                        songCache.saveSongs(songList);
                    }

                    @Override
                    public Function<SongsResponse, List<Song>> mapper() {
                        return new Function<SongsResponse, List<Song>>() {
                            @Override
                            public List<Song> apply(SongsResponse response) throws Exception {
                                if (response == null) {
                                    throw new SongFetchException(Constants.ErrorType.SONG_LIST_EMPTY);
                                }
                                SongsResponseData songsResponseData = response.feed;
                                if (songsResponseData == null || ValidationUtil.isListEmpty(songsResponseData.results)) {
                                    throw new SongFetchException(Constants.ErrorType.SONG_LIST_EMPTY);
                                }
                                List<Song> songs = new ArrayList<>(songsResponseData.results.size());
                                for (SongData songData : songsResponseData.results) {
                                    songs.add(Song.from(songData));
                                }
                                return songs;
                            }
                        };
                    }
                };
            }
        }, BackpressureStrategy.BUFFER);
    }

}
