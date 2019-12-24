package com.yuejian.meet.session.panel;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.api.utils.PreferencesIm;
import com.netease.nim.uikit.api.utils.UtilsIm;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.GiftAllEntity;
import com.netease.nim.uikit.app.entity.MonyEntity;
import com.netease.nim.uikit.app.extension.ChatGiftAttachment;
import com.netease.nim.uikit.app.extension.CustomAttachment;
import com.netease.nim.uikit.app.extension.CustomAttachmentType;
import com.netease.nim.uikit.common.ui.listview.ListViewUtil;
import com.netease.nim.uikit.session.module.Container;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MemberPushOption;
import com.netease.nimlib.sdk.msg.model.NIMAntiSpamOption;
import com.yuejian.meet.R;
import com.yuejian.meet.app.MyApplication;
import com.yuejian.meet.bean.animation.GiftAnmManager;
import com.yuejian.meet.bsr.BSRGiftLayout;
import com.yuejian.meet.bsr.BSRGiftView;
import com.yuejian.meet.session.avchat.constant.ChatMsgType;
import com.yuejian.meet.session.widgets.AVChatAdapter;
import com.yuejian.meet.session.widgets.CustomRoundView;
import com.yuejian.meet.session.widgets.MagicTextView;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.FileUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.MaxHeightListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 消息收发模块
 * <b>创建时间</b> 2016/11/22 <br>
 *
 * @author zhouwenjun
 */
public class MessageListPanel {

    private Container container = null;
    private View rootView = null;
    private List<IMMessage> items;
    private AVChatAdapter avChatAdapter;
    private MaxHeightListView mLvChat;
    private ImageView mGiftImage;
    private String chatTheme;
    private String name;
    private LinearLayout llgiftcontent;
    /**
     * 动画相关
     */
    private NumAnim giftNumAnim;
    private TranslateAnimation inAnim;
    private TranslateAnimation outAnim;
    /**
     * 数据相关
     */
    private List<View> giftViewCollection = new ArrayList<View>();
    private BSRGiftView bsrGiftView;
    private BSRGiftLayout bsrGiftLayout;
    private GiftAnmManager giftAnmManager;

    public MessageListPanel(Container container, View rootView, String chatTheme, String name) {
        this.container = container;
        this.rootView = rootView;
        this.chatTheme = chatTheme;
        this.name = name;
        init();
    }

    private void init() {
        findView();
        initListView();
        //礼物展现和消失动画
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(container.activity, R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(container.activity, R.anim.gift_out);
        giftNumAnim = new NumAnim();
        clearTiming();
    }

    private void findView() {
        mLvChat = (MaxHeightListView) rootView.findViewById(R.id.lv_video_chat_request);
        mLvChat.setListViewHeight(DensityUtils.dip2px(container.activity, 180));
//        mGiftImage = (ImageView) rootView.findViewById(R.id.gift_iv_annim);
        llgiftcontent = (LinearLayout) rootView.findViewById(R.id.llgiftcontent);
        bsrGiftLayout = (BSRGiftLayout) rootView.findViewById(R.id.gift_layout);
        bsrGiftView = (BSRGiftView) rootView.findViewById(R.id.gift_view);
        giftAnmManager = new GiftAnmManager(bsrGiftLayout, container.activity);
    }

    /**
     * 初始化文字聊天列表
     */
    private void initListView() {
        items = new ArrayList<>();
        IMMessage message1 = getLocalMessage(container.activity.getString(R.string.toast_video_tips),
                container.activity.getString(R.string.tex_about_notice), ChatMsgType.SYSTEM_NOTIFY_TIPS);
        items.add(message1);
        if (!TextUtils.isEmpty(chatTheme)) {
            IMMessage message2 = getLocalMessage(container.activity.getString(R.string.txt_my_thme) + chatTheme, name, ChatMsgType.ORDINARY_SEND_MSG);
            items.add(message2);
        }
        avChatAdapter = new AVChatAdapter(mLvChat, items, R.layout.layout_avchat_lv_item);
        mLvChat.setAdapter(avChatAdapter);
    }

    /**
     * 发送消息后，更新本地消息列表
     *
     * @param message
     */
    public void onMsgSend(IMMessage message) {
        // add to listView and refresh
        items.add(message);
        List<IMMessage> addedListItems = new ArrayList<>(1);
        addedListItems.add(message);
        avChatAdapter.refresh(items);
        ListViewUtil.scrollToBottom(mLvChat);
    }

    /**
     * 收到消息
     *
     * @param messages
     */
    public void onIncomingMessage(List<IMMessage> messages) {

        boolean needScrollToBottom = ListViewUtil.isLastMessageVisible(mLvChat);
        List<IMMessage> addedListItems = new ArrayList<>(messages.size());
        for (IMMessage message : messages) {
            if (isMyMessage(message)) {
                String msgType = ChatMsgType.ORDINARY_SEND_MSG;
                Map<String, Object> pushData = message.getPushPayload();
                if (pushData != null) {
//                    msgType = pushData.get("MsgType").toString();
                }
                if (ChatMsgType.GIFT_MSG.equals(ChatMsgType.GIFT_MSG)) {//如果收到礼物信息
                    receiveGiftMsg(message);
//                    message.setContent(message.getFromNick()+"发送了 "+pushData.get("giftName").toString()+"给您");
                    getBal();
                }
                items.add(message);
                addedListItems.add(message);
//                deleteItem(message);
            }
        }
//        avChatAdapter.refresh(items);
        // incoming messages tip
        try {
            IMMessage lastMsg = messages.get(messages.size() - 1);
//            if (isMyMessage(lastMsg)) {
                if (needScrollToBottom) {
                    ListViewUtil.scrollToBottom(mLvChat);
                }
//            }
        } catch (Exception e) {

        }
    }
    /**
     * 支付成功之后 重新查余额
     */
    public void getBal() {
        new NetApi().getBal(AppConfig.CustomerId, this, new DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                MonyEntity monyEntity = JSON.parseObject(data, MonyEntity.class);
                if (monyEntity == null) {
                    ViewInject.shortToast(container.activity, "更新钱包失败");
                    return;
                }
                UtilsIm.setMyMoney(container.activity, monyEntity);
                if (AppConfig.UserSex.equals("1"))
                    Bus.getDefault().post(AppConfig.updateMoney);///发通知给视频重新算视频时长
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
    /**
     * 删除消息
     *
     * @param messageItem
     */
    private void deleteItem(IMMessage messageItem) {
        NIMClient.getService(MsgService.class).deleteChattingHistory(messageItem);
    }

    /**
     * 收到礼物消息，执行礼物展示动画
     */
    public void receiveGiftMsg(IMMessage message) {
//        ChatGiftDao chatGiftDao = new ChatGiftDao(container.activity);
        String name = message.getFromNick();
        try {
            ChatGiftAttachment redPacket= (ChatGiftAttachment) message.getAttachment();
            if (redPacket==null)return;
            GiftAllEntity entity=JSON.parseObject(redPacket.getData(),GiftAllEntity.class);
            showGift(entity, name,message.getFromAccount());
        }catch (Exception e){}

//        Map<String,Object> map=message.getPushPayload();
//        List<GiftAllEntity> listGift= JSON.parseArray(PreferencesIm.get(MyApplication.context, PreferencesIm.gift_image,""),GiftAllEntity.class);
//
//        if (map==null)
//            return;
////        GiftAttachment attachment = (GiftAttachment) message.getAttachment();
////        if (attachment == null) {
////            return;
////        }
//        String id=map.get("giftId").toString();
//        GiftAllEntity giftBean = null;
//        for(int i=0;i<listGift.size();i++){
//            if (listGift.get(i).getId().equals(id)){
//                giftBean=listGift.get(i);
//                break;
//            }
//        }
////        GiftAllEntity giftBean = listGift.get(Integer.parseInt(map.get("giftId").toString()));
////        GiftAllEntity giftBean = null;
//        if (giftBean == null) {
//            giftBean = new GiftAllEntity();
//            giftBean.setGift_name("礼物");
//        }
////        if (giftBean.id.equals("12")) {
////        giftAnmManager.showCarOne();
////        } else {
//            showGift(entity, name);
////        }
    }


    Timer timer = new Timer();
    /**
     * 定时清除礼物
     */
    private void clearTiming() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int count = llgiftcontent.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = llgiftcontent.getChildAt(i);
                    CustomRoundView crvheadimage = (CustomRoundView) view.findViewById(R.id.crvheadimage);
                    long nowtime = System.currentTimeMillis();
                    long upTime = (Long) crvheadimage.getTag();
                    if ((nowtime - upTime) >= 3000) {
                        removeGiftView(i);
                        return;
                    }
                }
            }
        };
        timer.schedule(task, 0, 3000);
    }

    AnimatorSet animatorSet = new AnimatorSet();
    ObjectAnimator objectAnimator1;
    ObjectAnimator objectAnimator2;
    ObjectAnimator objectAnimator3;
    /**
     * 显示礼物的方法
     */
    private void showGift(GiftAllEntity giftBean, String name,String account) {
        String tag = giftBean.gift_name+account;
        View giftView = llgiftcontent.findViewWithTag(tag);
        if (giftView == null) {/*该用户不在礼物显示列表*/

            if (llgiftcontent.getChildCount() > 2) {/*如果正在显示的礼物的个数超过两个，那么就移除最后一次更新时间比较长的*/
                View giftView1 = llgiftcontent.getChildAt(0);
                CustomRoundView picTv1 = (CustomRoundView) giftView1.findViewById(R.id.crvheadimage);
                long lastTime1 = (Long) picTv1.getTag();
                View giftView2 = llgiftcontent.getChildAt(1);
                CustomRoundView picTv2 = (CustomRoundView) giftView2.findViewById(R.id.crvheadimage);
                long lastTime2 = (Long) picTv2.getTag();
                if (lastTime1 > lastTime2) {/*如果第二个View显示的时间比较长*/
                    removeGiftView(1);
                } else {/*如果第一个View显示的时间长*/
                    removeGiftView(0);
                }
            }

            giftView = addGiftView();/*获取礼物的View的布局*/
            giftView.setTag(tag);/*设置view标识*/

            CustomRoundView crvheadimage = (CustomRoundView) giftView.findViewById(R.id.crvheadimage);
            ImageView mImgGiftPic = (ImageView) giftView.findViewById(R.id.img_gift_pic);
            LinearLayout mLlBg = (LinearLayout) giftView.findViewById(R.id.ll_bg);
            TextView mTxtName = (TextView) giftView.findViewById(R.id.txt_user_name);
            TextView mTxtGiftName = (TextView) giftView.findViewById(R.id.txt_gift_name);
            mTxtName.setText(name);
            if (account.equals(AppConfig.CustomerId)){
                mTxtGiftName.setText("你赠送了礼物" + giftBean.gift_name);
            }else {
                mTxtGiftName.setText("给你赠送了礼物" + giftBean.gift_name);
            }
            String fileUrl= FileUtils.getChatGiftPath(MyApplication.context)+"/"+ StringUtils.getFileName(giftBean.getGift_image());
            Bitmap bitmap = BitmapFactory.decodeFile(fileUrl);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(container.activity.getResources(), R.mipmap.ic_gift_animation);
            }
            mImgGiftPic.setImageBitmap(bitmap);

            final MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
            giftNum.setText("x"+giftBean.getGift_count());/*设置礼物数量*/
            crvheadimage.setTag(System.currentTimeMillis());/*设置时间标记*/
            giftNum.setTag(Integer.parseInt(giftBean.getGift_count()+""));/*给数量控件设置标记*/

            llgiftcontent.addView(giftView);/*将礼物的View添加到礼物的ViewGroup中*/
            llgiftcontent.invalidate();/*刷新该view*/
            objectAnimator1 = ObjectAnimator.ofFloat(mLlBg,"translationX",-500f,0f);
            objectAnimator1.setDuration(500);

            objectAnimator2 = ObjectAnimator.ofFloat(mImgGiftPic,"alpha",0f,1f);
            objectAnimator2.setStartDelay(300);
            objectAnimator2.setDuration(500);

            objectAnimator3 = ObjectAnimator.ofFloat(mImgGiftPic,"translationX",-500f,0f);
            objectAnimator3.setStartDelay(300);
            objectAnimator3.setDuration(500);
            animatorSet.play(objectAnimator1).with(objectAnimator2).with(objectAnimator3);
            animatorSet.start();
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    giftNumAnim.start(giftNum);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
//            giftView.startAnimation(inAnim);/*开始执行显示礼物的动画*/
//            inAnim.setAnimationListener(new Animation.AnimationListener() {/*显示动画的监听*/
//                @Override
//                public void onAnimationStart(Animation animation) {
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    giftNumAnim.start(giftNum);
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//                }
//            });
        } else {/*该用户在礼物显示列表*/
            CustomRoundView crvheadimage = (CustomRoundView) giftView.findViewById(R.id.crvheadimage);/*找到头像控件*/
            MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
            int showNum = (Integer) giftNum.getTag() + Integer.parseInt(giftBean.getGift_count()+"");
            giftNum.setText("x" + showNum);
            giftNum.setTag(showNum);
            crvheadimage.setTag(System.currentTimeMillis());
            giftNumAnim.start(giftNum);
        }
    }

    /**
     * 添加礼物view,(考虑垃圾回收)
     */
    private View addGiftView() {
        View view = null;
        if (giftViewCollection.size() <= 0) {
            /*如果垃圾回收中没有view,则生成一个*/
            view = LayoutInflater.from(container.activity).inflate(R.layout.item_gift, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            view.setLayoutParams(lp);
            llgiftcontent.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view) {
                }

                @Override
                public void onViewDetachedFromWindow(View view) {
                    giftViewCollection.add(view);
                }
            });
        } else {
            view = giftViewCollection.get(0);
            giftViewCollection.remove(view);
        }
        return view;
    }

    /**
     * 删除礼物view
     */
    private void removeGiftView(final int index) {
        final View removeView = llgiftcontent.getChildAt(index);
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llgiftcontent.removeViewAt(index);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        if (container.activity != null) {
            container.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    removeView.startAnimation(outAnim);
                }
            });
        }
    }


    /**
     * 收到已读回执（更新VH的已读label）
     */
    public void receiveReceipt() {

    }

    /**
     * 发送已读回执（需要过滤）
     */

    public void sendReceipt() {
        if (container.account == null || container.sessionType != SessionTypeEnum.P2P) {
            return;
        }
        IMMessage message = getLastReceivedMessage();
        if (!sendReceiptCheck(message)) {
            return;
        }
        NIMClient.getService(MsgService.class).sendMessageReceipt(container.account, message);
    }

    private IMMessage getLastReceivedMessage() {
        IMMessage lastMessage = null;
        for (int i = items.size() - 1; i >= 0; i--) {
            if (sendReceiptCheck(items.get(i))) {
                lastMessage = items.get(i);
                break;
            }
        }
        return lastMessage;
    }

    private boolean sendReceiptCheck(final IMMessage msg) {
        if (msg == null || msg.getDirect() != MsgDirectionEnum.In ||
                msg.getMsgType() == MsgTypeEnum.tip || msg.getMsgType() == MsgTypeEnum.notification||msg.getMsgType()==MsgTypeEnum.video||msg.getMsgType().getValue()==100) {
            return false; // 非收到的消息，Tip消息和通知类消息，不要发已读回执
        }
        return true;
    }

    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == container.sessionType
                && message.getSessionId() != null
                && message.getMsgType() != MsgTypeEnum.audio
                && message.getMsgType() != MsgTypeEnum.video
                && message.getMsgType() != MsgTypeEnum.avchat
                && message.getSessionId().equals(container.account);
    }

    /**
     * 初始化官方消息
     *
     * @return
     */
    @NonNull
    private IMMessage getLocalMessage(final String content, final String nickName, final String msgType) {
        return new IMMessage() {
            @Override
            public String getUuid() {
                return null;
            }

            @Override
            public boolean isTheSame(IMMessage imMessage) {
                return false;
            }

            @Override
            public String getSessionId() {
                return null;
            }

            @Override
            public SessionTypeEnum getSessionType() {
                return SessionTypeEnum.P2P;
            }

            @Override
            public String getFromNick() {
                return nickName;
            }

            @Override
            public MsgTypeEnum getMsgType() {
                return MsgTypeEnum.text;
            }

            @Override
            public MsgStatusEnum getStatus() {
                return null;
            }

            @Override
            public void setStatus(MsgStatusEnum msgStatusEnum) {

            }

            @Override
            public void setDirect(MsgDirectionEnum msgDirectionEnum) {

            }

            @Override
            public MsgDirectionEnum getDirect() {
                return null;
            }

            @Override
            public void setContent(String s) {

            }

            @Override
            public String getContent() {
                return content;
            }

            @Override
            public long getTime() {
                return 0;
            }

            @Override
            public void setFromAccount(String s) {

            }

            @Override
            public String getFromAccount() {
                return "";
            }

            @Override
            public void setAttachment(MsgAttachment msgAttachment) {

            }

            @Override
            public MsgAttachment getAttachment() {
                return null;
            }

            @Override
            public AttachStatusEnum getAttachStatus() {
                return null;
            }

            @Override
            public void setAttachStatus(AttachStatusEnum attachStatusEnum) {

            }

            @Override
            public CustomMessageConfig getConfig() {
                return null;
            }

            @Override
            public void setConfig(CustomMessageConfig customMessageConfig) {

            }

            @Override
            public Map<String, Object> getRemoteExtension() {
                return null;
            }

            @Override
            public void setRemoteExtension(Map<String, Object> map) {

            }

            @Override
            public Map<String, Object> getLocalExtension() {
                return null;
            }

            @Override
            public void setLocalExtension(Map<String, Object> map) {

            }

            @Override
            public String getPushContent() {
                return null;
            }

            @Override
            public void setPushContent(String s) {

            }

            @Override
            public Map<String, Object> getPushPayload() {
                Map<String, Object> data = new HashMap<>();
                data.put("MsgType", msgType);
                return data;
            }

            @Override
            public void setPushPayload(Map<String, Object> map) {

            }

            @Override
            public MemberPushOption getMemberPushOption() {
                return null;
            }

            @Override
            public void setMemberPushOption(MemberPushOption memberPushOption) {

            }

            @Override
            public boolean isRemoteRead() {
                return false;
            }

            @Override
            public int getFromClientType() {
                return 0;
            }

            @Override
            public NIMAntiSpamOption getNIMAntiSpamOption() {
                return null;
            }

            @Override
            public void setNIMAntiSpamOption(NIMAntiSpamOption nimAntiSpamOption) {

            }
        };
    }

    AnimatorSet animSet = new AnimatorSet();
    private Animator lastAnimator = null;
    /**
     * 数字放大动画
     */
    public class NumAnim {

        public void start(View view) {
            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.end();
                lastAnimator.cancel();
            }
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX", 2.2f, 0.8f, 1.0f);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY", 2.2f, 0.8f, 1.0f);
            lastAnimator = animSet;
            animSet.setDuration(200);
            animSet.setInterpolator(new OvershootInterpolator());
            animSet.playTogether(anim1, anim2);
            animSet.start();
        }
    }
    public void onDestroy(){
        if (animSet!=null){
            animSet.cancel();
            animSet=null;
        }
        if (lastAnimator!=null){
            lastAnimator.cancel();
            lastAnimator=null;
        }
        if (timer!=null){
            timer.cancel();
            timer=null;
        }
        if (animatorSet!=null){
            animatorSet.cancel();
            animatorSet=null;
        }
        if (objectAnimator1!=null){
            objectAnimator1.cancel();
            objectAnimator1=null;
        }
        if (objectAnimator2!=null){
            objectAnimator2.cancel();
            objectAnimator2=null;
        }
        if (objectAnimator3!=null){
            objectAnimator3.cancel();
            objectAnimator1=null;
        }
    }
}
