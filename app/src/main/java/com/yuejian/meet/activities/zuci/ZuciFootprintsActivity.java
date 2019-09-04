package com.yuejian.meet.activities.zuci;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.clan.ZuciFootprintsAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ZuciFootprintsEntity;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 足迹
 */
public class ZuciFootprintsActivity extends BaseActivity implements SpringView.OnFreshListener {
    String id;
    List<ZuciFootprintsEntity> listData = new ArrayList();
    @Bind(R.id.footprints_list)
    ListView listView;
    ZuciFootprintsAdapter mAdapter;
    int pageIndex = 1;
    @Bind(R.id.zuci_footprints_spring)
    SpringView springView;

    public void initData() {
        this.springView.setFooter(new DefaultFooter(this));
        this.springView.setHeader(new DefaultHeader(this));
        this.springView.setListener(this);
        setTitleText(getString(R.string.footprint));
        this.mAdapter = new ZuciFootprintsAdapter(this.listView, this.listData, R.layout.item_zuci_footprints_list_layout);
        this.listView.setAdapter(this.mAdapter);
        requstData();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_zuci_footprints);
        this.id = getIntent().getStringExtra("id");
        initData();
    }

    public void onLoadmore() {
        this.pageIndex += 1;
        requstData();
    }

    public void onRefresh() {
        this.pageIndex = 1;
        requstData();
    }

    public void requstData() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("id", this.id);
        localHashMap.put("pageIndex", this.pageIndex + "");
        localHashMap.put("pageItemCount", "20");
        this.apiImp.zuciFootprints(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                if (ZuciFootprintsActivity.this.pageIndex == 1) {
                    ZuciFootprintsActivity.this.listData.clear();
                }
                ZuciFootprintsActivity.this.listData.addAll(JSON.parseArray(paramAnonymousString, ZuciFootprintsEntity.class));
                ZuciFootprintsActivity.this.mAdapter.refresh(ZuciFootprintsActivity.this.listData);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
