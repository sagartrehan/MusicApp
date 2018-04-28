package com.sagar.music_provider.song_provider;

import com.sagar.music_provider.Constants;
import com.sagar.music_provider.Response;
import com.sagar.music_provider.cache.SongCache;
import com.sagar.music_provider.rest.SongApi;
import com.sagar.music_provider.rest.response.GenreData;
import com.sagar.music_provider.rest.response.SongData;
import com.sagar.music_provider.rest.response.SongsResponse;
import com.sagar.music_provider.rest.response.SongsResponseData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.subscribers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class SongDataProviderImplTest {

    @Mock
    private SongApi songApi;

    @Mock
    private SongCache songCache;

    private SongDataProvider songDataProvider;

    @Before
    public void setUp() {
        songDataProvider = new SongDataProviderImpl(songApi, songCache);
    }

    @Test
    public void test_FetchSongs_With_Invalid_SongRequest() {
        SongRequest songRequest = mock(SongRequest.class);
        TestSubscriber<Response<List<Song>>> fetchSongSubscriber = songDataProvider
                .fetchSongs(songRequest)
                .test();
        List<Throwable> errorList = fetchSongSubscriber.errors();
        assertNotNull(errorList);
        assertNotNull(errorList.get(0));
        SongFetchException songFetchException = (SongFetchException) errorList.get(0);
        assertEquals(Constants.ErrorType.INVALID_SONG_REQUEST, songFetchException.getErrorType());
    }

    @Test
    public void test_FetchSongs_When_Network_Not_Available() {
        Song song1 = mock(Song.class);
        when(song1.getArtistName()).thenReturn("Arjit Singh");
        when(song1.getGenres()).thenReturn("hi");
        when(song1.getId()).thenReturn("123");
        when(song1.getName()).thenReturn("Love you soniyee");
        when(song1.getUrl()).thenReturn("www.google.com");
        List<Song> songList = new ArrayList<>();
        songList.add(song1);

        when(songCache.getSongs()).thenReturn(Flowable.just(songList));
        when(songApi.getTopMusicListing("HK", 10))
                .thenReturn(Single.<SongsResponse>error(new ConnectException("Unresolved host")));

        TestSubscriber<Response<List<Song>>> songListSubscriber = songDataProvider
                .fetchSongs(new SongRequest("HK", 10))
                .test();

        assertNotNull(songListSubscriber.values());
        Response<List<Song>> songResponseList = songListSubscriber.values().get(0);
        assertNotNull(songResponseList);
        assertNotNull(songResponseList.getData());
        assertEquals(1, songResponseList.getData().size());
        assertEquals("Arjit Singh", songResponseList.getData().get(0).getArtistName());
    }

    @Test
    public void test_FetchSongs_When_Network_Available() {
        Song song1 = mock(Song.class);
        when(song1.getId()).thenReturn("1");
        when(song1.getArtistName()).thenReturn("Arjit Singh");
        when(song1.getUrl()).thenReturn("www.google.com");
        when(song1.getName()).thenReturn("Love you soniyee");
        when(song1.getGenres()).thenReturn("genre");
        List<Song> songList = new ArrayList<>();
        songList.add(song1);

        when(songCache.getSongs()).thenReturn(Flowable.just(songList));

        List<GenreData> genreList = new ArrayList<>();
        GenreData genreData = new GenreData();
        genreData.genreId = "1";
        genreData.name = "genre";
        genreData.url = "www.google.com";

        SongData songData1 = mock(SongData.class);
        songData1.id = "1";
        songData1.genres = genreList;
        songData1.name = "Love you soniyee";
        songData1.artistName = "Arjit Singh";
        songData1.url = "www.google.com";
        List<SongData> songDataList = new ArrayList<>();
        songDataList.add(songData1);

        SongsResponse songsResponse = mock(SongsResponse.class);
        SongsResponseData songsResponseData = mock(SongsResponseData.class);
        songsResponse.feed = songsResponseData;
        songsResponseData.results = songDataList;

        when(songApi.getTopMusicListing("HK", 10)).thenReturn(Single.just(songsResponse));

        TestSubscriber<Response<List<Song>>> songListSubscriber = songDataProvider
                .fetchSongs(new SongRequest("HK", 10))
                .test();
        assertNotNull(songListSubscriber.values());
        Response<List<Song>> songResponseList = songListSubscriber.values().get(0);
        assertNotNull(songResponseList);
        assertNotNull(songResponseList.getData());
        assertEquals(1, songResponseList.getData().size());
        assertEquals("Arjit Singh", songResponseList.getData().get(0).getArtistName());
    }


}
