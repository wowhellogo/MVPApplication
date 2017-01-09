package com.hao.common.net;
import com.hao.common.utils.NetWorkUtil;
import com.hao.common.utils.StorageUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * @Package com.daoda.aijiacommunity.common.net
 * @作 用:Retrofit 离线缓存策略的实现
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2016/6/1 0001
 */
public abstract class AbRestNetDataSource {
    public final static int MAX_ATTEMPS = 3;//连接次数
    protected Retrofit retrofit;
    protected OkHttpClient okHttpClient;
    private CacheInterceptor mInterceptor=new CacheInterceptor();
    public class CacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();//获取请求
            HttpUrl httpUrl = request.url();
            //这里就是说判读我们的网络条件，要是有网络的话就直接获取网络上面的数据，要是没有网络的话就去缓存里面取数据
            if(NetWorkUtil.isNetworkAvailable()){
                request = request.newBuilder()
                        .url(httpUrl)
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
                Response originalResponse = chain.proceed(request);
                return originalResponse.newBuilder()
                        //这里设置的为0就是说不进行缓存，我们也可以设置缓存时间
                        .header("Cache-Control", "public, max-age=" + 0)
                        .removeHeader("Pragma")
                        .build();
            }else{
                request = request.newBuilder()
                        .url(httpUrl)
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                int maxTime = 4*24*60*60;
                Response originalResponse = chain.proceed(request);
                return originalResponse.newBuilder()
                        //这里的设置的是我们的没有网络的缓存时间，想设置多少就是多少。
                        .header("Cache-Control", "public, only-if-cached, max-stale="+maxTime)
                        .removeHeader("Pragma")
                        .build();
            }

        }

    }

    public AbRestNetDataSource() {
        //设置缓存路径
        File httpCacheDirectory = new File(StorageUtil.getCacheDir(), "responses");
        if (!httpCacheDirectory.exists()) {
            httpCacheDirectory.mkdirs();
        }
        //设置缓存 10M
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);

        okHttpClient = new OkHttpClient().newBuilder().addInterceptor(mInterceptor)
                .addNetworkInterceptor(mInterceptor).cache(cache).build();
        newRequest();
    }

    public abstract void newRequest();
}
