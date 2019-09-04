package com.yuejian.meet.session.viewholder;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.RedEnvelopeEntity;
import com.netease.nim.uikit.app.extension.RedEnvelopeAttachment;
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
 * 发入群礼物
 * Created by zh03 on 2017/8/21.
 */

public class MsgViewHlderInviteJoinGroup extends MsgViewHolderBase {

    TextView invite_join_content;
    LinearLayout invite_join_group_layout;///layout  用来设置背景
    LinearLayout invite_join_left_layout,invite_join_right_layout;

    ProgressDialog dialog;
    public MsgViewHlderInviteJoinGroup(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_invite_join_msg_view_layout;
    }

    @Override
    protected void inflateContentView() {
        invite_join_content=findViewById(R.id.invite_join_content);
        invite_join_group_layout=findViewById(R.id.invite_join_group_layout);
        invite_join_left_layout=findViewById(R.id.invite_join_left_layout);
        invite_join_right_layout=findViewById(R.id.invite_join_right_layout);
    }

    @Override
    protected void bindContentView() {
        invite_join_left_layout.setVisibility(View.GONE);
        invite_join_right_layout.setVisibility(View.GONE);
//        invite_join_group_layout.setBackgroundResource(message.getDirect()== MsgDirectionEnum.In?R.drawable.nim_message_left_transparent_bg:R.drawable.nim_message_right_transparent_bg);
        if (message.getDirect()== MsgDirectionEnum.In){//接收
            invite_join_left_layout.setVisibility(View.VISIBLE);
            InviteJoinAttachment envelopeAttachment= (InviteJoinAttachment) message.getAttachment();
            RedEnvelopeEntity envelopeEntity= JSON.parseObject(envelopeAttachment.getData(),RedEnvelopeEntity.class);
            invite_join_content.setText(envelopeEntity.getGift_name()+"×1");
        }else {
            invite_join_right_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected boolean isShowGiftBg() {
        return true;
    }

    @Override
    protected void onItemClick() {
        dialog = new ProgressDialog(context);
        //设置风格为圆形
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(null);
        dialog.setIcon(null);
        //设置提示信息
        dialog.setMessage("正在操作..");
        //设置是否可以通过返回键取消
        dialog.setCancelable(true);
        dialog.show();
        InviteJoinAttachment envelopeAttachment= (InviteJoinAttachment) message.getAttachment();
        final RedEnvelopeEntity envelopeEntity= JSON.parseObject(envelopeAttachment.getData(),RedEnvelopeEntity.class);
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        params.put("t_id",envelopeEntity.getT_id());
        new ApiImp().intoGroup(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ImUtils.toTeamSession(context,envelopeEntity.getT_id(),data);
                if (dialog!=null)dialog.dismiss();
            }
            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog!=null)dialog.dismiss();
            }
        });
    }
}
