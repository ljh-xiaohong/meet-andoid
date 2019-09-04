package com.yuejian.meet.activities.zuci;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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
import com.yuejian.meet.activities.search.MoreSearchActivity;
import com.yuejian.meet.adapters.clan.ZuciCollectionAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ZuciCollectionEntity;
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
 * 我的收藏
 */
public class ZuciCollectionActivity extends BaseActivity implements SpringView.OnFreshListener {
    String city = "";
    @Bind(R.id.collection_city_sel)
    TextView collection_city_sel;
    @Bind(R.id.collection_province_sel)
    TextView collection_province_sel;
    @Bind(R.id.collection_surname_sel)
    TextView collection_surname_sel;
    Intent intent;
    List<ZuciCollectionEntity> listData = new ArrayList();
    @Bind(R.id.collection_list)
    ListView listView;
    ZuciCollectionAdapter mAdapter;
    int pageIndex = 1;
    String province = "";
    SpringView springView;
    String surname = "";
    @Bind(R.id.titlebar_imgBtn_search)
    ImageButton titlebar_imgBtn_search;

    public void initData() {
        setTitleText(getString(R.string.my_favorite));
        this.springView.setHeader(new DefaultHeader(this));
        this.springView.setFooter(new DefaultFooter(this));
        this.springView.setListener(this);
        this.titlebar_imgBtn_search.setVisibility(View.VISIBLE);
        this.titlebar_imgBtn_search.setImageDrawable(getResources().getDrawable(R.mipmap.icon_nav_seach));
        this.mAdapter = new ZuciCollectionAdapter(this.listView, this.listData, R.layout.item_collect_list_layout);
        this.listView.setAdapter(this.mAdapter);
        requstData();
    }

    public void onBusCallback(BusCallEntity event) {
      if (event.getCallType() == BusEnum.SURNAME_UPDATE) {
          if (event.getData().equals(getString(R.string.home_all_name))){
              surname="";
          }else {
              surname = event.getData();
          }
            collection_surname_sel.setText(surname);
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
            collection_province_sel.setText(province);
            collection_city_sel.setText(city);
            pageIndex = 1;
            requstData();
        } else if (event.getCallType() == BusEnum.CITY) {
            city = event.getData();
            collection_city_sel.setText(city);
            pageIndex = 1;
            requstData();
        } else if (event.getCallType() == BusEnum.PROVINCE) {
            province = event.getData();
            collection_province_sel.setText(province);
            pageIndex = 1;
            requstData();
        }else if (event.getCallType() == BusEnum.ZUCI_SELECT){
          finish();
      }
    }

    @OnClick({R.id.collection_city_sel, R.id.collection_province_sel, R.id.collection_surname_sel, R.id.titlebar_imgBtn_search})
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            default:
            case R.id.titlebar_imgBtn_search:
                this.intent = new Intent(this, MoreSearchActivity.class);
                this.intent.putExtra("type", 2);
                this.intent.putExtra("content", "");
//                this.intent.putExtra("meSurname", "全部");
                startActivity(this.intent);
                break;
            case R.id.collection_surname_sel:
                this.intent = new Intent(this, SurnameActivity.class);
                if (this.user != null) {
                    this.intent.putExtra("meSurname", this.user.getSurname());
                }
                startActivity(this.intent);
                break;
            case R.id.collection_province_sel:
                this.intent = new Intent(this, SelectMemberCityActivity.class);
                if (!StringUtils.isEmail(AppConfig.province)) {
                    this.intent.putExtra("city", AppConfig.province);
                }
                this.intent.putExtra("clanSel", true);
                this.intent.putExtra("isSovereignty", false);
                startActivity(this.intent);
                break;
            case R.id.collection_city_sel:
                if (StringUtil.isEmpty(province)){
                    ViewInject.toast(this, R.string.Select_province3);
                    return;
                }
                this.intent = new Intent(this, SelectMemberCityActivity.class);
                if (!StringUtils.isEmail(AppConfig.province)) {
                    this.intent.putExtra("city", AppConfig.city);
                }
                intent.putExtra("regionName",province);
                this.intent.putExtra("clanSel", true);
                this.intent.putExtra("rank", "1");
                this.intent.putExtra("isSovereignty", false);
                startActivity(this.intent);
                break;
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_zuci_collection);
        this.springView = ((SpringView)findViewById(R.id.collection_spring));
        initData();
    }

    public void onLoadmore()
    {
        this.pageIndex += 1;
        requstData();
    }

    public void onRefresh() {
        this.pageIndex = 1;
        requstData();
    }

    public void postCollection(final int paramInt) {
        Builder localBuilder = new Builder(this);
        localBuilder.setTitle(R.string.hint);
        localBuilder.setMessage(R.string.Should_cancel_the_collection);
        localBuilder.setNeutralButton(R.string.cancel, null);
        localBuilder.setNegativeButton(R.string.confirm, new OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                Map<String,Object> params= new HashMap();
                params.put("type", "1");
                params.put("relation_id", ((ZuciCollectionEntity)ZuciCollectionActivity.this.listData.get(paramInt)).getClan_hall_id());
                params.put("customer_id", ZuciCollectionActivity.this.user.getCustomer_id());
                ZuciCollectionActivity.this.apiImp.collection(params, this, new DataIdCallback<String>()
                {
                    public void onFailed(String paramAnonymous2String1, String paramAnonymous2String2, int paramAnonymous2Int) {}

                    public void onSuccess(String paramAnonymous2String, int paramAnonymous2Int)
                    {
                        ZuciCollectionActivity.this.listData.remove(paramInt);
                        ZuciCollectionActivity.this.mAdapter.refresh(ZuciCollectionActivity.this.listData);
                    }
                });
            }
        });
        localBuilder.show();
    }

    public void postTop(final int paramInt) {
        HashMap localHashMap = new HashMap();
        localHashMap.put("clan_hall_id", ((ZuciCollectionEntity)this.listData.get(paramInt)).getClan_hall_id());
        localHashMap.put("customer_id", AppConfig.CustomerId);
        this.apiImp.CollectionTopRoDrop(localHashMap, this, new DataIdCallback<String>()
        {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt)
            {
                ((ZuciCollectionEntity)ZuciCollectionActivity.this.listData.get(paramInt)).setIs_top(paramAnonymousString);
                ZuciCollectionActivity.this.mAdapter.refresh(ZuciCollectionActivity.this.listData);
            }
        });
    }

    public void requstData()
    {
        HashMap localHashMap = new HashMap();
        localHashMap.put("customer_id", this.user.getCustomer_id());
        localHashMap.put("pageIndex", this.pageIndex + "");
        localHashMap.put("pageItemCount", "20");
        localHashMap.put("surname", this.surname);
        localHashMap.put("province", this.province);
        localHashMap.put("city", this.city);
        localHashMap.put("longitude", AppConfig.slongitude);
        localHashMap.put("latitude", AppConfig.slatitude);
        this.apiImp.ZuciCollection(localHashMap, this, new DataIdCallback<String>()
        {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String data, int paramAnonymousInt)
            {
                if (ZuciCollectionActivity.this.pageIndex == 1) {
                   listData.clear();
                }
                listData.addAll(JSON.parseArray(data, ZuciCollectionEntity.class));
                mAdapter.refresh(ZuciCollectionActivity.this.listData);
            }
        });
    }
}
