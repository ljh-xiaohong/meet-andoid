package com.yuejian.meet.session.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.app.activity.RedEnvelopeDetailsActivity;
import com.netease.nim.uikit.app.entity.RedEnvelopeEntity;
import com.netease.nim.uikit.app.extension.RedEnvelopeAttachment;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.Map;

/**
 * 礼物
 * Created by zh03 on 2017/8/21.
 */
@Deprecated
public class MsgViewHlderChatGift extends MsgViewHolderBase {

    TextView red_title_msg;
    TextView red_get;
    public MsgViewHlderChatGift(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_red_envelope_msg_view_layout;
    }

    @Override
    protected void inflateContentView() {
        red_title_msg=findViewById(R.id.red_title_msg);
        red_get=findViewById(R.id.red_get);
    }

    @Override
    protected void bindContentView() {
        RedEnvelopeAttachment envelopeAttachment= (RedEnvelopeAttachment) message.getAttachment();
        RedEnvelopeEntity envelopeEntity= JSON.parseObject(envelopeAttachment.getData(),RedEnvelopeEntity.class);
        red_title_msg.setText(envelopeEntity.getContent());
    }

}
