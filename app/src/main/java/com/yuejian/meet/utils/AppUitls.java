package com.yuejian.meet.utils;

import android.content.Context;

import com.netease.nim.uikit.app.AppConfig;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Created by zh03 on 2017/8/4.
 */

public class AppUitls {
    public final static int CONNECT_TIMEOUT = 60;
    public final static int READ_TIMEOUT = 100;
    public final static int WRITE_TIMEOUT = 60;

    /**
     * 友盟配置
     */
    public static void UMENG_Config(Context context) {
        UMShareAPI.get(context);
        PlatformConfig.setWeixin("wx457b71ba948f85c0", "fd76c66d61469a632f0539937f8d4028");
        PlatformConfig.setQQZone("1106149025", "tnfyqFCYrxHoLlgM");
//        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
        Config.DEBUG = true;
    }

    /**
     * 设置可以请求https
     */
    public static void initOkhttpClient() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS).hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager).build();
        OkHttpUtils.initClient(okHttpClient);
    }

    //极光
    public static void JPUSH_Config(Context context) {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(context);
    }

    public static boolean isLogin() {
        return AppConfig.userEntity == null;
    }

    public static void goToPersonHome(Context context, String opCustomerId) {
//        Intent intent = new Intent(context, WebActivity.class);
//        intent.putExtra("url", Constants.PERSON_HOME + "&customer_id=" + opCustomerId);
//        Intent intent=new Intent(context, PersonHomeActivity.class);
//        intent.putExtra("customer_id",opCustomerId);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
    }
}
