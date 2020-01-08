package com.yuejian.meet.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.api.utils.PreferencesIm;
import com.netease.nim.uikit.api.utils.UtilsIm;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.entity.GiftAllEntity;
import com.netease.nim.uikit.app.entity.MonyEntity;
import com.netease.nim.uikit.app.entity.PriceEnitty;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.app.myenum.ChatEnum;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.app.MyApplication;
import com.yuejian.meet.service.DownloadGiftImgService;
import com.yuejian.meet.session.DemoCache;
import com.yuejian.meet.session.avchat.activity.AVChatActivity;
import com.yuejian.meet.session.config.preference.UserPreferences;
import com.yuejian.meet.session.util.ImUserInfoUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zh03 on 2017/6/30.
 */

public class ImUtils {

    public static int logingCum = 0;//
    public static Boolean isLoginIm = false;

    public static void loginIm() {
        if (AppConfig.userEntity == null) {
            return;
        }
        LoginInfo info = new LoginInfo(AppConfig.CustomerId, AppConfig.userEntity.getToken());
        NIMClient.getService(AuthService.class).login(info).setCallback(callback);
    }

   public static RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
        @Override
        public void onSuccess(LoginInfo param) {
            isLoginIm = true;
            DemoCache.setAccount(param.getAccount());
            NimUIKit.setAccount(param.getAccount());
            logingCum = 0;
            ImUserInfoUtil.loadRecentMessage();
            initNotificationConfig();
            ImMesssageRedDot.CallMessageRigister();
        }

        @Override
        public void onFailed(int code) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    logingCum += 1;
                    if (logingCum < 3) {
//                        loginIm();
                    }
                }
            }, 4000);
        }

        @Override
        public void onException(Throwable exception) {
            FCLoger.debug("-------------------------exe:" + exception.getMessage());
        }
    };

    private static void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        } else {
            // 更新配置
            try {
                NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 单聊启动方法
     *
     * @param context
     * @param customerId ///对方的id
     */
    public static void toP2PCaht(final Context context, final String customerId) {
        AppConfig.isShopP2PChat = false;
        if (AppConfig.userEntity == null) {
            hintLogin(context);
            return;
        }
        NimUIKit.startP2PSession(context, customerId);
    }

    //启动群聊
    public static void toTeamSession(Context context, String teamid, String sponsorId) {
        AppConfig.isShopP2PChat = false;
        AppConfig.sponsorId = sponsorId;
        AppConfig.chatEnum = ChatEnum.defaults;
        NimUIKit.startTeamSession(context, teamid);
    }
    public static void toGroupChat(Context paramContext, String paramString1, ChatEnum paramChatEnum, String paramString2, Boolean paramBoolean)
    {
        AppConfig.sponsorId = paramString2;
        AppConfig.chatEnum = paramChatEnum;
        AppConfig.isShopP2PChat = paramBoolean;
        NimUIKit.startTeamSession(paramContext, paramString1);
    }

    //商店聊天
    public static void toStoreP2PCaht(final Context context, final String customerId) {
        AppConfig.isShopP2PChat = true;
        if (AppConfig.userEntity == null) {
            hintLogin(context);
            return;
        }
        NimUIKit.startP2PSession(context, customerId);
    }

    //商域群聊
    public static void toAssemblyHall(Context context, String teamid, ChatEnum customerid, String sponsorId) {
        AppConfig.sponsorId = sponsorId;
        AppConfig.chatEnum = customerid;
        AppConfig.isShopP2PChat = false;
        NimUIKit.startTeamSession(context, teamid);
    }

    /************************ 音视频通话 ***********************/
    public static void startAudio(final Context context, final String account, final AVChatType avChatType) {
        if (AppConfig.userEntity == null) {
            hintLogin(context);
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("customerId", account);
        params.put("myCustomerId", AppConfig.CustomerId);
        new NetApi().getPrice(account, context, new DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                PriceEnitty priceEnitty = JSON.parseObject(data, PriceEnitty.class);
                MonyEntity monyEntity = JSON.parseObject(data, MonyEntity.class);
                UtilsIm.setMyMoney(context, monyEntity);
                AppConfig.avChatPrice = priceEnitty.getVideo_price();
                if (priceEnitty.getVideo_price() == 0 || UtilsIm.getTimeLong(AppConfig.moneySun + "", AppConfig.avChatPrice) > (60 * 3 * 1000)) {
                    AVChatActivity.launch(context, account, avChatType.getValue(), AVChatActivity.FROM_INTERNAL);
                } else {
                    ViewInject.shortToast(MyApplication.context, "余额不足看四分钟");
                }
            }

            @Override
            public void onSuccess(String data, int id) {

            }

            @Override
            public void onFailed(String errCode, String errMsg) {
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    ///提示登录
    public static void hintLogin(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("赶快去登录吧");
        builder.setMessage("要使用完整功能,要先去登录哦");
        builder.setCancelable(false);
        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ImUtils.isLoginIm=false;
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                AppManager.finishAllActivity();
            }
        });
        builder.show();
    }


    /**
     * 获取礼物表初始化
     */
    public static void giftLoad() {
        new ApiImp().getGiftAll(null, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<GiftAllEntity> list = JSON.parseArray(data, GiftAllEntity.class);
                if ((list != null && !AppConfig.isGiftDownload) || !data.equals(PreferencesIm.get(MyApplication.context, PreferencesIm.gift_image, ""))) {
                    ArrayList<String> listImgURL = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        listImgURL.add(list.get(i).gift_image);
                    }
                    if (listImgURL.size() > 0) {//调用service下载礼物图片
                        Intent service = new Intent(MyApplication.context, DownloadGiftImgService.class);
                        service.putStringArrayListExtra("giftImageList", listImgURL);
                        MyApplication.context.startService(service);
                        PreferencesIm.write(MyApplication.context, PreferencesIm.giftDownload, true);
                    }
                }
                if (list != null)
                    PreferencesIm.put(MyApplication.context, PreferencesIm.gift_image, data);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    public static void setMoney(String gainsSum, String rechargeSum) {
        if (gainsSum.equals("") || rechargeSum.equals("")) {
            ViewInject.shortToast(MyApplication.context, "更新钱包失败");
            return;
        }
    }

    /**
     * 用户被踢监听
     */
    public static void monitorLoginType() {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                new Observer<StatusCode>() {
                    public void onEvent(StatusCode status) {
                        if (status == StatusCode.KICKOUT || status == StatusCode.PWD_ERROR) {//被踢下线
                            logout();
                            Bus.getDefault().post(AppConfig.userKick);
                        }
                    }
                }, true);
    }

    /**
     * 退出登录
     */
    public static void logout() {
//        AppConfig.userEntity = null;
//        PreferencesUtil.put(MyApplication.context, PreferencesUtil.KEY_USER_INFO, "");
//        AppConfig.CustomerId = "";
//        AppConfig.UserSex = null;
//        AppConfig.Token = "";
//        BusCallEntity busEnum=new BusCallEntity();
//        busEnum.setCallType(BusEnum.LOGOUT);
//        Bus.getDefault().post(busEnum);
    }
}
