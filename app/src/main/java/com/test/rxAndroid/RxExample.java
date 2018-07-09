package com.test.rxAndroid;

import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RxExample {
    private static final String TAG = "RxAndroidSamples";

    private final static CompositeDisposable disposables = new CompositeDisposable();
    private final static HandlerThread handlerThread = new HandlerThread("RxAndroidSamples HandlerThread");

    public static void run1() {
        disposables.add(sampleObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.newThread())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override public void onComplete() {
                        Log.d(TAG, "onComplete(): " + Thread.currentThread());
                    }

                    @Override public void onError(Throwable e) {
                        Log.e(TAG, "onError(): " + Thread.currentThread(), e);
                    }

                    @Override public void onNext(String string) {
                        Log.d(TAG, "onNext(" + string + "): " + Thread.currentThread()) ;
                    }
                }));
    }

    public static void run() {
        if (!handlerThread.isAlive()) {
            handlerThread.start();
        }
        disposables.add(sampleObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.newThread())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.from(handlerThread.getLooper()))
                .subscribeWith(new DisposableObserver<String>() {
                    @Override public void onComplete() {
                        Log.d(TAG, "onComplete(): " + Thread.currentThread());
                    }

                    @Override public void onError(Throwable e) {
                        Log.e(TAG, "onError(): " + Thread.currentThread(), e);
                    }

                    @Override public void onNext(String string) {
                        Log.d(TAG, "onNext(" + string + "): " + Thread.currentThread()) ;
                    }
                }));
    }

    static Observable<String> sampleObservable() {
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override public ObservableSource<? extends String> call() throws Exception {
                // Do some long running operation
                SystemClock.sleep(1000);
                Log.d(TAG, "ObservableSource call : " + Thread.currentThread());
                return Observable.just("one", "two", "three", "four", "five");
            }
        });
    }
}
