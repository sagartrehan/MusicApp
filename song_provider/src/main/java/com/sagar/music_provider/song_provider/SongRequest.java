package com.sagar.music_provider.song_provider;

public class SongRequest {

    private String countryCode;
    private int limit;

    public SongRequest(String countryCode, int limit) {
        this.countryCode = countryCode;
        this.limit = limit;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public int getLimit() {
        return limit;
    }
}
