package com.yuejian.meet.activities.zuci;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.common.SelectMemberCityActivity;
import com.yuejian.meet.activities.common.SurnameActivity;
import com.yuejian.meet.activities.search.MoreSearchActivity;
import com.yuejian.meet.adapters.clan.ZuciCollectionAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ZuciCollectionEntity;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 附近祖祠
 */
public class ZuciNearbyActivity extends BaseActivity implements SpringView.OnFreshListener {
    String city = "";
    Intent intent;
    List<ZuciCollectionEntity> listData = new ArrayList();
    @Bind(R.id.nearby_list)
    ListView listView;
    ZuciCollectionAdapter mAdapter;
    @Bind(R.id.nearby_city_sel)
    TextView nearby_city_sel;
    @Bind(R.id.nearby_province_sel)
    TextView nearby_province_sel;
    @Bind(R.id.nearby_surname_sel)
    TextView nearby_surname_sel;
    int pageIndex = 1;
    String province = "";
    SpringView springView;
    String surname = "";
    @Bind(R.id.titlebar_imgBtn_search)
    ImageButton titlebar_imgBtn_search;
    public String bindingId="";
    public Boolean isBanin=false;


    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_zuci_nearby);
        if (getIntent().hasExtra("bindingId")){
            bindingId=getIntent().getStringExtra("bindingId");
            isBanin=getIntent().getBooleanExtra("isBanin",false);
        }
        initData();
    }

    public void initData() {
        setTitleText(getString(R.string.Ancestral_shrine_near));
        this.titlebar_imgBtn_search.setVisibility(View.VISIBLE);
        this.titlebar_imgBtn_search.setImageDrawable(getResources().getDrawable(R.mipmap.icon_nav_seach));
        this.mAdapter = new ZuciCollectionAdapter(this.listView, this.listData, R.layout.item_collect_list_layout);
        mAdapter.setBandinId(bindingId,isBanin);
        this.listView.setAdapter(this.mAdapter);
        mAdapter.setBandinId(bindingId,isBanin);
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
            nearby_surname_sel.setText(surname);
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
            nearby_province_sel.setText(province);
            nearby_city_sel.setText(city);
            pageIndex = 1;
            requstData();
        } else if (event.getCallType() == BusEnum.CITY) {
            city = event.getData();
            nearby_city_sel.setText(city);
            pageIndex = 1;
            requstData();
        } else if (event.getCallType() == BusEnum.PROVINCE) {
            province = event.getData();
            nearby_province_sel.setText(province);
            pageIndex = 1;
            requstData();
        }
    }

    @OnClick({R.id.nearby_city_sel, R.id.nearby_province_sel, R.id.nearby_surname_sel, R.id.titlebar_imgBtn_search})
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            default:
            case R.id.nearby_city_sel:
                if (this.province.equals("")) {
                ViewInject.toast(this, R.string.Select_province3);
                return;
                }
                this.intent = new Intent(this, SelectMemberCityActivity.class);
                this.intent.putExtra("regionName", this.province);
                this.intent.putExtra("rank", "1");
                this.intent.putExtra("city", this.city);
                this.intent.putExtra("clanSel", true);
                startActivityIfNeeded(this.intent, 3);
                break;
            case R.id.nearby_surname_sel:
                this.intent = new Intent(this, SurnameActivity.class);
                if (this.user != null) {
                    this.intent.putExtra("meSurname", this.user.getSurname());
                }
                this.intent.putExtra("meSurname", getString(R.string.surnames_all));
                startActivity(this.intent);
                break;
            case R.id.nearby_province_sel:
                this.intent = new Intent(this, SelectMemberCityActivity.class);
                if (StringUtils.isAutonomy(AppConfig.province)) {
                    this.intent.putExtra("city", AppConfig.province);
                }
                this.intent.putExtra("clanSel", true);
                this.intent.putExtra("isSovereignty", false);
                startActivity(this.intent);
                break;

            case R.id.titlebar_imgBtn_search:
                this.intent = new Intent(this, MoreSearchActivity.class);
                this.intent.putExtra("type", 2);
                this.intent.putExtra("content", "");
                if (isBanin){
                    intent.putExtra("bindingId",bindingId);
                    intent.putExtra("isBanin",isBanin);
                }
                startActivity(this.intent);
                break;
        }

    }

    public void onLoadmore() {
        this.pageIndex += 1;
        requstData();
    }

    public void onRefresh() {
        this.pageIndex = 1;
        requstData();
    }

    public void postCollection(final int paramInt) {
        HashMap localHashMap = new HashMap();
        localHashMap.put("type", "1");
        localHashMap.put("relation_id", ((ZuciCollectionEntity)this.listData.get(paramInt)).getClan_hall_id());
        localHashMap.put("customer_id", this.user.getCustomer_id());
        this.apiImp.collection(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt)
            {
                ((ZuciCollectionEntity)ZuciNearbyActivity.this.listData.get(paramInt)).setIs_collection(paramAnonymousString);
                ZuciNearbyActivity.this.mAdapter.refresh(ZuciNearbyActivity.this.listData);
            }
        });
    }

    public void postTop(final int paramInt) {
        HashMap localHashMap = new HashMap();
        localHashMap.put("clan_hall_id", ((ZuciCollectionEntity)this.listData.get(paramInt)).getId());
        localHashMap.put("customer_id", AppConfig.CustomerId);
        this.apiImp.CollectionTopRoDrop(localHashMap, this, new DataIdCallback<String>()
        {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt)
            {
                ((ZuciCollectionEntity)ZuciNearbyActivity.this.listData.get(paramInt)).setIs_top(paramAnonymousString);
                ZuciNearbyActivity.this.mAdapter.refresh(ZuciNearbyActivity.this.listData);
            }
        });
    }

    public void requstData() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("longitude", AppConfig.slongitude);
        localHashMap.put("latitude", AppConfig.slatitude);
        localHashMap.put("surname", this.surname);
        localHashMap.put("province", this.province);
        localHashMap.put("city", this.city);
        localHashMap.put("pageIndex", this.pageIndex + "");
        localHashMap.put("pageItemCount", "20");
        this.apiImp.zuciNearby(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                if (pageIndex == 1) {
                    listData.clear();
                }
                listData.addAll(JSON.parseArray(paramAnonymousString, ZuciCollectionEntity.class));
                mAdapter.refresh(ZuciNearbyActivity.this.listData);
            }
        });
    }
}
