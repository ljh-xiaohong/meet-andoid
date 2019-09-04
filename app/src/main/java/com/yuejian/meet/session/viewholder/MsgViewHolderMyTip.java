package com.yuejian.meet.session.viewholder;

import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.RedEnvelopeEntity;
import com.netease.nim.uikit.app.extension.MyTiptAttachment;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.session.emoji.MoonUtil;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;

import java.util.Map;

/**
 * Created by huangjun on 2015/11/25.
 * Tip类型消息ViewHolder
 */
public class MsgViewHolderMyTip extends MsgViewHolderBase {

    protected TextView notificationTextView;
    protected TextView tip_messaage_gift_hint;
    protected ImageView tip_gift_img;
    protected LinearLayout nim_gift_hint_layout;

    public MsgViewHolderMyTip(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return com.netease.nim.uikit.R.layout.nim_my_tip_message_item_notification;
    }

    @Override
    protected void inflateContentView() {
        notificationTextView = (TextView) view.findViewById(com.netease.nim.uikit.R.id.message_item_notification_label);
        tip_messaage_gift_hint = (TextView) view.findViewById(com.netease.nim.uikit.R.id.tip_messaage_gift_hint);
        tip_gift_img=(ImageView) view.findViewById(com.netease.nim.uikit.R.id.tip_gift_img);
        nim_gift_hint_layout=(LinearLayout) view.findViewById(com.netease.nim.uikit.R.id.nim_gift_hint_layout);
    }

    @Override
    protected void bindContentView() {
        tip_messaage_gift_hint.setVisibility(View.GONE);
        tip_gift_img.setVisibility(View.GONE);
        nim_gift_hint_layout.setVisibility(View.VISIBLE);
        String text = "未知通知提醒";
        MyTiptAttachment myTip= (MyTiptAttachment) message.getAttachment();
        RedEnvelopeEntity envelopeEntity= JSON.parseObject(myTip.getData(),RedEnvelopeEntity.class);
        if (envelopeEntity.getType().equals("0")){///领红包

//            view.setVisibility(View.GONE);
            tip_messaage_gift_hint.setVisibility(View.VISIBLE);
            tip_gift_img.setVisibility(View.VISIBLE);
            if (envelopeEntity.getOp_customer_id().equals(AppConfig.CustomerId)){
                if (envelopeEntity.getCustomer_id().equals(AppConfig.CustomerId)&&envelopeEntity.bag_gift_cnt.equals("0")){
                    text="您领完了自己的";
                }else if (envelopeEntity.getCustomer_id().equals(AppConfig.CustomerId)){
                    text="您领取了自己的";
                }else {
                    text="您领取了"+envelopeEntity.getNick_name()+"的";
                }
            }else {
                if (envelopeEntity.getCustomer_id().equals(AppConfig.CustomerId)&&envelopeEntity.bag_gift_cnt.equals("0")){
                    text=envelopeEntity.getOp_nick_name()+"领完了您的";
                }else if (envelopeEntity.getCustomer_id().equals(AppConfig.CustomerId)){
                    text=envelopeEntity.getOp_nick_name()+"领取了您的";
                }else {
                    nim_gift_hint_layout.setVisibility(View.GONE);
                    // 物理删除
                    NIMClient.getService(MsgService.class).deleteChattingHistory(message);
                    getMsgAdapter().adoptGiftDeleteItem(message, true);
//                    text=envelopeEntity.getOp_nick_name()+"领取了"+envelopeEntity.getNick_name()+"的";
                }
            }
        }else if (envelopeEntity.getType().equals("1")){///接受邀请入群
            tip_messaage_gift_hint.setVisibility(View.GONE);
            if (!envelopeEntity.getCustomer_id().equals(AppConfig.CustomerId))
                text=envelopeEntity.getContent();
            else
                text="您接受了对方的邀请进入了"+envelopeEntity.getGroup_name();
        }else if (envelopeEntity.getType().equals("2")||envelopeEntity.getType().equals("3")||envelopeEntity.getType().equals("4")){
            text=envelopeEntity.getContent();
        }
        notificationTextView.setText(text);
//        String text = "未知通知提醒";
//        if (TextUtils.isEmpty(message.getContent())) {
//            Map<String, Object> content = message.getRemoteExtension();
//            if (content != null && !content.isEmpty()) {
//                text = (String) content.get("content");
//            }
//        } else {
//            text = message.getContent();
//        }
//        handleTextNotification(text);
    }

    private void handleTextNotification(String text) {
        MoonUtil.identifyFaceExpressionAndATags(context, notificationTextView, text, ImageSpan.ALIGN_BOTTOM);
        notificationTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }
}
