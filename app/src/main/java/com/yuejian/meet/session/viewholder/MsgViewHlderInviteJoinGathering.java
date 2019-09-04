package com.yuejian.meet.session.viewholder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.entity.RedEnvelopeEntity;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.clan.ClanInfoActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.ClanChargeEntity;
import com.yuejian.meet.bean.MoreClanEntity;
import com.yuejian.meet.session.extension.InviteJoinGatheringAttachment;
import java.util.HashMap;
import java.util.Map;

public class MsgViewHlderInviteJoinGathering extends MsgViewHolderBase {
    TextView charge_content;
    TextView charge_fee;
    TextView charge_tiem;
    ProgressDialog dialog;
    LinearLayout invite_join_group_layout;
    LinearLayout invite_join_left_layout;
    LinearLayout invite_join_right_layout;
    TextView op_charge_content;
    TextView op_charge_fee;
    TextView op_charge_tiem;

    public MsgViewHlderInviteJoinGathering(BaseMultiItemFetchLoadAdapter paramBaseMultiItemFetchLoadAdapter) {
        super(paramBaseMultiItemFetchLoadAdapter);
    }

    protected void bindContentView() {
        this.invite_join_left_layout.setVisibility(View.GONE);
        this.invite_join_right_layout.setVisibility(View.GONE);
        ClanChargeEntity localClanChargeEntity = (ClanChargeEntity)JSON.parseObject(((RedEnvelopeEntity)JSON.parseObject(((InviteJoinGatheringAttachment)this.message.getAttachment()).getData(), RedEnvelopeEntity.class)).getCharge(), ClanChargeEntity.class);
        if (this.message.getDirect() == MsgDirectionEnum.In) {
            this.invite_join_left_layout.setVisibility(View.VISIBLE);
            this.op_charge_content.setText(localClanChargeEntity.getCharge_cnt());
            this.op_charge_fee.setText(localClanChargeEntity.getCharge_price() + "/人");
            this.op_charge_tiem.setText("截止时间" + localClanChargeEntity.getCharge_endtime());
            return;
        }
        this.charge_content.setText(localClanChargeEntity.getCharge_cnt());
        this.charge_fee.setText(localClanChargeEntity.getCharge_price() + "/人");
        this.charge_tiem.setText("截止时间" + localClanChargeEntity.getCharge_endtime());
        this.invite_join_right_layout.setVisibility(View.VISIBLE);
    }

    protected int getContentResId() {
        return R.layout.nim_invite_gathering_join_msg_view_layout;
    }

    protected void inflateContentView() {
        this.op_charge_content = ((TextView)findViewById(R.id.op_charge_content));
        this.op_charge_fee = ((TextView)findViewById(R.id.op_charge_fee));
        this.op_charge_tiem = ((TextView)findViewById(R.id.op_charge_tiem));
        this.invite_join_group_layout = ((LinearLayout)findViewById(R.id.invite_join_group_layout));
        this.invite_join_left_layout = ((LinearLayout)findViewById(R.id.invite_join_left_layout));
        this.invite_join_right_layout = ((LinearLayout)findViewById(R.id.invite_join_right_layout));
        this.charge_content = ((TextView)findViewById(R.id.charge_content));
        this.charge_fee = ((TextView)findViewById(R.id.charge_fee));
        this.charge_tiem = ((TextView)findViewById(R.id.charge_tiem));
    }

    protected boolean isShowGiftBg() {
        return true;
    }

    protected void onItemClick() {
        RedEnvelopeEntity localRedEnvelopeEntity = (RedEnvelopeEntity)JSON.parseObject(((InviteJoinGatheringAttachment)this.message.getAttachment()).getData(), RedEnvelopeEntity.class);
        final ClanChargeEntity localClanChargeEntity = (ClanChargeEntity)JSON.parseObject(localRedEnvelopeEntity.getCharge(), ClanChargeEntity.class);
        HashMap localHashMap = new HashMap();
        localHashMap.put("association_id", localRedEnvelopeEntity.getAssociation_id());
        new ApiImp().isClanDissolve(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt)
            {
                MoreClanEntity moreClanEntity = new MoreClanEntity();
                moreClanEntity.setAssociation_id(localClanChargeEntity.getAssociation_id());
                Intent localIntent = new Intent(MsgViewHlderInviteJoinGathering.this.context, ClanInfoActivity.class);
                localIntent.putExtra("clanEntity", moreClanEntity);
                MsgViewHlderInviteJoinGathering.this.context.startActivity(localIntent);
            }
        });
    }
}
