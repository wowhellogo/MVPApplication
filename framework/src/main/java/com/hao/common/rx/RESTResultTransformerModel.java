package com.hao.common.rx;

import com.hao.common.exception.NotFoundDataException;
import com.hao.common.exception.ResolveErrorException;
import com.hao.common.exception.RestErrorException;
import com.hao.common.net.result.RESTResult;
import com.hao.common.net.result.Result;

import rx.Observable;
import rx.functions.Func1;

/**
 * @Package com.hao.common.rx
 * @作 用:RESTResult<E> 转换Model
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年09月05日  14:11
 */

public class RESTResultTransformerModel<E> implements Observable.Transformer<RESTResult<E>,E> {
    @Override
    public Observable<E> call(Observable<RESTResult<E>> restResultObservable) {
        return restResultObservable.flatMap(new Func1<RESTResult<E>, Observable<E>>() {
            @Override
            public Observable<E> call(RESTResult<E> mrestResult) {
                if (!mrestResult.isHasError()) {//成功
                    if (mrestResult.getResult() != null) {
                        if (mrestResult.getResult().isHasData()) {//有数据
                            Result<E> result = mrestResult.getResult();
                            return Observable.just(result.getModel());
                        } else {//没有数据
                            return Observable.error(new NotFoundDataException(mrestResult.getMessage()+""));
                        }
                    } else {
                        return Observable.error(new ResolveErrorException());//解析出错
                    }
                } else {//失败
                    return Observable.error(new RestErrorException(mrestResult.getMessage()+""));
                }
            }
        });
    }
}
