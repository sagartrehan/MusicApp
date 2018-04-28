package com.sagar.musicapp.song;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.sagar.music_provider.Constants;
import com.sagar.music_provider.Response;
import com.sagar.music_provider.song_provider.Song;
import com.sagar.music_provider.song_provider.SongDataProvider;
import com.sagar.music_provider.song_provider.SongRequest;
import com.sagar.musicapp.NetworkUtil;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NetworkUtil.class, SongListViewModel.class})
public class SongListViewModelTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    private SongDataProvider songDataProvider;

    @Mock
    private SongRequest songRequest;

    @Mock
    private Context context;

    private SongListViewModel songListViewModel;

    @BeforeClass
    public static void setUpRxSchedulers() {
        final Scheduler immediate = new Scheduler() {
            @Override
            public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(new Executor() {
                    @Override
                    public void execute(@NonNull Runnable command) {
                        command.run();
                    }
                });
            }
        };
        RxJavaPlugins.setInitIoSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });
        RxJavaPlugins.setInitComputationSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });
        RxJavaPlugins.setInitNewThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });
        RxJavaPlugins.setInitSingleSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });
    }

    @Before
    public void setUp() {
        mockStatic(NetworkUtil.class);
        when(songRequest.getCountryCode()).thenReturn("HK");
        when(songRequest.getLimit()).thenReturn(10);
        when(context.getApplicationContext()).thenReturn(context);
        songListViewModel = new SongListViewModel(songDataProvider, songRequest);
    }

    @Test
    public void test_GetSongList_Observable() {
        Song song1 = mock(Song.class);
        when(song1.getName()).thenReturn("Love you soniyee");
        when(song1.getArtistName()).thenReturn("Arjit Singh");
        List<Song> songList = new ArrayList<>();
        songList.add(song1);
        Response<List<Song>> songListResponse = new Response<>(Constants.ResponseSource.LOCAL, songList, false);
        when(songDataProvider.fetchSongs(songRequest)).thenReturn(Flowable.just(songListResponse));

        MutableLiveData<Response<List<Song>>> responseLiveData = songListViewModel.getSongListObservable();
        Response<List<Song>> actualSongListResponse = responseLiveData.getValue();

        assertNotNull(actualSongListResponse);
        assertNotNull(actualSongListResponse.getData());
        assertNotNull(actualSongListResponse.getData().get(0));
        assertEquals("Love you soniyee", actualSongListResponse.getData().get(0).getName());
    }

    @Test
    public void test_GetNetworkState_Observable() {
        when(NetworkUtil.createNetworkStateMonitor(context)).thenReturn(Flowable.just(true));
        MutableLiveData<Boolean> networkStateLiveData = songListViewModel.getNetworkStateObservable(context);
        assertNotNull(networkStateLiveData);
        assertTrue(networkStateLiveData.getValue());
    }
}
