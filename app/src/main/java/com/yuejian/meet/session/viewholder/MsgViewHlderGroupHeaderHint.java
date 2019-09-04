package com.yuejian.meet.session.viewholder;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.entity.RedEnvelopeEntity;
import com.netease.nim.uikit.app.extension.RedEnvelopeAttachment;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.yuejian.meet.R;

/**
 * 群聊头部提示
 * Created by zh03 on 2017/8/21.
 */

public class MsgViewHlderGroupHeaderHint extends MsgViewHolderBase {

    TextView group_message_header_hint;
    public MsgViewHlderGroupHeaderHint(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.item_group_message_header;
    }

    @Override
    protected void inflateContentView() {
        group_message_header_hint=findViewById(R.id.group_message_header_hint);
        String statrString="我们提倡文明聊天，群聊中不允许出现低俗、暴力或侮辱他人的聊天内容。详情见";
        String sendString="《约见用户行为规范》";
        String str=statrString+sendString;
        final SpannableStringBuilder sp = new  SpannableStringBuilder(str);
        sp.setSpan(new ForegroundColorSpan(0xFF84BDE8),statrString.length(),str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        group_message_header_hint.setText(sp);
    }

    @Override
    protected boolean isShowHeadImage() {
        return false;
    }

    @Override
    protected void onItemClick() {

    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }

    @Override
    public Boolean isShowTime() {
        return false;
    }

    @Override
    protected void bindContentView() {
    }

}
