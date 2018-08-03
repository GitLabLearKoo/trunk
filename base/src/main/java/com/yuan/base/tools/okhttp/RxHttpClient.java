package com.yuan.base.tools.okhttp;

import android.content.Context;

import com.yuan.base.TrunkConfig;
import com.yuan.base.tools.system.SystemUtil;
import com.yuan.base.tools.okhttp.okUtil.OKConfig;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

/**
 * Created by YuanYe on 2017/7/19.
 * 本类设计说明：根据ClientBuilder构建Client初始化，
 * 根据CacheInterceptor实现OKHttp缓存,
 * 本类主要用于创建OKHttpClient对象。
 */
public class RxHttpClient {

    private OkHttpClient client = null;
    private OKConfig config;
    private Context mContext;

    private final long CONNECTTIMEOUT = 10 * 1000l; //链接超时，单位：毫秒
    private final long READTIMEOUT = 10 * 1000l;//读取超时， 单位：毫秒

    public RxHttpClient(Context context) {
        this.mContext = context;
        this.config = OKConfig.create()
                .cookie(null)
                .connectTimeout(CONNECTTIMEOUT)
                .readTimeoutMills(READTIMEOUT)
                .isReConnection(false)
                .build();
    }

    /**
     * 传入设置参数
     *
     * @param _config
     */
    public RxHttpClient(Context context,OKConfig _config) {
        this.mContext = context;
        if (_config == null) {
            //默认设置
            config = OKConfig.create()
                    .cookie(null)
                    .connectTimeout(CONNECTTIMEOUT)
                    .readTimeoutMills(READTIMEOUT)
                    .isReConnection(false)
                    .build();
        } else {
            this.config = _config;
        }
    }

    public OkHttpClient getClient() {
        if (client == null) {
            //设置缓存路径
            File cacheFile = new File(SystemUtil.SDcard.getCachPath(mContext), TrunkConfig.NET_CACHE);
            if (cacheFile.getParentFile().exists())
                cacheFile.mkdirs();
            //设置缓存大小(当先线程的八分之一)
            Cache cache = new Cache(cacheFile, TrunkConfig.MAX_DIR_SIZE);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //失败后是否重新连接
            builder.retryOnConnectionFailure(config.isReConnection());
            //连接超时
            builder.connectTimeout(config.getConnectTimeout() != 0 ? config.getConnectTimeout() : CONNECTTIMEOUT, TimeUnit.MILLISECONDS);
            //读取超时
            builder.readTimeout(config.getReadTimeoutMills() != 0
                    ? config.getReadTimeoutMills() : READTIMEOUT, TimeUnit.MILLISECONDS);
            //设置cookie，保存cookie,读取cookie
            CookieJar cookieJar = config.getCookie();
            if (cookieJar != null) {
                builder.cookieJar(cookieJar);
            }
            //设置缓存拦截器，实现网络缓存(有网络的时候不缓存，没有网络的时候缓存)
            builder.addInterceptor(new CacheInterceptor(mContext))
                    .addNetworkInterceptor(new CacheInterceptor(mContext))
                    .cache(cache);
            //添加请求头
            client = builder.build();
        }
        return client;
    }

}
