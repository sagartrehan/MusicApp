package com.sagar.music_provider;

import android.annotation.SuppressLint;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public abstract class NetworkBoundSource<LocalType, RemoteType> {

    @SuppressLint("CheckResult")
    protected NetworkBoundSource(final FlowableEmitter<Response<LocalType>> emitter) {
        final Disposable firstDataDisposable = getLocal()
                .map(new Function<LocalType, Response<LocalType>>() {
                    @Override
                    public Response<LocalType> apply(LocalType localType) throws Exception {
                        return new Response<>(Constants.ResponseSource.LOCAL, localType, true);
                    }
                })
                .subscribe(new Consumer<Response<LocalType>>() {
                    @Override
                    public void accept(Response<LocalType> localTypeResponse) throws Exception {
                        emitter.onNext(localTypeResponse);
                    }
                });

        getRemote().map(mapper())
                .subscribe(new Consumer<LocalType>() {
                    @Override
                    public void accept(LocalType localType) throws Exception {
                        firstDataDisposable.dispose();
                        saveCallResult(localType);
                        getLocal()
                                .map(new Function<LocalType, Response<LocalType>>() {
                                    @Override
                                    public Response<LocalType> apply(LocalType localType) throws Exception {
                                        return new Response<>(Constants.ResponseSource.REMOTE, localType, false);
                                    }
                                })
                                .subscribe(new Consumer<Response<LocalType>>() {
                                    @Override
                                    public void accept(Response<LocalType> localTypeResponse) throws Exception {
                                        emitter.onNext(localTypeResponse);
                                    }
                                });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        emitter.onNext(new Response<LocalType>(
                                Constants.ErrorType.RETROFIT,
                                Constants.ResponseSource.REMOTE,
                                false,
                                throwable));
                    }
                });
    }

    public abstract Single<RemoteType> getRemote();

    public abstract Flowable<LocalType> getLocal();

    public abstract void saveCallResult(LocalType data);

    public abstract Function<RemoteType, LocalType> mapper();

}
