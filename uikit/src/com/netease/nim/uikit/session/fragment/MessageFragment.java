package com.netease.nim.uikit.session.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.CustomPushContentProvider;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.api.utils.PreferencesIm;
import com.netease.nim.uikit.api.utils.PreferencesMessage;
import com.netease.nim.uikit.api.utils.Utils;
import com.netease.nim.uikit.api.utils.UtilsIm;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.ChatFeeType;
import com.netease.nim.uikit.app.SendMessageEnum;
import com.netease.nim.uikit.app.entity.ExpenseEntity;
import com.netease.nim.uikit.app.entity.MonyEntity;
import com.netease.nim.uikit.app.extension.ChatGiftAttachment;
import com.netease.nim.uikit.app.myenum.ChatEnum;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nim.uikit.session.actions.BaseAction;
import com.netease.nim.uikit.session.actions.ImageAction;
import com.netease.nim.uikit.session.actions.LocationAction;
import com.netease.nim.uikit.session.actions.VideoAction;
import com.netease.nim.uikit.session.constant.Extras;
import com.netease.nim.uikit.session.module.Container;
import com.netease.nim.uikit.session.module.ModuleProxy;
import com.netease.nim.uikit.session.module.input.InputPanel;
import com.netease.nim.uikit.session.module.list.MessageListPanelEx;
import com.netease.nim.uikit.team.model.TeamExtras;
import com.netease.nim.uikit.team.model.TeamRequestCode;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天界面基类
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class MessageFragment extends TFragment implements ModuleProxy {

    private View rootView;

    private SessionCustomization customization;

    protected static final String TAG = "MessageActivity";

    // 聊天对象
    protected String sessionId; // p2p对方Account或者群id

    protected SessionTypeEnum sessionType;

    // modules
    protected InputPanel inputPanel;
    protected MessageListPanelEx messageListPanel;
    public static List<BaseAction> actions;



    public String chatText="1";///发送消息收费类型
    public String sendMessageType="0";//发送消息类型
    ExpenseEntity expenseEntity=new ExpenseEntity();//扣费类
    NetApi api=new NetApi();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parseIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.nim_message_fragment, container, false);
        return rootView;
    }

    /**
     * ***************************** life cycle *******************************
     */

    @Override
    public void onPause() {
        super.onPause();

        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE,
                SessionTypeEnum.None);
        inputPanel.onPause();
        messageListPanel.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        messageListPanel.onResume();
        NIMClient.getService(MsgService.class).setChattingAccount(sessionId, sessionType);
        getActivity().setVolumeControlStream(AudioManager.STREAM_VOICE_CALL); // 默认使用听筒播放
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        messageListPanel.onDestroy();
        registerObservers(false);
        if (inputPanel != null) {
            inputPanel.onDestroy();
        }
    }

    public boolean onBackPressed() {
        if (inputPanel.collapse(true)) {
            return true;
        }

        if (messageListPanel.onBackPressed()) {
            return true;
        }
        return false;
    }

    public void refreshMessageList() {
        messageListPanel.refreshMessageList();
    }

    private void parseIntent() {
        sessionId = getArguments().getString(Extras.EXTRA_ACCOUNT);
        sessionType = (SessionTypeEnum) getArguments().getSerializable(Extras.EXTRA_TYPE);
        IMMessage anchor = (IMMessage) getArguments().getSerializable(Extras.EXTRA_ANCHOR);

        customization = (SessionCustomization) getArguments().getSerializable(Extras.EXTRA_CUSTOMIZATION);
        Container container = new Container(getActivity(), sessionId, sessionType, this);

        if (messageListPanel == null) {
            messageListPanel = new MessageListPanelEx(this,container, rootView, anchor, false, false);
        } else {
            messageListPanel.reload(container, anchor);
        }

        if (inputPanel == null) {
            inputPanel = new InputPanel(container, rootView, getActionList());
            inputPanel.setCustomization(customization);
        } else {
            inputPanel.reload(container, customization);
        }

        registerObservers(true);

        if (customization != null) {
            messageListPanel.setChattingBackground(customization.backgroundUri, customization.backgroundColor);
        }
    }

    /**
     * ************************* 消息收发 **********************************
     */
    // 是否允许发送消息
    protected boolean isAllowSendMessage(final IMMessage message) {
        return true;
    }

    /**
     * ****************** 观察者 **********************
     */

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(incomingMessageObserver, register);
        service.observeMessageReceipt(messageReceiptObserver, register);
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
//            if (AppConfig.videoToCustomerId.equals(sessionId)&&messages.get(0).getMsgType()!= MsgTypeEnum.avchat){return;}
            messageListPanel.onIncomingMessage(messages);
            sendMsgReceipt(); // 发送已读回执
        }
    };

    private Observer<List<MessageReceipt>> messageReceiptObserver = new Observer<List<MessageReceipt>>() {
        @Override
        public void onEvent(List<MessageReceipt> messageReceipts) {
            receiveReceipt();
        }
    };

    /**
     * ********************** implements ModuleProxy *********************
     */
    @Override
    public boolean sendMessage(IMMessage message) {
//群聊判断是否禁言
        if (message.getSessionType()==SessionTypeEnum.Team){
            isGroupCaht(message);
            return true;
        }
        if (!isAllowSendMessage(message)) {
            return false;
        }
        int mesType=message.getMsgType().getValue();
        if (mesType==1&&message.getPushPayload()!=null){///礼物
            message.setPushContent("发来一个礼物");
            message.setContent("礼物");
            appendPushConfig(message);
        // send message to server and save to db
        NIMClient.getService(MsgService.class).sendMessage(message, false);

        messageListPanel.onMsgSend(message);
            return true;
        }
        switch (mesType){
            case 0:///文本消息
                chatText=ChatFeeType.chatText;
                sendMessageType= SendMessageEnum.MessageText;
                break;
            case 1:////图片
                chatText=ChatFeeType.chatText;
                sendMessageType= SendMessageEnum.MessagePicture;
                break;
            case 2://语音
                chatText=ChatFeeType.chatText;
                sendMessageType= SendMessageEnum.MessageAudio;
                break;
            case 3://视频
//                chatText=ChatFeeType.charVideo;
//                sendMessageType= SendMessageEnum.MessageText;
                break;
            case 4://位置

                break;
            case 5://文件

                break;
            case 6://音视频通话

                break;
            case 7://通知消息

                break;
            case 8://提醒消息

                break;
            case 9://自定义消息

                break;
            case 100://阅后即焚
                break;
        }

        if (!AppConfig.isShopP2PChat && !(message.getAttachment() instanceof ChatGiftAttachment)){
            if (!PreferencesMessage.readBoolean(getContext(),PreferencesMessage.isChat+sessionId)){
                if (PreferencesMessage.readInt(getContext(),sessionId)<3){
                    PreferencesMessage.write(getContext(),sessionId,(PreferencesMessage.readInt(getContext(),sessionId)+1));
                }else {//私聊消息限制
                    Toast.makeText(getContext(),R.string.message_Reply_before,Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        Map<String, Object> pushPayload=new HashMap<>();
        pushPayload.put("my_customer_id",message.getFromAccount());
        pushPayload.put("op_customer_id",message.getSessionId());
        pushPayload.put("type","1");
        message.setPushPayload(pushPayload);

        appendPushConfig(message);
        // send message to server and save to db
        NIMClient.getService(MsgService.class).sendMessage(message, false);
//
        messageListPanel.onMsgSend(message);

        return true;
    }

    public void isGroupCaht(final IMMessage message){
        Map<String, Object> pushPayload=new HashMap<>();
//        pushPayload.put("my_customer_id",message.getFromAccount());
//        pushPayload.put("op_customer_id",message.getSessionId());
        pushPayload.put("sub_type",AppConfig.chatEnum.getValue());
        pushPayload.put("t_id",message.getSessionId());
        pushPayload.put("type","1");
        pushPayload.put("leader_id",AppConfig.sponsorId);
        message.setPushPayload(pushPayload);
        if (AppConfig.chatEnum!= ChatEnum.defaults){
            NIMClient.getService(MsgService.class).sendMessage(message, false);
            messageListPanel.onMsgSend(message);
            return;
        }
        Map<String,String> params=new HashMap<>();
        params.put("t_id",message.getSessionId());
        api.isGroupCaht(params, this, new DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                appendPushConfig(message);
                // send message to server and save to db
                NIMClient.getService(MsgService.class).sendMessage(message, false);
//
                messageListPanel.onMsgSend(message);
            }
            @Override
            public void onFailed(String errCode, String errMsg) {
            }
        });
    }
    private void appendPushConfig(IMMessage message) {
        CustomPushContentProvider customConfig = NimUIKit.getCustomPushContentProvider();
        if (customConfig != null) {
            String content = customConfig.getPushContent(message);
            Map<String, Object> payload = customConfig.getPushPayload(message);
            message.setPushContent(content);
            message.setPushPayload(payload);
        }
    }

    @Override
    public void onInputPanelExpand() {
        messageListPanel.scrollToBottom();
    }

    @Override
    public void shouldCollapseInputPanel() {
        inputPanel.collapse(false);
    }

    @Override
    public boolean isLongClickEnabled() {
        return !inputPanel.isRecording();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        inputPanel.onActivityResult(requestCode, resultCode, data);
        messageListPanel.onActivityResult(requestCode, resultCode, data);
    }

    public void putPrivate(TeamMember member){
        Intent data = new Intent();
        data.putExtra(TeamExtras.RESULT_EXTRA_DATA, member);
        inputPanel.onActivityResult(TeamRequestCode.REQUEST_TEAM_AIT_MEMBER, Activity.RESULT_OK, data);
//        messageListPanel.onActivityResult(TeamRequestCode.REQUEST_TEAM_AIT_MEMBER, Activity.RESULT_OK, data);
    }
    // 操作面板集合
    protected List<BaseAction> getActionList() {
        List<BaseAction> actions = new ArrayList<>();
        actions.add(new ImageAction());
        actions.add(new VideoAction());
//        actions.add(new LocationAction());
        if (customization != null && customization.actions != null && !AppConfig.isShopP2PChat) {
//            if (!AppConfig.isVideo){
//                actions.add(customization.actions.get(0));
//            }else{
                actions.addAll(customization.actions);
//            }
        }
        return actions;
    }
//    public static void setActionList(List<BaseAction> action){
//        actions=action;
//    }

    /**
     * 发送已读回执
     */
    private void sendMsgReceipt() {
        messageListPanel.sendReceipt();
    }

    /**
     * 收到已读回执
     */
    public void receiveReceipt() {
        messageListPanel.receiveReceipt();
    }


}
