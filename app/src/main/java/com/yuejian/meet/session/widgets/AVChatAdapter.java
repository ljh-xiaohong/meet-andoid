package com.yuejian.meet.session.widgets;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.AbsListView;
import android.widget.TextView;

import com.netease.nim.uikit.app.AppConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.session.avchat.constant.ChatMsgType;
import com.yuejian.meet.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 视频过程中文字聊天界面适配器
 * <b>创建时间</b> 2016/11/24 <br>
 *
 * @author zhouwenjun
 */
public class AVChatAdapter extends FKAdapter<IMMessage> {

    private TextView txtUserName;
    private String fromNick;
    private String string;

    public AVChatAdapter(AbsListView view, List<IMMessage> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
    }

    @Override
    public void convert(AdapterHolder helper, IMMessage item, boolean isScrolling, int position) {
        super.convert(helper, item, isScrolling, position);
        txtUserName = helper.getView(R.id.txt_chat_user);
        String msgType = ChatMsgType.ORDINARY_SEND_MSG;
        Map<String, Object> pushData = item.getPushPayload();
        if (pushData != null) {
            msgType = pushData.get("MsgType").toString();
        }
        switch (msgType) {
            case ChatMsgType.FOLLOW_MSG://关注消息
                initFollowMsg(item, isScrolling);
                break;
            case ChatMsgType.ORDINARY_SEND_MSG://普通文字消息
                initSendOrdinaryMsg(item, isScrolling);
                break;
            case ChatMsgType.GIFT_MSG://礼物消息
                initGiftMsg(item, isScrolling);
                break;
            case ChatMsgType.SYSTEM_NOTIFY_TIPS://初始化官方消息
                initSystemMsg(item, isScrolling);
                break;
        }
    }

    /**
     * 普通文字消息
     *
     * @param item
     * @param isScrolling
     */
    private void initSendOrdinaryMsg(IMMessage item, boolean isScrolling) {
        fromNick = item.getFromNick();
        if (AppConfig.CustomerId.equals(item.getFromAccount())) {//如果是自己
            fromNick = mCxt.getString(R.string.txt_me);
        }
        String content = fromNick + ":  " + item.getContent();
        SpannableString spannableString = new SpannableString(content);
        if (AppConfig.CustomerId.equals(item.getFromAccount())) {//如果是自己
            //用颜色标记文本
            spannableString.setSpan(new ForegroundColorSpan(mCxt.getResources().getColor(R.color.colorPink)), 0, fromNick.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), fromNick.length() + 3, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //setSpan时需要指定的 flag,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括).
        } else {
            spannableString.setSpan(new ForegroundColorSpan(mCxt.getResources().getColor(R.color.colorGreen)), 0, fromNick.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), fromNick.length() + 3, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        txtUserName.setText(spannableString);
    }

    /**
     * 系统通知
     *
     * @param item
     * @param isScrolling
     */
    private void initSystemMsg(IMMessage item, boolean isScrolling) {
        fromNick = item.getFromNick();
        if (!fromNick.equals(mCxt.getString(R.string.tex_about_notice))) {
            fromNick = mCxt.getString(R.string.tex_about_notice);
        }
        String content = fromNick + ":  " + item.getContent();
        SpannableString spannableString = new SpannableString(content);
        //用颜色标记文本
        spannableString.setSpan(new ForegroundColorSpan(mCxt.getResources().getColor(R.color.colorYellow)), 0, fromNick.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(mCxt.getResources().getColor(R.color.colorPink)), fromNick.length() + 3, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //setSpan时需要指定的 flag,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括).
        txtUserName.setText(spannableString);
    }

    /**
     * 礼物消息
     *
     * @param item
     * @param isScrolling
     */
    private void initGiftMsg(IMMessage item, boolean isScrolling) {
        //setSpan时需要指定的 flag,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括).
        string = mCxt.getString(R.string.tex_about_notice);
        if (AppConfig.CustomerId.equals(item.getFromAccount())) {//如果是自己
            String content = string + ":  " + item.getPushContent();
            SpannableString spannableString = new SpannableString(content);
            //用颜色标记文本
            spannableString.setSpan(new ForegroundColorSpan(mCxt.getResources().getColor(R.color.colorYellow)), 0, string.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(mCxt.getResources().getColor(R.color.colorPink)), string.length() + 3, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtUserName.setText(spannableString);
        } else {
            Map<String, Object> pushData = item.getPushPayload();
//            ChatGiftDao chatGiftDao = new ChatGiftDao(mCxt);
//            GiftAttachment attachment = (GiftAttachment) item.getAttachment();
//            if (attachment == null) {
//                return;
//            }
//            GiftBean giftBean = chatGiftDao.queryGiftInfo(attachment.getChartlet());
//            if (giftBean == null) {
//                giftBean = new GiftBean("礼物", "*", "", "", "", "");
//            }
            int price = (int) (Double.parseDouble(pushData.get("giftPrice").toString()) * 0.7);
            String content = string + ":  " + item.getFromNick() + " " + String.format(StringUtils.getResStr(mCxt, R.string.live_receive_tips), (pushData.get("giftName").toString() + " (+" + price));
            SpannableString spannableString = new SpannableString(content);
            //用颜色标记文本
            spannableString.setSpan(new ForegroundColorSpan(mCxt.getResources().getColor(R.color.colorYellow)), 0, string.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(mCxt.getResources().getColor(R.color.colorPink)), string.length() + 3, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtUserName.setText(spannableString);
//            txtUserName.setText(NimUserInfoCache.getInstance().getUserInfo(item.getSessionId()).getName());
        }

    }

    /**
     * 关注消息
     *
     * @param item
     * @param isScrolling
     */
    private void initFollowMsg(IMMessage item, boolean isScrolling) {
        string = mCxt.getString(R.string.tex_about_notice);
        if (AppConfig.CustomerId.equals(item.getFromAccount())) {//如果是自己
            String content = string + ":  " + item.getPushContent()/* + item.getFromNick()*/;
            SpannableString spannableString = new SpannableString(content);
            //用颜色标记文本
            spannableString.setSpan(new ForegroundColorSpan(mCxt.getResources().getColor(R.color.colorYellow)), 0, string.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(mCxt.getResources().getColor(R.color.colorPink)), string.length() + 3, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtUserName.setText(spannableString);
            txtUserName.setText(spannableString);
        } else {
            String content = string + ":  " + item.getFromNick() + StringUtils.getResStr(mCxt, R.string.txt_follow_you);
            SpannableString spannableString = new SpannableString(content);
            //用颜色标记文本
            spannableString.setSpan(new ForegroundColorSpan(mCxt.getResources().getColor(R.color.colorYellow)), 0, string.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(mCxt.getResources().getColor(R.color.colorPink)), string.length() + 3, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtUserName.setText(spannableString);
        }
    }
}
