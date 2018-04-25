package com.sagar.musicapp;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.facebook.stetho.Stetho;

import java.util.HashSet;
import java.util.Set;

public class AppManager extends Application {

    private static AppModels appModels;

    @Override
    public void onCreate() {
        super.onCreate();
        initModels();
        initFresco();
        initStetho();
    }

    public static AppModels getAppModels() {
        return appModels;
    }

    private void initModels() {
        appModels = new AppModels(getApplicationContext());
    }

    private void initFresco() {
        Set<RequestListener> requestListeners = new HashSet<>();
        requestListeners.add(new RequestLoggingListener());
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .setRequestListeners(requestListeners)
                .build();
        Fresco.initialize(this, config);
    }

    private void initStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(getApplicationContext());
        }
    }
}
