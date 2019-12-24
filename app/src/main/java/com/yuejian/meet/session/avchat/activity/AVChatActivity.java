package com.yuejian.meet.session.avchat.activity;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.api.utils.CountDownTimerUtils;
import com.netease.nim.uikit.api.utils.UtilsIm;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.ExpenseEntity;
import com.netease.nim.uikit.app.entity.GiftAllEntity;
import com.netease.nim.uikit.app.entity.MonyEntity;
import com.netease.nim.uikit.app.entity.PriceEnitty;
import com.netease.nim.uikit.app.extension.ChatGiftAttachment;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nim.uikit.session.module.Container;
import com.netease.nim.uikit.session.module.ModuleProxy;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
import com.netease.nimlib.sdk.avchat.constant.AVChatControlCommand;
import com.netease.nimlib.sdk.avchat.constant.AVChatEventType;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatControlEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatOnlineAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.find.AvChatServiceInfoActivity;
import com.yuejian.meet.activities.mine.InCashActivity;
import com.yuejian.meet.bean.AvChatConsumptionEntity;
import com.yuejian.meet.bean.animation.GiftAnmManager;
import com.yuejian.meet.bsr.BSRGiftLayout;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.service.ScreenAndLockService;
import com.yuejian.meet.session.avchat.AVChatNotification;
import com.yuejian.meet.session.avchat.AVChatProfile;
import com.yuejian.meet.session.avchat.AVChatSoundPlayer;
import com.yuejian.meet.session.avchat.AVChatUI;
import com.yuejian.meet.session.avchat.AVChatVideo;
import com.yuejian.meet.session.avchat.constant.CallStateEnum;
import com.yuejian.meet.session.avchat.constant.ChatMsgType;
import com.yuejian.meet.session.avchat.receiver.PhoneCallStateObserver;
import com.yuejian.meet.session.panel.MessageListPanel;
import com.yuejian.meet.session.util.GiftAttachment;
import com.yuejian.meet.session.widgets.listener.SoftKeyBoardListener;
import com.yuejian.meet.utils.FCLoger;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.SystemTool;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.CustomEditText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 音视频界面
 * Created by hzxuwen on 2015/4/21.
 */
public class AVChatActivity extends UI implements AVChatUI.AVChatListener, AVChatStateObserver, View.OnClickListener, ModuleProxy {
    // constant
    private static final String TAG = "AVChatActivity";
    private static final String KEY_IN_CALLING = "KEY_IN_CALLING";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";
    private static final String KEY_CALL_TYPE = "KEY_CALL_TYPE";
    private static final String KEY_SOURCE = "source";
    private static final String KEY_CALL_CONFIG = "KEY_CALL_CONFIG";
    public static final String INTENT_ACTION_AVCHAT = "INTENT_ACTION_AVCHAT";

    /**
     * 来自广播
     */
    public static final int FROM_BROADCASTRECEIVER = 0;
    /**
     * 来自发起方
     */
    public static final int FROM_INTERNAL = 1;
    /**
     * 来自通知栏
     */
    public static final int FROM_NOTIFICATION = 2;
    /**
     * 未知的入口
     */
    public static final int FROM_UNKNOWN = -1;
    // face unity
//    private FaceU faceU;

    // data
    private AVChatUI avChatUI; // 音视频总管理器
    private AVChatData avChatData; // config for connect video server
    private int state; // calltype 音频或视频
    private String receiverId = ""; // 对方的account
    //文字发送界面
    @Nullable
    @Bind(R.id.rlayout_video_accept_chat)
    RelativeLayout mRlayoutVideoChat;
    //文字消息输入框
    @Nullable
    @Bind(R.id.c_edit_txt_msg)
    CustomEditText mCEditTxtMsg;
    //底部视频通话控制按钮
    @Nullable
    @Bind(R.id.rl_surface_bottom_bar_btn)
    LinearLayout mRlSurfaceBottomBar;
    @Bind(R.id.avchat_video_record)
    ImageView avchat_video_record;
    RelativeLayout call_notificationLayout;
    private View mRootView;
    private String mChatTheme;
    private String name = "某用户";
    NimUserInfo userInfo;
//    private BSRGiftLayout bsrGiftLayout;
    private GiftAnmManager giftAnmManager;

    //消息收发面板
    private MessageListPanel messageListPanel;
    private boolean isKeyBoardShow = false;
    private String comingAccount = "";
    NetApi api = new NetApi();
    public static Boolean isAvChatHit = false;
    public int avChatMoney = 180;
    CountDownTimerUtils timeUtils;
//    PriceEnitty mPriceEntity;
    // state
    private boolean isUserFinish = false;
    private boolean mIsInComingCall = false;// is incoming call or outgoing call
    private boolean isCallEstablished = false; // 电话是否接通
    private static boolean needFinish = true; // 若来电或去电未接通时，点击home。另外一方挂断通话。从最近任务列表恢复，则finish
    private boolean hasOnPause = false; // 是否暂停音视频


    // notification
    private AVChatNotification notifier;

    public static void launch(Context context, String account, int callType, int source) {
        needFinish = false;
        isAvChatHit = true;
        Intent intent = new Intent();
        intent.setClass(context, AVChatActivity.class);
        intent.putExtra(KEY_ACCOUNT, account);
        intent.putExtra(KEY_IN_CALLING, false);
        intent.putExtra(KEY_CALL_TYPE, callType);
        intent.putExtra(KEY_SOURCE, source);
        context.startActivity(intent);
    }

    /**
     * incoming call
     *
     * @param context
     */
    public static void launch(Context context, AVChatData config, int source) {
        needFinish = false;
        isAvChatHit = false;
        Intent intent = new Intent();
        intent.setClass(context, AVChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_CALL_CONFIG, config);
        intent.putExtra(KEY_IN_CALLING, true);
        intent.putExtra(KEY_SOURCE, source);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (needFinish || !checkSource()) {
            finish();
            return;
        }
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

        KeyguardManager mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        //true为打开，false为关闭  判断手机是否亮屏
        boolean ifOpen = powerManager.isScreenOn();
        if (!ifOpen || mKeyguardManager.inKeyguardRestrictedInputMode()) {
            Intent intent = new Intent(this, ScreenAndLockService.class);
            startService(intent);
        }


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Bus.getDefault().register(this);
        mRootView = LayoutInflater.from(this).inflate(R.layout.avchat_activity, null);
        setContentView(mRootView);
        ButterKnife.bind(this);
        avchat_video_record = (ImageView) findViewById(R.id.avchat_video_record);
        call_notificationLayout= (RelativeLayout) mRootView.findViewById(R.id.call_notificationLayout);
        avchat_video_record.setOnClickListener(this);
        mIsInComingCall = getIntent().getBooleanExtra(KEY_IN_CALLING, false);
        avChatUI = new AVChatUI(this, mRootView, this);
        if (!avChatUI.initiation()) {
            this.finish();
            return;
        }

        registerNetCallObserver(true);
        if (mIsInComingCall) {
            inComingCalling();
        } else {
            call_notificationLayout.setVisibility(View.VISIBLE);
            outgoingCalling();
        }

        notifier = new AVChatNotification(this);
        if (receiverId.equals(""))
            receiverId = avChatData.getAccount();
        AppConfig.videoToCustomerId = receiverId;
        AppConfig.mAvchattime = Long.parseLong("0");
        notifier.init(receiverId != null ? receiverId : avChatData.getAccount());
        isCallEstablished = false;
        //放到所有UI的基类里面注册，所有的UI实现onKickOut接口
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, true);
        if (getIntent().getStringExtra(Constants.AVCHAT_KEYS.KEY_CHAT_THEME) != null) {
            mChatTheme = getIntent().getStringExtra(Constants.AVCHAT_KEYS.KEY_CHAT_THEME);
        }
        initMessageListInfo();
        softKeyboardListener();
        new AVChatVideo().initgift(gift);//礼物回调接口
//        bsrGiftLayout = (BSRGiftLayout) findViewById(R.id.gift_layout);
//        giftAnmManager = new GiftAnmManager(bsrGiftLayout, container.activity);//礼物动态
////////

        userInfo = NimUserInfoCache.getInstance().getUserInfo(receiverId);
        // 打开音频
        AVChatManager.getInstance().muteLocalAudio(false);
    }

    /**
     * 初始化聊天列表信息
     */
    private void initMessageListInfo() {
//        if (mIsInComingCall) {
//            container = new Container(this, comingAccount, SessionTypeEnum.P2P, this);
//        } else {
        container = new Container(this, receiverId, SessionTypeEnum.P2P, this);
//        }
        messageListPanel = new MessageListPanel(container, mRootView, mChatTheme, name);//消息发送处理类
//        inputPanel = new InputPanel(container, mRootView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        avChatUI.pauseVideo(); // 暂停视频聊天（用于在视频聊天过程中，APP退到后台时必须调用）
        hasOnPause = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        activeCallingNotifier();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cancelCallingNotifier();
        if (hasOnPause) {
            avChatUI.resumeVideo();
            hasOnPause = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppConfig.videoToCustomerId="";
        Intent intent = new Intent(this, ScreenAndLockService.class);
        stopService(intent);
        Bus.getDefault().unregister(this);
        AppConfig.mAvchattime = Long.parseLong("0");
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, false);
        AVChatProfile.getInstance().setAVChatting(false);
        registerNetCallObserver(false);
        cancelCallingNotifier();
        ButterKnife.unbind(this);
        registerObservers(false);
        messageListPanel.onDestroy();
        gift=null;
        avChatUI=null;
        needFinish = true;
        if (timeUtils != null)
            timeUtils.cancel();
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * 判断来电还是去电
     *
     * @return
     */
    private boolean checkSource() {
        registerObservers(true);
        switch (getIntent().getIntExtra(KEY_SOURCE, FROM_UNKNOWN)) {
            case FROM_BROADCASTRECEIVER: // incoming call
                AppConfig.avChatPrice = Long.parseLong("0");
                parseIncomingIntent();
                return true;
            case FROM_INTERNAL: // outgoing call
                parseOutgoingIntent();
                if (state == AVChatType.VIDEO.getValue() || state == AVChatType.AUDIO.getValue()) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    /**
     * 来电参数解析
     */
    private void parseIncomingIntent() {
        avChatData = (AVChatData) getIntent().getSerializableExtra(KEY_CALL_CONFIG);
        AppConfig.getChatId = avChatData.getChatId() + "";
        state = avChatData.getChatType().getValue();
    }

    /**
     * 去电参数解析
     */
    private void parseOutgoingIntent() {
        receiverId = getIntent().getStringExtra(KEY_ACCOUNT);
        state = getIntent().getIntExtra(KEY_CALL_TYPE, -1);
    }

    /**
     * 注册监听
     *
     * @param register
     */
    private void registerNetCallObserver(boolean register) {
        AVChatManager.getInstance().observeAVChatState(this, register);
        AVChatManager.getInstance().observeCalleeAckNotification(callAckObserver, register);
        AVChatManager.getInstance().observeControlNotification(callControlObserver, register);
        AVChatManager.getInstance().observeHangUpNotification(callHangupObserver, register);
        AVChatManager.getInstance().observeOnlineAckNotification(onlineAckObserver, register);
        AVChatManager.getInstance().observeTimeoutNotification(timeoutObserver, register);
        PhoneCallStateObserver.getInstance().observeAutoHangUpForLocalPhone(autoHangUpForLocalPhoneObserver, register);
    }

    /**
     * 注册/注销网络通话被叫方的响应（接听、拒绝、忙）
     */
    Observer<AVChatCalleeAckEvent> callAckObserver = new Observer<AVChatCalleeAckEvent>() {
        @Override
        public void onEvent(AVChatCalleeAckEvent ackInfo) {

            AVChatSoundPlayer.instance().stop();

            if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_BUSY) {

                AVChatSoundPlayer.instance().play(AVChatSoundPlayer.RingerTypeEnum.PEER_BUSY);
                avChatUI.closeRtc();
                avChatUI.closeSessions(AVChatExitCode.PEER_BUSY);
            } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_REJECT) {
                avChatUI.closeRtc();
                avChatUI.closeSessions(AVChatExitCode.REJECT);
            } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_AGREE) {
                avChatUI.isCallEstablish.set(true);
                avChatUI.canSwitchCamera = true;
                call_notificationLayout.setVisibility(View.GONE);
                //////接通通话发送状态给服务器
                Map<String, String> params = new HashMap<>();
                params.put("customer_id", AppConfig.CustomerId);
                params.put("op_customer_id", receiverId);
                params.put("channel_id", AppConfig.getChatId + "");
                api.startVideo(params, this, new DataCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                avChatUI.closeCamera();
//                            }
//                        },1500);
                    }

                    @Override
                    public void onSuccess(String data, int id) {

                    }

                    @Override
                    public void onFailed(String errCode, String errMsg) {
                        avChatUI.hangUp(AVChatExitCode.HANGUP);
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg, int id) {

                    }
                });
                if (AppConfig.avChatPrice > 0)
                    setTime();
//                    isBalance();
            }
        }
    };


    Observer<Long> timeoutObserver = new Observer<Long>() {
        @Override
        public void onEvent(Long chatId) {

            AVChatData info = avChatUI.getAvChatData();
            if (info != null && info.getChatId() == chatId) {

                avChatUI.closeRtc();
                avChatUI.closeSessions(AVChatExitCode.PEER_NO_RESPONSE);

                // 来电超时，自己未接听
                if (mIsInComingCall) {
                    activeMissCallNotifier();
                }

                AVChatSoundPlayer.instance().stop();
            }

        }
    };

    Observer<Integer> autoHangUpForLocalPhoneObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer integer) {

            AVChatSoundPlayer.instance().stop();

            avChatUI.closeRtc();
            avChatUI.closeSessions(AVChatExitCode.PEER_BUSY);
        }
    };

    /**
     * 注册/注销网络通话控制消息（音视频模式切换通知）
     */
    Observer<AVChatControlEvent> callControlObserver = new Observer<AVChatControlEvent>() {
        @Override
        public void onEvent(AVChatControlEvent netCallControlNotification) {
            handleCallControl(netCallControlNotification);
        }
    };

    /**
     * 注册/注销网络通话对方挂断的通知
     */
    Observer<AVChatCommonEvent> callHangupObserver = new Observer<AVChatCommonEvent>() {
        @Override
        public void onEvent(AVChatCommonEvent avChatHangUpInfo) {
            ///通话结束上传服务
            api.endVideo(AppConfig.getChatId, this, new DataCallback<String>() {
                @Override
                public void onSuccess(String data) {
//                    if (AppConfig.UserSex.equals("1")){
                    AvChatConsumptionEntity entity = JSON.parseObject(data, AvChatConsumptionEntity.class);
                    Intent intent = new Intent(getApplicationContext(), AvChatServiceInfoActivity.class);
                    intent.putExtra("duration", entity.getDuration());
                    intent.putExtra("cost", entity.cost);
                    intent.putExtra("video_id", entity.getVideo_id());
                    intent.putExtra("isMiStart", isAvChatHit);
                    intent.putExtra("opCustomerId", receiverId);
                    startActivity(intent);
//                    }
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
            AVChatSoundPlayer.instance().stop();

            avChatUI.closeRtc();
            avChatUI.closeSessions(AVChatExitCode.HANGUP);
            cancelCallingNotifier();
            // 如果是incoming call主叫方挂断，那么通知栏有通知
            if (mIsInComingCall && !isCallEstablished) {
                activeMissCallNotifier();
            }
        }
    };

    /**
     * 注册/注销同时在线的其他端对主叫方的响应
     */
    Observer<AVChatOnlineAckEvent> onlineAckObserver = new Observer<AVChatOnlineAckEvent>() {
        @Override
        public void onEvent(AVChatOnlineAckEvent ackInfo) {

            AVChatSoundPlayer.instance().stop();

            String client = null;
            switch (ackInfo.getClientType()) {
                case ClientType.Web:
                    client = "Web";
                    break;
                case ClientType.Windows:
                    client = "Windows";
                    break;
                case ClientType.Android:
                    client = "Android";
                    break;
                case ClientType.iOS:
                    client = "iOS";
                    break;
                default:
                    break;
            }
            if (client != null) {
                String option = ackInfo.getEvent() == AVChatEventType.CALLEE_ONLINE_CLIENT_ACK_AGREE ? "接听！" : "拒绝！";
                Toast.makeText(AVChatActivity.this, "通话已在" + client + "端被" + option, Toast.LENGTH_SHORT).show();
            }
            avChatUI.closeRtc();
            avChatUI.closeSessions(-1);
        }
    };


    /**
     * 接听
     */
    private void inComingCalling() {
        avChatUI.inComingCalling(avChatData);
    }

    /**
     * 拨打
     */
    private void outgoingCalling() {
        if (!NetworkUtil.isNetAvailable(AVChatActivity.this)) { // 网络不可用
            Toast.makeText(this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        avChatUI.outGoingCalling(receiverId, AVChatType.typeOfValue(state));
    }

    /**
     * *************************** AVChatListener *********************************
     */

    @Override
    public void uiExit() {
        finish();
    }


    /****************************** 连接建立处理 ********************/

    /**
     * 处理连接服务器的返回值
     *
     * @param auth_result
     */
    protected void handleWithConnectServerResult(int auth_result) {
        LogUtil.i(TAG, "result code->" + auth_result);
        if (auth_result == 200) {
            Log.d(TAG, "onConnectServer success");
        } else if (auth_result == 101) { // 连接超时
            avChatUI.closeRtc();
            avChatUI.closeSessions(AVChatExitCode.PEER_NO_RESPONSE);
        } else if (auth_result == 401) { // 验证失败
            avChatUI.closeRtc();
            avChatUI.closeSessions(AVChatExitCode.CONFIG_ERROR);
        } else if (auth_result == 417) { // 无效的channelId
            avChatUI.closeRtc();
            avChatUI.closeSessions(AVChatExitCode.INVALIDE_CHANNELID);
        } else { // 连接服务器错误，直接退出
            avChatUI.closeRtc();
            avChatUI.closeSessions(AVChatExitCode.CONFIG_ERROR);
        }
    }

    /**************************** 处理音视频切换 *********************************/

    /**
     * 处理音视频切换请求
     *
     * @param notification
     */
    private void handleCallControl(AVChatControlEvent notification) {
        switch (notification.getControlCommand()) {
            case AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO:
                avChatUI.incomingAudioToVideo();
                break;
            case AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO_AGREE:
                onAudioToVideo();
                break;
            case AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO_REJECT:
                avChatUI.onCallStateChange(CallStateEnum.AUDIO);
                Toast.makeText(AVChatActivity.this, R.string.avchat_switch_video_reject, Toast.LENGTH_SHORT).show();
                break;
            case AVChatControlCommand.SWITCH_VIDEO_TO_AUDIO:
                onVideoToAudio();
                break;
            case AVChatControlCommand.NOTIFY_VIDEO_OFF:
                avChatUI.peerVideoOff();
                break;
            case AVChatControlCommand.NOTIFY_VIDEO_ON:
                avChatUI.peerVideoOn();
                break;
            default:
//                Toast.makeText(this, "对方发来指令值：" + notification.getControlCommand(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 音频切换为视频
     */
    private void onAudioToVideo() {
        avChatUI.onAudioToVideo();
        avChatUI.initAllSurfaceView(avChatUI.getVideoAccount());
    }

    /**
     * 视频切换为音频
     */
    private void onVideoToAudio() {
        avChatUI.onCallStateChange(CallStateEnum.AUDIO);
        avChatUI.onVideoToAudio();
    }

    /**
     * 通知栏
     */
    private void activeCallingNotifier() {
        if (notifier != null && !isUserFinish) {
            notifier.activeCallingNotification(true);
        }
    }

    private void cancelCallingNotifier() {
        if (notifier != null) {
            notifier.activeCallingNotification(false);
        }
    }

    private void activeMissCallNotifier() {
        if (notifier != null) {
            notifier.activeMissCallNotification(true);
        }
    }

    @Override
    public void finish() {
        isUserFinish = true;
        super.finish();
    }


    /**
     * ************************ AVChatStateObserver ****************************
     */

    @Override
    public void onTakeSnapshotResult(String account, boolean success, String file) {

    }

    @Override
    public void onConnectionTypeChanged(int netType) {

    }

    @Override
    public void onAVRecordingCompletion(String account, String filePath) {
        if (account != null && filePath != null && filePath.length() > 0) {
            String msg = "音视频录制已结束, " + "账号：" + account + " 录制文件已保存至：" + filePath;
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "录制已结束.", Toast.LENGTH_SHORT).show();
        }
        if (avChatUI != null) {
            avChatUI.resetRecordTip();
        }
    }

    @Override
    public void onAudioRecordingCompletion(String filePath) {
        if (filePath != null && filePath.length() > 0) {
            String msg = "音频录制已结束, 录制文件已保存至：" + filePath;
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "录制已结束.", Toast.LENGTH_SHORT).show();
        }
        if (avChatUI != null) {
            avChatUI.resetRecordTip();
        }
    }

    @Override
    public void onLowStorageSpaceWarning(long availableSize) {
        if (avChatUI != null) {
            avChatUI.showRecordWarning();
        }
    }


    @Override
    public void onFirstVideoFrameAvailable(String account) {

    }

    @Override
    public void onVideoFpsReported(String account, int fps) {

    }

    @Override
    public void onJoinedChannel(int code, String audioFile, String videoFile, int i) {
        handleWithConnectServerResult(code);
    }

    @Override
    public void onLeaveChannel() {

    }

    @Override
    public void onUserJoined(String account) {
        Log.d(TAG, "onUserJoin -> " + account);
        avChatUI.setVideoAccount(account);
        avChatUI.initLargeSurfaceView(avChatUI.getVideoAccount());
    }

    @Override
    public void onUserLeave(String account, int event) {
        Log.d(TAG, "onUserLeave -> " + account);
    }

    @Override
    public void onProtocolIncompatible(int status) {

    }

    @Override
    public void onDisconnectServer() {

    }

    @Override
    public void onNetworkQuality(String user, int quality, AVChatNetworkStats stats) {

    }

    @Override
    public void onCallEstablished() {
        Log.d(TAG, "onCallEstablished");
        if (avChatUI.getTimeBase() == 0)
            avChatUI.setTimeBase(SystemClock.elapsedRealtime());

        if (state == AVChatType.AUDIO.getValue()) {
            avChatUI.onCallStateChange(CallStateEnum.AUDIO);
        } else {
            avChatUI.initSmallSurfaceView();
            avChatUI.onCallStateChange(CallStateEnum.VIDEO);
        }
        isCallEstablished = true;
    }

    @Override
    public void onDeviceEvent(int code, String desc) {

    }

    @Override
    public void onFirstVideoFrameRendered(String user) {

    }

    @Override
    public void onVideoFrameResolutionChanged(String user, int width, int height, int rotate) {

    }

    @Override
    public boolean onVideoFrameFilter(AVChatVideoFrame frame, boolean maybeDualInput) {
//        if (faceU != null) {
//            faceU.effect(frame.data, frame.width, frame.height, FaceU.VIDEO_FRAME_FORMAT.I420);
//        }
        return true;
    }

    @Override
    public boolean onAudioFrameFilter(AVChatAudioFrame frame) {
        return true;
    }

    @Override
    public void onAudioDeviceChanged(int device) {

    }

    @Override
    public void onReportSpeaker(Map<String, Integer> speakers, int mixedEnergy) {

    }

    @Override
    public void onAudioMixingEvent(int event) {

    }

    @Override
    public void onSessionStats(AVChatSessionStats sessionStats) {

    }

    @Override
    public void onLiveEvent(int event) {

    }


    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                AVChatSoundPlayer.instance().stop();
                finish();
            }
        }
    };

    @OnClick({R.id.imgBtn_online_keyboard, R.id.txt_btn_send_info, R.id.avchat_video_record})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_online_keyboard://切换到文字输入
                showKeyBoard();
                break;
            case R.id.txt_btn_send_info://发送文字消息
                onTextMessageSendButtonPressed();
                break;
            case R.id.avchat_video_record://发送礼物
//                sendGift();
                break;
        }
    }

    //礼物发送回调
//    private void sendGift(){
//        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
//        if (editNameDialog==null)
//            editNameDialog = new GiftDialogFragment();
//        editNameDialog.show(fm,"");
//        editNameDialog.setOnSendGiftLister(new GiftDialogFragment.OnSendGiftListener() {
//            @Override
//            public void sendGift(GiftAllEntity giftBean) {
//                if (editNameDialog!=null)
//                    editNameDialog.dismiss();
//                sendGiftInVideo(giftBean);
//            }
//        });
//    }
    //发送礼物
    public AVChatVideo.sendGiftEntity gift = new AVChatVideo.sendGiftEntity() {
        @Override
        public void setGif(GiftAllEntity giftBean) {
            isEndGift(giftBean);
        }
    };

    ///判断是否能发礼物
    public void isEndGift(final GiftAllEntity giftBean) {
        if (AppConfig.avChatPrice > 0) {//判断视频是否收费  如果是要判断是否够钱发礼物
            ///查询账户余额还有多少钱
            api.getBal(AppConfig.CustomerId, this, new DataCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    MonyEntity monyEntity = JSON.parseObject(data, MonyEntity.class);
                    if (monyEntity == null) {
                        ViewInject.shortToast(getApplication(), "更新钱包失败");
                        return;
                    }
                    UtilsIm.setMyMoney(AVChatActivity.this, monyEntity);

                    Long pursueTime = UtilsIm.getTimeLong((AppConfig.moneySun - (giftBean.getGift_price() * Integer.parseInt(giftBean.getCount()))) + "", AppConfig.avChatPrice);///算总的金额减去礼物的钱还乘下多少豪秒
                    long pursueTimes = ((pursueTime / (60 * 1000)));///算还乘下多少分钟
                    if (pursueTimes > 3) {///当发送礼物之后还还可以聊三分钟以上就可以发送礼物
                        sendGift(giftBean, AppConfig.CustomerId, receiverId);
                    } else {
                        showDialog("余额不足，请充值");
//                        ViewInject.shortToast(getApplication(),"余额不足，请充值");
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
        } else {//女方发礼物
            ///查询账户余额还有多少钱
            api.getBal(AppConfig.CustomerId, this, new DataCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    MonyEntity monyEntity = JSON.parseObject(data, MonyEntity.class);
                    if (monyEntity == null || monyEntity.getRecharge_bal() == null) {
                        ViewInject.shortToast(getApplication(), "更新钱包失败");
                        return;
                    }
                    Double moneySum = Double.parseDouble(monyEntity.getGains_bal()) + Double.parseDouble(monyEntity.getRecharge_bal());
                    if ((moneySum - (giftBean.getGift_price() * Integer.parseInt(giftBean.getCount()))) > 0) {///判断女方的余额是否够发送礼物
                        sendGift(giftBean, AppConfig.CustomerId, receiverId);
                    } else {
                        showDialog("余额不足，请充值");
//                        ViewInject.shortToast(getApplication(),"余额不足，请充值");
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
    }

    ////余额不足提示
    public void showDialog(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("充值提示");
        builder.setMessage(content);
        builder.setNegativeButton("取消", null);
        builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                dialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(getApplication(), InCashActivity.class);
                startActivity(intent);
            }
        });
        builder.show();
    }

    /**
     * 发送礼物
     * *customerId   自己的id
     * opCustomerId  对方的id
     * giftId          礼物id
     */
    public void sendGift(final GiftAllEntity giftBean, final String customerId, String opCustomerId) {
        Map<String, String> params = new HashMap<>();
        params.put("customer_id", customerId);
        params.put("op_customer_id", opCustomerId);
        params.put("gift_id", giftBean.getId());
        params.put("gift_count", giftBean.getCount());
        params.put("gift_expense_type", "4");
        api.sendGift(params, this, new DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                sendGiftInVideo(giftBean);
                ExpenseEntity expenseEntity = JSON.parseObject(data, ExpenseEntity.class);
                MonyEntity entity = JSON.parseObject(expenseEntity.getBal(), MonyEntity.class);
                UtilsIm.setMyMoney(AVChatActivity.this, entity);
                if (AppConfig.avChatPrice > 0) {///男方发送完礼物之后重新计时
                    setTime();
//                    isBalance();
                }
            }

            @Override
            public void onSuccess(String data, int id) {

            }

            @Override
            public void onFailed(String errCode, String errMsg) {
                ViewInject.shortToast(getApplication(), errMsg);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    /// 获取女主播视频价格并计算乘下多少分钟
//    public void isBalance() {
//        if (isAvChatHit) {
//            Double timesun = AppConfig.moneySun / avChatMoney;
////            timeUtils=new CountDownTimerUtils(6000,1000);
//            api.getPrice(receiverId, this, new DataCallback<String>() {
//                @Override
//                public void onSuccess(String data) {
//                    mPriceEntity = JSON.parseObject(data, PriceEnitty.class);
//                    if (mPriceEntity == null || mPriceEntity.getVideo_price() == 0) {
//                        ViewInject.toast("价格未设置", getApplication());
//                        return;
//                    }
//                    setTime();
//                }
//
//                @Override
//                public void onFailed(String errCode, String errMsg) {
//                }
//            });
//        }
//    }

    ////设置视频通话倒计时
    public void setTime() {
        if (AppConfig.avChatPrice == 0) {
            return;
        }
        if (timeUtils != null) {
            timeUtils.cancel();
            timeUtils = null;
        }
        Long avTime = UtilsIm.getTimeLong(AppConfig.moneySun + "", AppConfig.avChatPrice) - (10 * 1000);
        Log.d(AppConfig.moneySun + "-----------------time:", avTime + "");
        timeUtils = new CountDownTimerUtils(avTime, 1000);
        timeUtils.setTimeCall(timeCall);
        timeUtils.start();
    }

    //倒计时回调
    public CountDownTimerUtils.TimeRockon timeCall = new CountDownTimerUtils.TimeRockon() {
        @Override
        public void TimeCall(Boolean isFinishCall) {
            avChatUI.hangUp(AVChatExitCode.HANGUP);
            Toast.makeText(getApplicationContext(), "时间到，关闭视频", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void TimeHintCall() {
            //乘下三分钟提示
            showDialog("余额乘下三分钟，请及时充值");
        }
    };

    //处理回调
    @BusReceiver
    public void onStringEvent(String event) {
        // 这里处理事件
        if (event.equals(AppConfig.updateMoney)) {
            setTime();////重新计算视频时长
        } else if ("close_camera".equals(event)) {
            avChatUI.toggleCloseCamera(true);
        } else if ("open_camera".equals(event)) {
            avChatUI.toggleCloseCamera(false);
        }else if ("connect_video".equals(event)){//对方接通视频
            //////接通通话发送状态给服务器
            Map<String, String> params = new HashMap<>();
            params.put("customer_id", receiverId);
            params.put("op_customer_id", AppConfig.CustomerId );
            params.put("channel_id", AppConfig.getChatId + "");
            new NetApi().startVideo(params, this, new DataCallback<String>() {
                @Override
                public void onSuccess(String data) {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                avChatUI.closeCamera();
//                            }
//                        },1500);
                }

                @Override
                public void onSuccess(String data, int id) {

                }

                @Override
                public void onFailed(String errCode, String errMsg) {
                    avChatUI.hangUp(AVChatExitCode.HANGUP);
                }

                @Override
                public void onFailed(String errCode, String errMsg, int id) {

                }
            });
        }
    }

    /**
     * 初始化余额信息
     */
//    private void initBalanceInfo() {
//        try {
//            mMinPrice = Double.parseDouble(postPrice) / 60;
//        } catch (Exception e) {
//
//        }
//    }
//    private CustomerBean mCustomerBean;
    private double remainTime;
    private double mMinPrice;

    private void sendGiftInVideo(GiftAllEntity giftBean) {
        ChatGiftAttachment redPacket = new ChatGiftAttachment();
        redPacket.setData(stringToJson(giftBean));
//        GiftAttachment giftAttachment = new GiftAttachment("gift", giftBean.id);
        IMMessage mIMMessage = MessageBuilder.createCustomMessage(receiverId, SessionTypeEnum.P2P, "[礼物]", redPacket);
        mIMMessage.setRemoteExtension(setExtra(giftBean.id, giftBean.gift_price, giftBean.getGift_name()));
        CustomMessageConfig customMessageConfig = new CustomMessageConfig();
        customMessageConfig.enableHistory = false;//该消息是否允许在消息历史中拉取
        customMessageConfig.enableRoaming = false;//该消息是否需要漫游
        customMessageConfig.enableUnreadCount = false;
        mIMMessage.setConfig(customMessageConfig);
        Map<String, Object> data = new HashMap<>();
        data.put("MsgType", ChatMsgType.GIFT_MSG);
        data.put("giftId", giftBean.id);
        data.put("giftName", giftBean.getGift_name());
        data.put("giftPrice", giftBean.getGift_price());
        data.put("giftImage", giftBean.getGift_image());
        mIMMessage.setStatus(MsgStatusEnum.read);
        CustomMessageConfig customMessageConfig1 = new CustomMessageConfig();
        customMessageConfig1.enablePush = false;
        customMessageConfig1.enableUnreadCount = false;
        mIMMessage.setConfig(customMessageConfig1);
        mIMMessage.setPushPayload(data);
        if (userInfo == null) {
            userInfo = NimUserInfoCache.getInstance().getUserInfo(receiverId);
            if(userInfo==null){
                ViewInject.toast(this, getString(R.string.txt_send_gift_failed));
                return;
            }
        }
        mIMMessage.setPushContent(String.format(StringUtils.getResStr(this, R.string.live_send_pay_tips), userInfo.getName(), giftBean.gift_name + " (-" + giftBean.gift_price));
        if (container.proxy.sendMessage(mIMMessage)) {
            messageListPanel.receiveGiftMsg(mIMMessage);
            FCLoger.debug("send gift success");
            Long price = giftBean.gift_price;
        }
    }

    public JSONObject stringToJson(final GiftAllEntity giftEntity) {
        GiftAllEntity entity = new GiftAllEntity();
        entity.setGift_image(giftEntity.getGift_image());
        entity.setGift_name(giftEntity.getGift_name());
        entity.setGift_count(Long.parseLong(giftEntity.getCount()));
        entity.setGift_id(giftEntity.getGift_id());
        entity.setGift_price(giftEntity.getGift_price());
        return (JSONObject) JSON.toJSON(entity);
    }

    ;

    private static int GIFT = 9;//("9", "礼物")

    private Map<String, Object> setExtra(String giftId, Long giftPrice, String giftName) {//创建消息的时候put extra
        Map<String, Object> extra = new HashMap<>();
        extra.put("isPay", "1");//0不付费  1付费
        extra.put("type", GIFT + "");
        extra.put("giftId", giftId);
        extra.put("giftName", giftName);
        extra.put("giftPrice", giftPrice);
        return extra;
    }

    //消息编辑发送面板
//    private InputPanel inputPanel;
    private Container container;

    /**
     * 发送文本消息
     */
    private void onTextMessageSendButtonPressed() {
        String text = mCEditTxtMsg.getText().toString();
        if (TextUtils.isEmpty(text)) {
            ViewInject.toast(StringUtils.getResStr(container.activity, R.string.txt_no_send_empty_msg), container.activity);
            return;
        }
        IMMessage textMessage = createTextMessage(text);
//        sendMessages(textMessage);
        if (container.proxy.sendMessage(textMessage)) {
            restoreText(true);
        }
    }

    /**
     * 发送消息
     *
     * @param imMessage
     */
    public void sendMessages(IMMessage imMessage) {
        NIMClient.getService(MsgService.class).sendMessage(imMessage, false);
    }

    /**
     * 发送成功后清空消息
     *
     * @param clearText
     */
    private void restoreText(boolean clearText) {
        if (clearText) {
            mCEditTxtMsg.setText("");
        }
    }

    /**
     * 创建消息
     *
     * @param text
     * @return
     */
    protected IMMessage createTextMessage(String text) {
        IMMessage imMessage = MessageBuilder.createTextMessage(container.account, container.sessionType, text);
        CustomMessageConfig customMessageConfig = new CustomMessageConfig();
        customMessageConfig.enableHistory = false;//该消息是否允许在消息历史中拉取
        customMessageConfig.enableRoaming = false;//该消息是否需要漫游
        customMessageConfig.enableUnreadCount = false;
        customMessageConfig.enableSelfSync = false;
        imMessage.setConfig(customMessageConfig);
        Map<String, Object> data = new HashMap<>();
        data.put("MsgType", ChatMsgType.ORDINARY_SEND_MSG);
        imMessage.setPushPayload(data);
        Map<String, Object> extra = new HashMap<>();
        extra.put("isPay", "0");//0不付费  1付费
        extra.put("type", "1");
        extra.put("isFrom", "视频");
        imMessage.setRemoteExtension(extra);
        return imMessage;
    }

    /**
     * 显示键盘
     */
    private void showKeyBoard() {
        try {
            isKeyBoardShow = true;
            if (mRlayoutVideoChat != null) {
                mRlayoutVideoChat.setVisibility(View.VISIBLE);
            }
            mCEditTxtMsg.setFocusableInTouchMode(true);
            mCEditTxtMsg.setFocusable(true);
            mCEditTxtMsg.requestFocus();
            mCEditTxtMsg.findFocus();
            SystemTool.showKeyBoard(mCEditTxtMsg);
            mRlSurfaceBottomBar.setVisibility(View.GONE);
//            mImgBtnClose.setVisibility(View.GONE);
        } catch (Exception e) {

        }
    }

    /**
     * 软键盘显示与隐藏的监听
     */
    private void softKeyboardListener() {
        SoftKeyBoardListener.setListener(AVChatActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {//软键盘展示
//                animateToHide();
            }

            @Override
            public void keyBoardHide(int height) {//软键盘影藏
                if (mRlayoutVideoChat != null) {
                    mRlayoutVideoChat.setVisibility(View.GONE);
                }
                mRlSurfaceBottomBar.setVisibility(View.VISIBLE);
//                mImgBtnClose.setVisibility(View.VISIBLE);
//                animateToShow();
            }
        });
    }


    //发消息之前网易云消息回调
    @Override
    public boolean sendMessage(IMMessage message) {
        if (!isAllowSendMessage(message)) {
            return false;
        }
        // send message to server and save to db
        NIMClient.getService(MsgService.class).sendMessage(message, false);
//        deleteItem(message);
        messageListPanel.onMsgSend(message);
        return true;
    }

    // 是否允许发送消息
    protected boolean isAllowSendMessage(final IMMessage message) {
        return true;
    }

    /**
     * 删除消息
     *
     * @param messageItem
     */
    private void deleteItem(IMMessage messageItem) {
        NIMClient.getService(MsgService.class).deleteChattingHistory(messageItem);
    }

    /**
     * ****************** 观察者 **********************
     */
    private boolean isObserversRegister = false;

    private void registerObservers(boolean register) {
        isObserversRegister = register;
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(incomingMessageObserver, register);
//        service.observeMessageReceipt(messageReceiptObserver, register);
    }

    /**
     * 消息接收观察者
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            messageListPanel.onIncomingMessage(messages);
//            sendMsgReceipt(); // 发送已读回执
        }
    };


    @Override
    public void onInputPanelExpand() {

    }

    @Override
    public void shouldCollapseInputPanel() {

    }

    @Override
    public boolean isLongClickEnabled() {
        return false;
    }
}

