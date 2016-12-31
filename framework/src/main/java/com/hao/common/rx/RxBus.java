package com.hao.common.rx;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * @Package com.daoda.aijiacommunity.common.rx
 * @作 用:用rxjava实现的eventbus
 * @创 建 人: linguoding
 * @日 期: 2016-01-18
 */
public class RxBus {
    private Subject<Object, Object> mBus;
    private static RxBus sInstance;

    private RxBus() {
        mBus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getInstance() {
        if (sInstance == null) {
            // [1]
            synchronized (RxBus.class) {
                if (sInstance == null) {
                    //单例模式之双重检测：线程一在此之前线程二到达了位置[1],如果此处不二次判断，那么线程二执行到这里的时候还会重新new
                    sInstance = new RxBus();
                }
            }
        }
        return sInstance;
    }

    private Subject<Object, Object> getBus() {
        return mBus;
    }

    public static boolean hasObservers() {
        return getInstance().getBus().hasObservers();
    }

    public static void send(Object obj) {
        if (getInstance().hasObservers()) {
            getInstance().getBus().onNext(obj);
        }
    }

    public static Observable<Object> toObservable() {
        return getInstance().getBus();
    }

    public static <T> Observable<T> toObservable(Class<T> clazz) {
        return getInstance().toObservable().ofType(clazz).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Observable<T> toObservableAndBindToLifecycle(Class<T> clazz, LifecycleProvider lifecycleProvider) {
        return getInstance().toObservable(clazz).compose(lifecycleProvider.bindToLifecycle());
    }

    public static <T> Observable<T> toObservableAndBindUntilStop(Class<T> clazz, LifecycleProvider lifecycleProvider) {
        return getInstance().toObservable(clazz).compose(lifecycleProvider.bindUntilEvent(lifecycleProvider instanceof RxAppCompatActivity ? ActivityEvent.STOP : FragmentEvent.STOP));
    }
}
