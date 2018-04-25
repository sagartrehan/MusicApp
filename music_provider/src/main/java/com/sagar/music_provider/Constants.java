package com.sagar.music_provider;

public final class Constants {

    public static final int TOP_SONG_LIST_LIMIT = 10;

    public static final class ResponseSource {
        public static final String LOCAL = "local";
        public static final String REMOTE = "remote";
    }

    public static final class ErrorType {
        public static final String RETROFIT = "network";
        public static final String INVALID_SONG_REQUEST = "invalid_song_request";
        public static final String SONG_LIST_EMPTY = "song_list_empty";
    }

}
