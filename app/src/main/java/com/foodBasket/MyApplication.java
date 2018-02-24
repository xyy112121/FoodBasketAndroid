package com.foodBasket;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.foodBasket.net.HttpsUtil;
import com.foodBasket.net.Tls12SocketFactory;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.OkHttpClient;

/**
 * Created by programmer on 2017/12/20.
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    public String mImageUrl = "http://120.78.254.71/Basket/";


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Logger.addLogAdapter(new AndroidLogAdapter());// 初始化logger
        Utils.init(instance);
        initNet();
    }

    public static MyApplication getApplication() {
        return instance;
    }

    public static RequestOptions getOptions() {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.icon_default)
                .error(R.mipmap.icon_default)
                .priority(Priority.HIGH);
        return options;
    }

    //网络初始化配置
    private void initNet() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);

            SSLSocketFactory socketFactory = new Tls12SocketFactory(sslContext.getSocketFactory());
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                    .sslSocketFactory(socketFactory, new HttpsUtil.UnSafeTrustManager())
                    .readTimeout(10000L, TimeUnit.MILLISECONDS)
                    //其他配置
                    .build();
            //设置全局公共参数
            OkHttpUtils.initClient(okHttpClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
