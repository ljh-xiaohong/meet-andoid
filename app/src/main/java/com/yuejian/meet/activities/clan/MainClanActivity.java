package com.yuejian.meet.activities.clan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.search.MoreSearchActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.MoreClanEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author :
 * @time : 2018/11/11 11:23
 * @desc : 宗亲会
 * @version: V1.0
 * @update : 2018/11/11 11:23
 */

public class MainClanActivity extends BaseActivity implements AMap.OnMapLoadedListener {
    private AMap aMap;
    List<MoreClanEntity> listData = new ArrayList();
    private MapView mapView;
    @Bind(R.id.titlebar_imgBtn_search)
    ImageButton titlebar_imgBtn_search;
    Intent intent;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main_clan);
        this.mapView = ((MapView) findViewById(R.id.map));
        this.mapView.onCreate(paramBundle);
        this.titlebar_imgBtn_search.setVisibility(View.VISIBLE);
        this.titlebar_imgBtn_search.setImageDrawable(getResources().getDrawable(R.mipmap.icon_nav_seach));
        init();
    }


    public void drawMarkers() {
        this.aMap.setOnMapLoadedListener(this);
        int i = 0;
        while (i < this.listData.size()) {
            getMyView((MoreClanEntity) this.listData.get(i), i);
            i += 1;
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                LatLng localLatLng = new LatLng(Double.parseDouble(AppConfig.slatitude), Double.parseDouble(AppConfig.slongitude));
                MainClanActivity.this.aMap.moveCamera(CameraUpdateFactory.changeLatLng(localLatLng));
            }
        }, 100L);
    }

    protected void getMyView(final MoreClanEntity paramMoreClanEntity, final int paramInt) {
        final View localView = getLayoutInflater().inflate(R.layout.custom_info_window_layout, null);
        TextView localTextView = (TextView) localView.findViewById(R.id.txt_info_window);
        final ImageView localImageView = (ImageView) localView.findViewById(R.id.map_photo);
        localTextView.setText(paramMoreClanEntity.getAssociation_name());
        Glide.with(getBaseContext()).load(paramMoreClanEntity.getAssociation_img()).asBitmap().listener(new RequestListener<String, Bitmap>() {

            public boolean onException(Exception paramAnonymousException, String paramAnonymousString, Target<Bitmap> paramAnonymousTarget, boolean paramAnonymousBoolean) {
                return false;
            }

            public boolean onResourceReady(Bitmap paramAnonymousBitmap, String paramAnonymousString, Target<Bitmap> paramAnonymousTarget, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2) {
                localImageView.setImageBitmap(paramAnonymousBitmap);
                LatLng localLatLng = new LatLng(Double.parseDouble(paramMoreClanEntity.getAssociation_lat()), Double.parseDouble(paramMoreClanEntity.getAssociation_lng()));
                MarkerOptions markerOptions = new MarkerOptions();
                new BitmapDescriptorFactory();
                markerOptions.icon(BitmapDescriptorFactory.fromView(localView)).position(localLatLng).title(paramInt + "").draggable(true);
                aMap.addMarker(markerOptions);
                return false;
            }
        }).into(localImageView);
    }

    public void init() {
        setTitleText(getString(R.string.Clansmen_association));
        if (this.aMap == null) {
            this.aMap = this.mapView.getMap();
        }
        this.aMap.getUiSettings().setZoomControlsEnabled(false);
        requstData();
        this.aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker paramAnonymousMarker) {
                int i = Integer.parseInt(paramAnonymousMarker.getTitle());
                intent = new Intent(MainClanActivity.this.getApplication(), ClanInfoActivity.class);
                intent.putExtra("clanEntity", (Serializable) MainClanActivity.this.listData.get(i));
                startActivity(intent);
                return true;
            }
        });
    }

    @OnClick({R.id.main_clan_moer, R.id.mi_clan_layout, R.id.titlebar_imgBtn_search})
    public void onClick(View paramView) {
        if (StringUtil.isEmpty(AppConfig.CustomerId)) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        switch (paramView.getId()) {
            default:
                return;
            case R.id.main_clan_moer:
                startActivity(new Intent(this, MoreClanActivity.class));
                return;
            case R.id.mi_clan_layout:
                startActivity(new Intent(this, MiClanAssociationActivity.class));
                return;
            case R.id.titlebar_imgBtn_search:
                intent = new Intent(this, MoreSearchActivity.class);
                intent.putExtra("type", 3);
                intent.putExtra("content", "");
                startActivity(intent);
                return;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mapView.onDestroy();
    }

    public void onMapLoaded() {
        int i = 0;
        if (i < 1) {
            Object localObject = new LatLng(Double.parseDouble(((MoreClanEntity) this.listData.get(i)).getAssociation_lat()), Double.parseDouble(((MoreClanEntity) this.listData.get(i)).getAssociation_lng()));
            if (i == 0) {
            }
            for (localObject = new LatLngBounds.Builder().include((LatLng) localObject).build(); ; localObject = new LatLngBounds.Builder().include((LatLng) localObject).build()) {
                this.aMap.moveCamera(CameraUpdateFactory.newLatLngBounds((LatLngBounds) localObject, 10));
                i += 1;
                break;
            }
        }
        this.aMap.moveCamera(CameraUpdateFactory.zoomTo(10.0F));
    }

    protected void onPause() {
        super.onPause();
        this.mapView.onPause();
    }

    protected void onResume() {
        super.onResume();
        this.mapView.onResume();
    }

    protected void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        this.mapView.onSaveInstanceState(paramBundle);
    }


    public void requstData() {
//        if (AppConfig.userEntity == null) {
//            return;
//        }
        HashMap localHashMap = new HashMap();
//        localHashMap.put("surname", AppConfig.userEntity.getSurname());
        this.apiImp.getMainClan(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String data, int paramAnonymousInt) {
                MainClanActivity.this.listData = JSON.parseArray(data, MoreClanEntity.class);
                if (MainClanActivity.this.listData.size() > 0) {
                    MainClanActivity.this.drawMarkers();
                }
            }
        });
    }

}
