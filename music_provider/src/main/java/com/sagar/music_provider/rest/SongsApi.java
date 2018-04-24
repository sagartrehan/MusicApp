package com.sagar.music_provider.rest;

import com.sagar.music_provider.rest.response.SongsResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SongsApi {

    String FETCH_TOP_MUSIC = "api/v1/{countryCode}/apple-music/top-songs/all/{limit}/explicit.json";

    @GET(FETCH_TOP_MUSIC)
    Single<SongsResponse> getTopMusicListing(
            @Path("countryCode") String countryCode,
            @Path("limit") String limit
    );

}
