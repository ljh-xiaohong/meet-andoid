package com.yuejian.meet.session.viewholder;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.GiftAllEntity;
import com.netease.nim.uikit.app.entity.RedEnvelopeEntity;
import com.netease.nim.uikit.app.extension.ChatGiftAttachment;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.session.extension.InviteJoinAttachment;
import com.yuejian.meet.utils.ImUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 私聊礼物
 * Created by zh03 on 2017/8/21.
 */

public class MsgViewHlderPrivateChatGift extends MsgViewHolderBase {

    TextView gift_count_left,gift_count_right;
    ImageView private_chat_gift_img;
    RelativeLayout private_chat_gift_layout;

    ProgressDialog dialog;
    public MsgViewHlderPrivateChatGift(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_private_chat_gift_msg_view_layout;
    }

    @Override
    protected void inflateContentView() {
        gift_count_left=findViewById(R.id.gift_count_left);
        gift_count_right=findViewById(R.id.gift_count_right);
        private_chat_gift_img=findViewById(R.id.private_chat_gift_img);
        private_chat_gift_layout=findViewById(R.id.private_chat_gift_layout);
    }

    @Override
    protected void bindContentView() {
        gift_count_left.setVisibility(View.GONE);
        gift_count_right.setVisibility(View.GONE);
        ChatGiftAttachment envelopeAttachment= (ChatGiftAttachment) message.getAttachment();
        GiftAllEntity envelopeEntity= JSON.parseObject(envelopeAttachment.getData(),GiftAllEntity.class);
        if (message.getDirect()== MsgDirectionEnum.In){//接收
            gift_count_right.setVisibility(View.VISIBLE);
//            private_chat_gift_layout.setBackgroundResource(leftBackground());
        }else {
            gift_count_left.setVisibility(View.VISIBLE);
//            private_chat_gift_layout.setBackgroundResource(rightBackground());
        }
        gift_count_left.setText("×"+envelopeEntity.getGift_count()+"");
        gift_count_right.setText("×"+envelopeEntity.getGift_count()+"");
        Glide.with(context).load(envelopeEntity.getGift_image()).placeholder(com.netease.nim.uikit.R.drawable.nim_image_default)
                .error(com.netease.nim.uikit.R.drawable.nim_image_default).into(private_chat_gift_img);
//        invite_join_content.setText("进入群聊即可领取×1"+envelopeEntity.getGift_name());
    }

    @Override
    protected boolean isShowGiftBg() {
        return true;
    }

    @Override
    protected void onItemClick() {

    }
}
