package com.hao.common.rx;

import com.hao.common.exception.RestErrorException;
import com.hao.common.net.result.RESTResult;

import rx.Observable;
import rx.functions.Func1;

/**
 * @Package com.hao.common.rx
 * @作 用:
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年09月05日  14:24
 */

public class RESTResultTransformBoolean implements Observable.Transformer<RESTResult,Boolean> {
    @Override
    public Observable<Boolean> call(Observable<RESTResult> restResultObservable) {
        return restResultObservable.flatMap(new Func1<RESTResult, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(RESTResult restResult) {
                if (!restResult.isHasError()) {
                    return Observable.just(true);
                } else {
                    return Observable.error(new RestErrorException(restResult.getMessage()+""));
                }
            }
        });
    }
}
