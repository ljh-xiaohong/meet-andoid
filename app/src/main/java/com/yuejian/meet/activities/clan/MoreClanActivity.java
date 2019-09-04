package com.yuejian.meet.activities.clan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.common.SelectMemberCityActivity;
import com.yuejian.meet.activities.common.SurnameActivity;
import com.yuejian.meet.adapters.clan.MoreClanAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.MoreClanEntity;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 所有宗亲会
 */
public class MoreClanActivity extends BaseActivity implements SpringView.OnFreshListener, OnItemClickListener{
    String city = "";
    Intent intent;
    List<MoreClanEntity> listData=new ArrayList<>();
    @Bind(R.id.more_clan_list)
    ListView listView;
    MoreClanAdapter mAdapter;
    @Bind(R.id.more_city_sel)
    TextView more_city_sel;
    @Bind(R.id.more_province_sel)
    TextView more_province_sel;
    @Bind(R.id.more_surname_sel)
    TextView more_surname_sel;
    int pageIndex = 1;
    String province = "";
    @Bind(R.id.re_spring)
    SpringView springView;
    String surname = "";
    public String zuciId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_clan);
        initData();
    }

    public void initData() {
        this.springView.setHeader(new DefaultHeader(this));
        this.springView.setFooter(new DefaultFooter(this));
        springView.setListener(this);
        setTitleText(getString(R.string.Clansmen_association_all));
        this.mAdapter = new MoreClanAdapter(this.listView, this.listData, R.layout.item_more_clan_list_layout);
        this.listView.setAdapter(this.mAdapter);
        if (getIntent().hasExtra("zuciId")){
            zuciId=getIntent().getStringExtra("zuciId");
            mAdapter.setZuciId(zuciId);
        }
        requstData();
    }

    public void onBusCallback(BusCallEntity event) {
        super.onBusCallback(event);
        if (event.getCallType() == BusEnum.SURNAME_UPDATE) {
            if (event.getData().equals(getString(R.string.surnames_all))){
                surname="";
            }else {
                surname = event.getData();
            }
            more_surname_sel.setText(surname);
            pageIndex = 1;
            requstData();
        } else if (event.getCallType() == BusEnum.district) {
            if (event.getAreaName().equals(getString(R.string.location))){
                province=AppConfig.province;
                city=AppConfig.city;

            }else if (event.getAreaName().equals(getString(R.string.nationwide))){
                province="";
                city="";
            }else {
                city = event.getData();
            }
            more_province_sel.setText(province);
            more_city_sel.setText(city);
            pageIndex = 1;
            requstData();
        } else if (event.getCallType() == BusEnum.CITY) {
            city = event.getData();
            more_city_sel.setText(city);
            pageIndex = 1;
            requstData();
        } else if (event.getCallType() == BusEnum.PROVINCE) {
            province = event.getData();
            more_province_sel.setText(province);
            pageIndex = 1;
            requstData();
        }
    }

    @OnClick({R.id.more_surname_sel, R.id.more_province_sel, R.id.more_city_sel})
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.more_city_sel:
                if (this.province.equals("")) {
                    ViewInject.toast(this, R.string.Select_province3);
                    return;
                }
                    intent = new Intent(this, SelectMemberCityActivity.class);
                    intent.putExtra("regionName", this.province);
                    intent.putExtra("rank", "1");
                    intent.putExtra("city", this.city);
                    intent.putExtra("clanSel", true);
                    startActivityIfNeeded(this.intent, 3);
                break;
            case R.id.more_province_sel:
                this.intent = new Intent(this, SelectMemberCityActivity.class);
                if (StringUtils.isAutonomy(AppConfig.province)) {
                    this.intent.putExtra("city", AppConfig.province);
                }
                this.intent.putExtra("clanSel", true);
                this.intent.putExtra("isSovereignty", false);
                startActivity(this.intent);
                break;
            case R.id.more_surname_sel:
                this.intent = new Intent(this, SurnameActivity.class);
                if (this.user != null) {
                    this.intent.putExtra("meSurname", this.user.getSurname());
                }
                this.intent.putExtra("meSurname", getString(R.string.surnames_all));
                startActivity(this.intent);
                break;
        }
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {}

    public void onLoadmore() {
        this.pageIndex += 1;
        requstData();
    }

    public void onRefresh() {
        this.pageIndex = 1;
        requstData();
    }

    public void requstData(){
        if (!StringUtil.isEmpty(zuciId)){
            requstZuidQuery();
            return;
        }
        HashMap localHashMap = new HashMap();
        localHashMap.put("association_surname", this.surname);
        localHashMap.put("association_province", this.province);
        localHashMap.put("association_city", this.city);
        localHashMap.put("pageIndex", this.pageIndex + "");
        localHashMap.put("pageItemCount", "20");
        localHashMap.put("customer_lng", AppConfig.slongitude);
        localHashMap.put("customer_lat", AppConfig.slatitude);
        localHashMap.put("customer_id", AppConfig.CustomerId);
        this.apiImp.findClanMore(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                if (pageIndex==1){
                    listData.clear();
                }
                MoreClanActivity.this.listData.addAll(JSON.parseArray(paramAnonymousString, MoreClanEntity.class));
                MoreClanActivity.this.mAdapter.refresh(MoreClanActivity.this.listData);
            }
        });
    }
    public void requstZuidQuery(){
        Map<String,Object> params=new HashMap<>();
        params.put("association_surname", this.surname);
        params.put("association_province", this.province);
        params.put("association_city", this.city);
        params.put("pageIndex", this.pageIndex + "");
        params.put("pageItemCount", "20");
        params.put("lng", AppConfig.slongitude);
        params.put("lat", AppConfig.slatitude);
        params.put("customer_id", AppConfig.CustomerId);
        params.put("hall_id", AppConfig.CustomerId);
        apiImp.zucuIdQueryClan(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (pageIndex==1){
                    listData.clear();
                }
                MoreClanActivity.this.listData.addAll(JSON.parseArray(data, MoreClanEntity.class));
                MoreClanActivity.this.mAdapter.refresh(MoreClanActivity.this.listData);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
}
