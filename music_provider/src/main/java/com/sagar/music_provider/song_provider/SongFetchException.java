package com.sagar.music_provider.song_provider;

public class SongFetchException extends Exception {

    private String errorType;

    SongFetchException(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorType() {
        return errorType;
    }
}
