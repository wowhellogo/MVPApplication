package com.hao.common.rx;

import com.hao.common.exception.NotDataListException;
import com.hao.common.exception.NotFoundDataException;
import com.hao.common.exception.ResolveErrorException;
import com.hao.common.exception.RestErrorException;
import com.hao.common.net.result.RESTResult;
import com.hao.common.net.result.Result;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @Package com.hao.common.rx
 * @作 用:RESTResult<E> 转换List
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年09月05日  14:17
 */

public class RESTResultTransformerList<E> implements Observable.Transformer<RESTResult<E>,List<E>> {
    @Override
    public Observable<List<E>> call(Observable<RESTResult<E>> restResultObservable) {
        return restResultObservable.flatMap(new Func1<RESTResult<E>, Observable<List<E>>>() {
            @Override
            public Observable<List<E>> call(RESTResult<E> mrestResult) {
                if (!mrestResult.isHasError()) {//成功
                    if (mrestResult.getResult() != null) {
                        if (mrestResult.getResult().isHasData()) {//有数据
                            Result<E> result = mrestResult.getResult();
                            if (null != result.getList() && result.getTotal() > 0 && result.getList().size() > 0) {
                                return Observable.just(result.getList());
                            } else {//没有更多的数据
                                return Observable.error(new NotDataListException(mrestResult.getMessage()+"", result.getTotal()));
                            }
                        } else {//List没有数据
                            return Observable.error(new NotFoundDataException(mrestResult.getMessage()));
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
