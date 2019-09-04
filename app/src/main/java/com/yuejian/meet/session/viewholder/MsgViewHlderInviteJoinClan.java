package com.yuejian.meet.session.viewholder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.RedEnvelopeEntity;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.clan.ClanInfoActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.MoreClanEntity;
import com.yuejian.meet.session.extension.InviteJoinClanAttachment;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MsgViewHlderInviteJoinClan extends MsgViewHolderBase {
    ProgressDialog dialog;
    LinearLayout invite_join_group_layout;
    LinearLayout invite_join_left_layout;
    LinearLayout invite_join_right_layout;
    TextView op_invite_clan_content;

    public MsgViewHlderInviteJoinClan(BaseMultiItemFetchLoadAdapter paramBaseMultiItemFetchLoadAdapter) {
        super(paramBaseMultiItemFetchLoadAdapter);
    }

    protected void bindContentView() {
        this.invite_join_left_layout.setVisibility(View.GONE);
        this.invite_join_right_layout.setVisibility(View.GONE);
        if (this.message.getDirect() == MsgDirectionEnum.In) {
            this.invite_join_left_layout.setVisibility(View.VISIBLE);
            RedEnvelopeEntity localRedEnvelopeEntity = (RedEnvelopeEntity)JSON.parseObject(((InviteJoinClanAttachment)this.message.getAttachment()).getData(), RedEnvelopeEntity.class);
            this.op_invite_clan_content.setText(localRedEnvelopeEntity.getContent());
            return;
        }
        this.invite_join_right_layout.setVisibility(View.VISIBLE);
    }

    protected int getContentResId() {
        return R.layout.nim_invite_clan_join_msg_view_layout;
    }

    protected void inflateContentView() {
        this.op_invite_clan_content = ((TextView)findViewById(R.id.op_invite_clan_content));
        this.invite_join_group_layout = ((LinearLayout)findViewById(R.id.invite_join_group_layout));
        this.invite_join_left_layout = ((LinearLayout)findViewById(R.id.invite_join_left_layout));
        this.invite_join_right_layout = ((LinearLayout)findViewById(R.id.invite_join_right_layout));
    }

    protected boolean isShowGiftBg() {
        return true;
    }

    protected void onItemClick() {
        RedEnvelopeEntity localRedEnvelopeEntity = (RedEnvelopeEntity)JSON.parseObject(((InviteJoinClanAttachment)this.message.getAttachment()).getData(), RedEnvelopeEntity.class);
        HashMap localHashMap = new HashMap();
        localHashMap.put("association_id", localRedEnvelopeEntity.getAssociation_id());
        new ApiImp().isClanDissolve(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt)
            {
                MsgViewHlderInviteJoinClan.this.toClan();
            }
        });
    }

    public void toClan() {
        final RedEnvelopeEntity localObject1 = JSON.parseObject(((InviteJoinClanAttachment)this.message.getAttachment()).getData(), RedEnvelopeEntity.class);
        if (this.message.getDirect() == MsgDirectionEnum.Out) {
            MoreClanEntity moreClanEntity = new MoreClanEntity();
            moreClanEntity.setAssociation_id(localObject1.getAssociation_id());
            Intent intent = new Intent(this.context, ClanInfoActivity.class);
            intent.putExtra("clanEntity", moreClanEntity);
            this.context.startActivity(intent);
            return;
        }
        this.dialog = new ProgressDialog(this.context);
        this.dialog.setProgressStyle(0);
        this.dialog.setTitle(null);
        this.dialog.setIcon(null);
        this.dialog.setMessage("正在操作..");
        this.dialog.setCancelable(true);
        this.dialog.show();
        Map<String,Object> params = new HashMap();
        params.put("association_id", ((RedEnvelopeEntity)localObject1).getAssociation_id());
        params.put("invite_customer_id", AppConfig.CustomerId);
        new ApiImp().addClanMember(params, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
                if (MsgViewHlderInviteJoinClan.this.dialog != null) {
                    MsgViewHlderInviteJoinClan.this.dialog.dismiss();
                }
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                MoreClanEntity moreClanEntity = new MoreClanEntity();
                moreClanEntity.setAssociation_id(localObject1.getAssociation_id());
                Intent localIntent = new Intent(MsgViewHlderInviteJoinClan.this.context, ClanInfoActivity.class);
                localIntent.putExtra("clanEntity", moreClanEntity);
                MsgViewHlderInviteJoinClan.this.context.startActivity(localIntent);
                if (MsgViewHlderInviteJoinClan.this.dialog != null) {
                    MsgViewHlderInviteJoinClan.this.dialog.dismiss();
                }
            }
        });
    }
}
