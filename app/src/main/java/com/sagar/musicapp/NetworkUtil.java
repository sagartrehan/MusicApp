package com.sagar.musicapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;

public class NetworkUtil {

    public static Flowable<Boolean> createNetworkStateMonitor(final Context context) {
        return Flowable
                .create(new FlowableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(final FlowableEmitter<Boolean> emitter) throws Exception {

                        final BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                emitter.onNext(isNetworkConnected(context));
                            }
                        };

                        emitter.setDisposable(new Disposable() {
                            @Override
                            public void dispose() {
                                context.unregisterReceiver(networkStateReceiver);
                            }

                            @Override
                            public boolean isDisposed() {
                                return false;
                            }
                        });

                        IntentFilter intentFilter = new IntentFilter();
                        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                        context.registerReceiver(networkStateReceiver, intentFilter);
                        emitter.onNext(isNetworkConnected(context));
                    }
                }, BackpressureStrategy.BUFFER);
    }

    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

}
