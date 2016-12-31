package com.hao.common.rx;

import com.hao.common.exception.NotFoundDataException;
import com.hao.common.exception.ResolveErrorException;
import com.hao.common.exception.RestErrorException;
import com.hao.common.net.result.RESTResult;

import rx.Observable;
import rx.functions.Func1;

/**
 * @Package com.hao.common.rx
 * @作 用:
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年09月05日  14:22
 */

public class RESTResultTransformData implements Observable.Transformer<RESTResult,String> {
    @Override
    public Observable<String> call(Observable<RESTResult> restResultObservable) {
        return restResultObservable.flatMap(new Func1<RESTResult, Observable<String>>() {
            @Override
            public Observable<String> call(RESTResult restResult) {
                if (!restResult.isHasError()) {//成功
                    if (restResult.getResult() != null) {
                        String data = restResult.getResult().getData();
                        if (data != null && !data.equals("")) {
                            return Observable.just(data);
                        } else {
                            return Observable.error(new NotFoundDataException("data is null"));
                        }
                    } else {
                        return Observable.error(new ResolveErrorException("result is null"));//解析出错
                    }
                } else {
                    return Observable.error(new RestErrorException(restResult.getMessage()+""));
                }
            }
        });
    }
}
