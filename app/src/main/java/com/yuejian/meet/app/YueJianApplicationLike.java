package com.yuejian.meet.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.mcxiaoke.bus.Bus;
import com.meituan.android.walle.WalleChannelReader;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.tinker.loader.app.DefaultApplicationLike;

/**
 * Created by zh02 on 2017/9/13.
 */

public class YueJianApplicationLike extends DefaultApplicationLike {

    public YueJianApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        String channel = WalleChannelReader.getChannel(getApplication());
        Bugly.setAppChannel(getApplication(), channel);
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        Bugly.init(getApplication(), "a9e16ab1a0", true);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker(this);
        Beta.betaPatchListener = new BetaPatchListener(){

            @Override
            public void onPatchReceived(String s) {

            }

            @Override
            public void onDownloadReceived(long l, long l1) {

            }

            @Override
            public void onDownloadSuccess(String s) {
                Log.d("patch", s);
//                Bus.getDefault().post("patch_download_success");
            }

            @Override
            public void onDownloadFailure(String s) {

            }

            @Override
            public void onApplySuccess(String s) {

            }

            @Override
            public void onApplyFailure(String s) {

            }

            @Override
            public void onPatchRollback() {

            }
        };
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

}
