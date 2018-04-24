package com.sagar.music_provider.rest;

public class ServiceSettings {

    private String mProtocol;
    private String mDomain;
    private String mPort;

    public ServiceSettings(String protocol, String domain, String port) {
        this.mProtocol = protocol;
        this.mDomain = domain;
        this.mPort = port;
    }

    String getServerUrl() {
        return mProtocol + "://" + mDomain + ((mPort == null || mPort.trim().isEmpty()) ? "" : ":" + mPort);
    }

}