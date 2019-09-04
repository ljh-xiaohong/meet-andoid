package com.yuejian.meet.activities.clan;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.clan.ClanProceedsInfoAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ClanChargeEntity;
import com.yuejian.meet.bean.ClanMemberChargeEntity;
import com.yuejian.meet.bean.ClanMiAllEntity;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

public class ClanProceedsInfoActivity extends BaseActivity {
    String chargeId;
    ClanChargeEntity clanChargeEntity;
    List<ClanMemberChargeEntity> listData;
    @Bind(R.id.proceeds_list)
    ListView listView;
    ClanProceedsInfoAdapter mAdapter;
    View viewHead;

    public void initData() {
        this.viewHead = View.inflate(this, R.layout.item_clan_proceeds_info_head_layout, null);
        setTitleText("收费详情");
        this.mAdapter = new ClanProceedsInfoAdapter(this.listView, this.listData, R.layout.item_clan_proceeds_info_list_layout);
        this.listView.setAdapter(this.mAdapter);
        this.listView.addHeaderView(this.viewHead);
        requstData();
    }

    public void onClick(View paramView) {}

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_clan_proceeds_info);
        this.chargeId = getIntent().getStringExtra("chargeId");
        initData();
    }

    public void requstData() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("charge_id", this.chargeId);
        this.apiImp.clanChargePayInfo(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                ClanMiAllEntity  clanMiAllEntity= JSON.parseObject(paramAnonymousString, ClanMiAllEntity.class);
                clanChargeEntity = ((ClanChargeEntity) JSON.parseObject(clanMiAllEntity.getChargeInfo(), ClanChargeEntity.class));
                listData = JSON.parseArray(clanMiAllEntity.getcList(), ClanMemberChargeEntity.class);
                mAdapter.refresh(listData);
                setViewHeadData();
            }
        });
    }

    public void setViewHeadData() {
        ((TextView)this.viewHead.findViewById(R.id.proceeds_sun)).setText(this.clanChargeEntity.getCharge_cnt());
        ((TextView)this.viewHead.findViewById(R.id.proceeds_cnt)).setText(this.clanChargeEntity.getCharge_pay_cnt());
        ((TextView)this.viewHead.findViewById(R.id.proceeds_no_cnt)).setText(Integer.parseInt(this.clanChargeEntity.getCharge_cnt()) - Integer.parseInt(this.clanChargeEntity.getCharge_pay_cnt()) + "");
    }
}
