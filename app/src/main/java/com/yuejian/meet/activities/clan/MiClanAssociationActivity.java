package com.yuejian.meet.activities.clan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.clan.MiClanAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ClanMiAllEntity;
import com.yuejian.meet.bean.MoreClanEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的宗亲会
 */
public class MiClanAssociationActivity extends BaseActivity implements OnItemClickListener {
    @Bind(R.id.add_clan)
    TextView add_clan;
    List<MoreClanEntity> listData = new ArrayList();
    @Bind(R.id.mi_clan_list)
    ListView listView;
    MiClanAdapter mAdapter;
    @Bind(R.id.no_clan_layout)
    RelativeLayout no_clan_layout;
    @Bind(R.id.txt_titlebar_recorder_again)
    TextView txt_titlebar_recorder_again;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_mi_clan_association);
        initData();
    }

    public void initData() {
        setTitleText(getString(R.string.My_clan_association));
        this.txt_titlebar_recorder_again.setText(R.string.careate_clan);
        txt_titlebar_recorder_again.setVisibility(View.VISIBLE);
        this.mAdapter = new MiClanAdapter(this.listView, this.listData, R.layout.item_more_clan_list_layout);
        this.listView.setAdapter(this.mAdapter);
        requstData();
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
    }

    public void onBusCallback(BusCallEntity paramBusCallEntity) {
        super.onBusCallback(paramBusCallEntity);
        if (paramBusCallEntity.getCallType() == BusEnum.CLAN_UPDATE) {
            requstData();
        }
    }

    @OnClick({R.id.create_clan, R.id.add_clan, R.id.txt_titlebar_recorder_again})
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.create_clan:
                startActivityForResult(new Intent(this, CreateMiClanActivity.class), 110);
                break;
            case R.id.add_clan:
//                startActivityForResult(new Intent(this, CreateMiClanActivity.class), 110);
                this.add_clan.getText().toString().equals(getString(R.string.common_btn_txt));
                startActivity(new Intent(this, MoreClanActivity.class));
                break;
            case R.id.txt_titlebar_recorder_again:
                startActivityForResult(new Intent(this, CreateMiClanActivity.class), 110);
                break;
        }
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {}

    public void requstData() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("customer_id", AppConfig.CustomerId);
        localHashMap.put("customer_lng", AppConfig.slongitude);
        localHashMap.put("customer_lat", AppConfig.slatitude);
        this.apiImp.MyClan(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                MiClanAssociationActivity.this.listData.clear();
                ClanMiAllEntity clanMiAllEntity = JSON.parseObject(paramAnonymousString, ClanMiAllEntity.class);
                List localList = JSON.parseArray(clanMiAllEntity.getCreateList(), MoreClanEntity.class);
                Iterator localIterator = localList.iterator();
                while (localIterator.hasNext()) {
                    ((MoreClanEntity)localIterator.next()).setIs_MiClan("Y");
                }
                if (localList.size() > 0) {
                    txt_titlebar_recorder_again.setVisibility(View.GONE);
                }
                listData.addAll(localList);
                if (listData.size()<=0){
                    return;
                }
                listData.addAll(JSON.parseArray(clanMiAllEntity.getAddList(), MoreClanEntity.class));
                listView.setVisibility(View.VISIBLE);
                mAdapter.refresh(MiClanAssociationActivity.this.listData);
            }
        });
    }

    public void requstMyClanType()
    {
        HashMap localHashMap = new HashMap();
        localHashMap.put("customer_id", AppConfig.CustomerId);
        this.apiImp.isCreateClan(localHashMap, this, new DataIdCallback<String>()
        {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt)
            {
                if (paramAnonymousString.equals("0"))
                {
                    MiClanAssociationActivity.this.requstData();
                    MiClanAssociationActivity.this.listView.setVisibility(View.VISIBLE);
                    return;
                }
                if (paramAnonymousString.equals("-1"))
                {
                    MiClanAssociationActivity.this.listView.setVisibility(View.GONE);
                    return;
                }
                MiClanAssociationActivity.this.listView.setVisibility(View.GONE);
            }
        });
    }
}
