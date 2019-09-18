package com.yuejian.meet.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.aliyun.common.utils.MySystemParams;
import com.aliyun.demo.recorder.faceunity.FaceUnityManager;
import com.aliyun.downloader.DownloaderManager;
import com.aliyun.sys.AlivcSdkCore;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallListener;
import com.fm.openinstall.model.AppData;
import com.fm.openinstall.model.Error;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.api.utils.PreferencesIm;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.UserEntity;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.mixpush.NIMPushClient;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.service.DownloadGiftImgService;
import com.yuejian.meet.session.DemoCache;
import com.yuejian.meet.session.SessionHelper;
import com.yuejian.meet.session.config.preference.UserPreferences;
import com.yuejian.meet.session.mixpush.DemoMixPushMessageHandler;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.AppinitUtil;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.DadanPreference;
import com.yuejian.meet.utils.FileUtils;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.SystemTool;
import com.yuejian.meet.utils.Utils;
import com.zhy.autolayout.config.AutoLayoutConifg;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by zh03 on 2017/7/20.
 */

public class MyApplication extends TinkerApplication {
    public static Context context;
    public static String chatGiftPath;
    public static float width;
    public static float height;
    public static float density;
    public static float minEdg;
    public static int densityDpi;

    public MyApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.yuejian.meet.app.YueJianApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        AppConfig.context = this;
        OpenInstall.init(this);
        //小米推送
        if (shouldInit()) {
            MiPushClient.registerPush(this, "2882303761517614354", "5901761448354");
            NIMPushClient.registerMiPush(this, "MIYUEJIANCHAT77", "2882303761517614354", "5901761448354");
        }
        initCustomId();
        DemoCache.setContext(context);
        AppUitls.initOkhttpClient();
        AppUitls.UMENG_Config(this);
        AppUitls.JPUSH_Config(this);
        AppinitUtil.initLanguage(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Dexter.initialize(context);
        initNimConfig();
        chatGiftPath = FileUtils.getChatGiftPath(this);
//        ImUtils.giftLoad();
        HashMap<String, Object> params = new HashMap<>();
        params.put("version", Utils.getVersionName(this));
//        new ApiImp().getMallSwitch(params, new DataIdCallback<String>() {
//            @Override
//            public void onSuccess(String data, int id) {
//                try {
//                    JSONObject object = new JSONObject(data);
//                    PreferencesUtil.write(getApplicationContext(), Constants.MALL_SWITCH, "1".equals(object.getString("market_switch")));
//                    PreferencesUtil.write(getApplicationContext(), Constants.UNDONE_SWITCH, "1".equals(object.getString("undone_switch")));
//                    Log.d("switch", data);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailed(String errCode, String errMsg, int id) {
//                Log.d("switch", errMsg);
//            }
//        });
        if (isMainProcess()) {
            initOpenInstall();
        }
        Log.d("TinkerApplicationLike","regId:"+MiPushClient.getRegId(this));

        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "c2a5563620e06621e6d0df6fcb246e44");
        AppUitls.UMENG_Config(this);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        // 初始化阿里云短视频SDK
        com.aliyun.vod.common.httpfinal.QupaiHttpFinal.getInstance().initOkHttpFinal();
        FaceUnityManager.getInstance().setUp(this);
        QupaiHttpFinal.getInstance().initOkHttpFinal();
        DownloaderManager.getInstance().init(this);
        AlivcSdkCore.register(getApplicationContext());
        AlivcSdkCore.setLogLevel(AlivcSdkCore.AlivcLogLevel.AlivcLogDebug);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    private void initNimConfig() {
        // 注册华为推送，参数：华为推送证书名称（需要在云信管理后台配置）
        NIMPushClient.registerHWPush(this, "YUAWEIYUEJIANCHAT77");
        NIMPushClient.registerMixPushMessageHandler(new DemoMixPushMessageHandler());
        NIMClient.init(this, loginInfo(), new ImConfig().getOptions(context));
        if (inMainProcess()) {
            NimUIKit.init(context);
            // 会话窗口的定制初始化。
            SessionHelper.init();
            // 注册网络通话来电
            new ImConfig().registerAVChatIncomingCallObserver(true);
            ImUtils.monitorLoginType();//监听用户登录情况
            new ImUtils().loginIm();//登录im
            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
        }
    }

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo loginInfo() {
        LoginInfo loginInfo = null;
        if (AppConfig.userEntity != null) {
            DemoCache.setAccount(AppConfig.userEntity.getCustomer_id());
            loginInfo = new LoginInfo(AppConfig.userEntity.getCustomer_id(), AppConfig.userEntity.getToken());
        }
        return loginInfo;
    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemTool.getProcessName(this);
        return packageName.equals(processName);
    }

    /**
     * 加载用户信息
     */
    public void initCustomId() {
        setlangage();

        String userData = PreferencesUtil.get(context, PreferencesUtil.KEY_USER_INFO, "");
        if(!CommonUtil.isNull(userData))return;
        UserEntity entity =new Gson().fromJson(userData,UserEntity.class);
        if (!CommonUtil.isNull(entity.getCustomer_id())){
            AppConfig.CustomerId = entity.getCustomer_id();
        }else {
            AppConfig.CustomerId = entity.getCustomerId();
        }
//        AppConfig.CustomerId =DadanPreference.getInstance(this).getString("CustomerId");

//        String userData = DadanPreference.getInstance(this).getString("CustomerId");
//        if (StringUtils.isNotEmpty(userData)) {
//            AppConfig.newUerEntity = CommonUtil.getBeanFromSp(this,"userData","userBean");
//            AppConfig.CustomerId = userData;
////            AppConfig.UserSex = AppConfig.userEntity.getSex();
////            AppConfig.Token = AppConfig.userEntity.getToken();
////            ////获取保存位置
////            AppConfig.city = PreferencesUtil.get(this, PreferencesUtil.CITY, "");
////            AppConfig.district = PreferencesUtil.get(this, PreferencesUtil.DISTRICT, "");
////            AppConfig.slatitude = PreferencesUtil.get(this, PreferencesUtil.LATITUDE, "0");
////            AppConfig.slongitude = PreferencesUtil.get(this, PreferencesUtil.LONGITUDE, "0");
////            AppConfig.province = PreferencesUtil.get(this, PreferencesUtil.PROVINCE, "");
////            AppConfig.moneySun = Double.parseDouble(PreferencesIm.get(this, PreferencesIm.moneySum, "0.0"));
//        }
//        AppConfig.isGiftDownload = PreferencesIm.readBoolean(this, PreferencesIm.giftDownload);
    }

    /**
     * 设置语言
     */
    public void setlangage(){
        if (StringUtil.isEmpty(PreferencesUtil.get(this,PreferencesUtil.Langguage,""))){
            String locale = Locale.getDefault().toString();
            if (locale.equals("zh_TW")){
                PreferencesUtil.put(this,PreferencesUtil.Langguage,"2");
                Resources resources = getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                Configuration config = resources.getConfiguration();
                config.locale = Locale.TRADITIONAL_CHINESE;
                resources.updateConfiguration(config, dm);
                AppConfig.language="2";
            }
        }else {
            Resources resources = getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration config = resources.getConfiguration();
            String name= PreferencesUtil.get(this,PreferencesUtil.Langguage,"1");
            AppConfig.language=name;
            if (name.contains("1")){
                config.locale = Locale.SIMPLIFIED_CHINESE;
            }else {
                config.locale = Locale.TRADITIONAL_CHINESE;
            }
            resources.updateConfiguration(config, dm);
        }
    }

    private void initOpenInstall() {
        OpenInstall.init(this);
        OpenInstall.getInstall(new AppInstallListener() {
            @Override
            public void onInstallFinish(AppData appData, Error error) {
                if (error == null) {
                    if (appData == null) return;
                    //获取自定义数据
                    if (StringUtils.isNotEmpty(appData.getData())) {
                        try {
                            String data = appData.data;
                            String inviteCode = data.substring(data.indexOf(":") + 2, data.length() - 2);
                            Log.d("OpenInstall", "get invite code " + inviteCode);
                            PreferencesUtil.put(getApplicationContext(), AppConfig.INVITE_CODE, inviteCode);
                            inviteCode = PreferencesUtil.get(getApplicationContext(), AppConfig.INVITE_CODE, "");
                            Log.d("OpenInstall", "get invite code from sp: " + inviteCode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d("OpenInstall", "getInstall : bindData = " + appData.getData());
                    }
                } else {
                    Log.e("OpenInstall", "getInstall : errorMsg = " + error.toString());
                }
            }
        });
    }

    public boolean isMainProcess() {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService
                (Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return getApplicationInfo().packageName.equals(appProcess.processName);
            }
        }
        return false;
    }
}
