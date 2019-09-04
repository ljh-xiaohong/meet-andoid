package com.yuejian.meet.activities.clan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.clan.ClanProceedsdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ClanChargeEntity;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ClanProceedsActivity
        extends BaseActivity
{
    String clanId;
    List<ClanChargeEntity> listData;
    @Bind(R.id.proceeds_list)
    ListView listView;
    ClanProceedsdapter mAdapter;
    @Bind(R.id.txt_titlebar_release)
    TextView txt_titlebar_release;

    public void initData() {
        setTitleText("收款");
        this.txt_titlebar_release.setVisibility(View.VISIBLE);
        this.txt_titlebar_release.setTextColor(Color.parseColor("#ffffff"));
        this.txt_titlebar_release.setText("发起收款");
        this.mAdapter = new ClanProceedsdapter(this.listView, this.listData, R.layout.item_clan_proceeds_list_layout);
        this.listView.setAdapter(mAdapter);
        requstData();
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        if (paramInt2 == -1) {
            requstData();
        }
    }

    @OnClick(R.id.txt_titlebar_release)
    public void onClick(View paramView) {
        switch (paramView.getId()){
            case R.id.txt_titlebar_release:
                Intent intent = new Intent(this, StartProceedsActivity.class);
                intent.putExtra("clanId", this.clanId);
                startActivityForResult(intent, 11);
                return;
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_clan_proceeds);
        this.clanId = getIntent().getStringExtra("clanId");
        initData();
    }

    public void requstData() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("association_id", this.clanId);
        this.apiImp.clanChargeList(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
                ClanProceedsActivity.this.finish();
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                ClanProceedsActivity.this.listData = JSON.parseArray(paramAnonymousString, ClanChargeEntity.class);
                ClanProceedsActivity.this.mAdapter.refresh(ClanProceedsActivity.this.listData);
            }
        });
    }
}
