/**
 * Copyright 2016 bingoogolapple
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hao.common.rx;

import com.hao.common.executor.JobExecutor;
import com.hao.common.executor.UIThread;
import com.trello.rxlifecycle.LifecycleProvider;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxUtil {
    private RxUtil() {
    }

    public static <T> Observable.Transformer<T, T> applySchedulersJobUI() {
        return observable -> observable.subscribeOn(Schedulers.from(JobExecutor.newInstance())).observeOn(UIThread.newInstance().getScheduler());
    }

    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Observable.Transformer<T, T> applySchedulersBindToLifecycle(LifecycleProvider lifecycleProvider) {
        if (lifecycleProvider == null) {
            return observable -> observable.compose(RxUtil.applySchedulers());
        } else {
            return observable -> observable.compose(RxUtil.applySchedulers()).compose(lifecycleProvider.bindToLifecycle());
        }
    }

    public static <T> Observable<T> runInUIThread(T t) {
        return Observable.just(t).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Observable<T> runInUIThreadDelay(T t, long delayMillis) {
        return Observable.just(t).delaySubscription(delayMillis, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread());
    }

    public static <T> Observable<T> runInUIThreadDelay(T t, long delayMillis, LifecycleProvider lifecycleProvider) {
        return Observable.just(t).delaySubscription(delayMillis, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).compose(lifecycleProvider.bindToLifecycle());
    }

    public static Observable<Void> runInUIThread() {
        return runInUIThread(null);
    }

    public static Observable<Void> runInUIThreadDelay(long delayMillis) {
        return runInUIThreadDelay(null, delayMillis);
    }

    public static Observable<Void> runInUIThreadDelay(long delayMillis, LifecycleProvider lifecycleProvider) {
        return runInUIThreadDelay(null, delayMillis, lifecycleProvider);
    }

    public static <T> Observable<T> runInIoThread(T t) {
        return Observable.just(t).observeOn(Schedulers.io());
    }

    public static Observable<Void> runInIoThread() {
        return runInIoThread(null);
    }

    public static <T> Observable<T> runInIoThreadDelay(T t, long delayMillis) {
        return Observable.just(t).delaySubscription(delayMillis, TimeUnit.MILLISECONDS, Schedulers.io());
    }

    public static Observable<Void> runInIoThreadDelay(long delayMillis) {
        return runInIoThreadDelay(null, delayMillis);
    }
}