package com.yuejian.meet.activities.zuci;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.ZuciInfoBindingClanAdapter;
import com.yuejian.meet.adapters.ZuciInfoFootprintAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.MoreClanEntity;
import com.yuejian.meet.bean.ZuciEntity;
import com.yuejian.meet.bean.ZuciFootprintsEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

//import com.yuejian.meet.activities.family.MessageBoardActivity;

public class ZuciInfoActivity extends BaseActivity {
    @Bind(R.id.zuci_info_distance)
    TextView distance;
    @Bind(R.id.footprint_recycler)
    RecyclerView footprint_recycler;
    @Bind(R.id.clan_recycler)
    RecyclerView clan_recycler;
    List<ZuciFootprintsEntity> footprintsList = new ArrayList();
    String id;
    Intent intent;
    @Bind(R.id.zuci_info_like)
    TextView like;
    @Bind(R.id.zuci_info_loatoin)
    TextView loatoin;
    ZuciInfoFootprintAdapter mFootprintAdapter;
    @Bind(R.id.zuci_info_name)
    TextView name;
    ZuciEntity zuciEntity;
    public String zuciName;
    @Bind(R.id.zuci_info_collection)
    TextView zuci_info_collection;
    @Bind(R.id.zuci_info_intro)
    TextView zuci_info_intro;
    @Bind(R.id.zuci_info_photo)
    ImageView zuci_info_photo;
    @Bind(R.id.clan_banding_layout)
    LinearLayout clan_banding_layout;
    List<MoreClanEntity> listClan=new ArrayList<>();
    ZuciInfoBindingClanAdapter bandingAdapter;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_zuci_info);
        initData();
    }

    public void initData() {
        this.intent = getIntent();
        this.id = this.intent.getStringExtra("id");
        this.zuciName = this.intent.getStringExtra("zuciName");
        setTitleText(this.zuciName);
        LinearLayoutManager localLinearLayoutManager = new LinearLayoutManager(this);
        localLinearLayoutManager.setOrientation(0);
        this.footprint_recycler.setLayoutManager(localLinearLayoutManager);
        this.mFootprintAdapter = new ZuciInfoFootprintAdapter();
        this.footprint_recycler.setAdapter(this.mFootprintAdapter);

        LinearLayoutManager bandingCalnlayuot = new LinearLayoutManager(this);
        bandingCalnlayuot.setOrientation(0);
        bandingAdapter=new ZuciInfoBindingClanAdapter();
        clan_recycler.setLayoutManager(bandingCalnlayuot);
        clan_recycler.setAdapter(bandingAdapter);
        requstData();
    }

    @OnClick({R.id.zuci_info_like, R.id.zuci_info_collection, R.id.footprint_more, R.id.zuci_comment, R.id.zuci_faxian,R.id.map_link,R.id.clan_banding_layout})
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.zuci_faxian:
                this.intent = new Intent(this, WebActivity.class);
//        paramView = UrlConstant.ExplainURL.FAXIAN + "?customer_id=" + AppConfig.CustomerId;
                this.intent.putExtra("url", UrlConstant.ExplainURL.FAXIAN + "?customer_id=" + AppConfig.CustomerId);
                this.intent.putExtra("No_Title", true);
                startActivity(this.intent);
                return;
            case R.id.zuci_info_like:
                postLike(this.zuciEntity);
                return;
            case R.id.zuci_info_collection:
                postCollection();
                return;
            case R.id.footprint_more:
//                this.intent = new Intent(this, ZuciFootprintsActivity.class);
//                this.intent.putExtra("id", this.zuciEntity.getId());
//                startActivity(this.intent);
                return;
            case R.id.zuci_comment:
//                this.intent = new Intent(this, MessageBoardActivity.class);
//                this.intent.putExtra("id", this.zuciEntity.getId());
//                startActivity(this.intent);
                return;
            case R.id.map_link:
                intent=new Intent(this,ZuciMapNavigationActivity.class);
                intent.putExtra("site",zuciEntity.getProvince()+zuciEntity.getCity()+zuciEntity.getArea()+zuciEntity.getDetailed_place());
                intent.putExtra("latitude",zuciEntity.getLatitude());
                intent.putExtra("longitude",zuciEntity.getLongitude());
                intent.putExtra("zuciName",zuciName);
                intent.putExtra("detailed",zuciEntity.getDetailed_place());
                startActivity(intent);
                break;
            case R.id.clan_banding_layout:
//                intent=new Intent(this, MoreClanActivity.class);
//                intent.putExtra("zuciId",zuciEntity.getId());
//                startActivity(intent);
                break;
        }
    }


    public void postCollection() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("type", "1");
        localHashMap.put("relation_id", this.zuciEntity.getId());
        localHashMap.put("customer_id", this.user.getCustomer_id());
        this.apiImp.collection(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                zuciEntity.setIs_collection(paramAnonymousString);
                zuci_info_collection.setSelected(zuciEntity.getIs_collection().equals("0")?false:true);
                zuci_info_collection.setText(zuciEntity.getIs_collection().equals("0")?getString(R.string.collect):getString(R.string.collect2));
            }
        });
    }

    public void postLike(final ZuciEntity paramZuciEntity) {
        HashMap localHashMap = new HashMap();
        localHashMap.put("id", paramZuciEntity.getId());
        localHashMap.put("customer_id", this.user.getCustomer_id());
        this.apiImp.zuciLike(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String data, int paramAnonymousInt) {
                ZuciEntity localZuciEntity = paramZuciEntity;
                like.setSelected(data.equals("0")?false:true);
                if (data.equals("0")){
                    data = Integer.parseInt(paramZuciEntity.getThumb_cnt()) - 1 + "";
                }else {
                    data = Integer.parseInt(paramZuciEntity.getThumb_cnt()) + 1 + "";
                }
                localZuciEntity.setThumb_cnt(data);
                ZuciInfoActivity.this.like.setText(" " + paramZuciEntity.getThumb_cnt() + getString(R.string.praise2));
            }
        });
    }

    public void requstData() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("id", this.id);
        localHashMap.put("longitude", AppConfig.slongitude);
        localHashMap.put("latitude", AppConfig.slatitude);
        this.apiImp.zuciInfoMessage(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                zuciEntity = JSON.parseObject(paramAnonymousString, ZuciEntity.class);
                footprintsList = JSON.parseArray(ZuciInfoActivity.this.zuciEntity.getFootprints(), ZuciFootprintsEntity.class);
                mFootprintAdapter.setListData(ZuciInfoActivity.this.footprintsList);
                listClan=JSON.parseArray(zuciEntity.getAssociations(),MoreClanEntity.class);
                if (listClan.size()>0){
                    clan_banding_layout.setVisibility(View.VISIBLE);
                    clan_recycler.setVisibility(View.VISIBLE);
                    bandingAdapter.setListData(listClan);
                }
                setData();
            }
        });
    }

    public void setData() {
        this.name.setText(this.zuciEntity.getName());
        this.distance.setText(this.zuciEntity.getDistance() + "km");
        this.loatoin.setText(this.zuciEntity.getProvince() + this.zuciEntity.getCity() + this.zuciEntity.getDetailed_place());
        this.zuci_info_intro.setText(this.zuciEntity.getIntroduce());
        like.setSelected(zuciEntity.getIs_praise().equals("0")?false:true);
        zuci_info_collection.setSelected(zuciEntity.getIs_collection().equals("0")?false:true);
        zuci_info_collection.setText(zuciEntity.getIs_collection().equals("0")?getString(R.string.collect):getString(R.string.collect2));
        this.like.setText(" " + this.zuciEntity.getThumb_cnt() + getString(R.string.praise2));
        Glide.with(this).load(this.zuciEntity.getFirst_figure()).into(this.zuci_info_photo);
    }
}
