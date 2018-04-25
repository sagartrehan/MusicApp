package com.sagar.music_provider;

public class Response<DATA> {

    private String errorType;
    private String responseSource;
    private Throwable throwable;
    private DATA data;
    private boolean isLoading;

    Response(String errorType, String responseSource, boolean isLoading, Throwable throwable) {
        this.errorType = errorType;
        this.responseSource = responseSource;
        this.throwable = throwable;
    }

    Response(String responseSource, DATA data, boolean isLoading) {
        this.responseSource = responseSource;
        this.data = data;
    }

    public boolean isError() {
        return errorType != null;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getResponseSource() {
        return responseSource;
    }

    public DATA getData() {
        return data;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
