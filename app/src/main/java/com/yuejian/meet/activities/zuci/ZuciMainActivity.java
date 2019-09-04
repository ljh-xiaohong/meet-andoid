package com.yuejian.meet.activities.zuci;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.LatLngBounds.Builder;
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
import com.yuejian.meet.bean.ZuciEntity;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 祖祠汇
 */
public class ZuciMainActivity extends BaseActivity implements AMap.OnMapLoadedListener, AMap.OnMapClickListener {
    private AMap aMap;
    Intent intent;
    List<ZuciEntity> listData;
    private MapView mapView;
    @Bind(R.id.titlebar_imgBtn_search)
    ImageButton titlebar_imgBtn_search;
    ZuciEntity zuciEntity;
    @Bind(R.id.zuci_main_collection)
    TextView zuci_main_collection;
    @Bind(R.id.zuci_main_distance)
    TextView zuci_main_distance;
    @Bind(R.id.zuci_main_distance_layout)
    LinearLayout zuci_main_distance_layout;
    @Bind(R.id.zuci_main_info_message)
    LinearLayout zuci_main_info_message;
    @Bind(R.id.zuci_main_like)
    TextView zuci_main_like;
    @Bind(R.id.zuci_main_loatoin)
    TextView zuci_main_loatoin;
    @Bind(R.id.zuci_main_name)
    TextView zuci_main_name;
    @Bind(R.id.zuci_main_photo)
    ImageView zuci_main_photo;


    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_zuci_main);
        this.mapView = ((MapView) findViewById(R.id.zuzi_map));
        this.mapView.onCreate(paramBundle);
        initData();
    }

    public void drawMarkers() {
        this.aMap.setOnMapLoadedListener(this);
        int i = 0;
        while (i < this.listData.size()) {
            getMyView((ZuciEntity) this.listData.get(i), i);
            i += 1;
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                LatLng localLatLng = new LatLng(Double.parseDouble(AppConfig.slatitude), Double.parseDouble(AppConfig.slongitude));
                ZuciMainActivity.this.aMap.moveCamera(CameraUpdateFactory.changeLatLng(localLatLng));
            }
        }, 100L);
    }

    protected void getMyView(final ZuciEntity paramZuciEntity, final int paramInt) {
        final View localView = getLayoutInflater().inflate(R.layout.zuci_custom_loaction_window_layout, null);
        TextView localTextView = (TextView) localView.findViewById(R.id.txt_info_window);
        final ImageView localImageView = (ImageView) localView.findViewById(R.id.map_photo);
        localTextView.setText(paramZuciEntity.getName());
        Glide.with(getBaseContext()).load(paramZuciEntity.getFirst_figure()).asBitmap().listener(new RequestListener<String, Bitmap>() {
            public boolean onException(Exception paramAnonymousException, String paramAnonymousString, Target<Bitmap> paramAnonymousTarget, boolean paramAnonymousBoolean) {
                return false;
            }

            public boolean onResourceReady(Bitmap paramAnonymousBitmap, String paramAnonymousString, Target<Bitmap> paramAnonymousTarget, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2) {
                localImageView.setImageBitmap(paramAnonymousBitmap);
                LatLng latLng = new LatLng(Double.parseDouble(paramZuciEntity.getLatitude()), Double.parseDouble(paramZuciEntity.getLongitude()));
                MarkerOptions markerOptions = new MarkerOptions();
                new BitmapDescriptorFactory();
                markerOptions.icon(BitmapDescriptorFactory.fromView(localView)).position(latLng).title(paramInt + "").draggable(true);
                ZuciMainActivity.this.aMap.addMarker(markerOptions);
                return false;
            }
        }).into(localImageView);
    }

    public void initData() {
        this.titlebar_imgBtn_search.setVisibility(View.VISIBLE);
        this.titlebar_imgBtn_search.setImageDrawable(getResources().getDrawable(R.mipmap.icon_nav_seach));
        setTitleText(getString(R.string.Ancestral_shrine_remit));
        if (this.aMap == null) {
            this.aMap = this.mapView.getMap();
        }
        this.aMap.getUiSettings().setZoomControlsEnabled(false);
        this.aMap.setOnMapClickListener(this);
        this.aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker paramAnonymousMarker) {
                int i = Integer.parseInt(paramAnonymousMarker.getTitle());
                ZuciMainActivity.this.requstInfoMessage(((ZuciEntity) ZuciMainActivity.this.listData.get(i)).getId());
                return true;
            }
        });
        requstData();
    }

    @OnClick({R.id.titlebar_imgBtn_search, R.id.zuci_main_like, R.id.zuci_main_but, R.id.zuci_main_collection, R.id.zuci_main_collection_layout, R.id.zuci_add, R.id.zuci_main_locality, R.id.zuci_main_nearby})
    public void onClick(View paramView) {
        if ((StringUtil.isEmpty(AppConfig.CustomerId)) && (paramView.getId() != R.id.zuci_main_locality)) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        switch (paramView.getId()) {
            default:
                return;
            case R.id.zuci_main_collection_layout:
                startActivity(new Intent(this, ZuciCollectionActivity.class));
                return;
            case R.id.titlebar_imgBtn_search:
                this.intent = new Intent(this, MoreSearchActivity.class);
                this.intent.putExtra("type", 2);
                this.intent.putExtra("content", "");
                startActivity(this.intent);
                return;
            case R.id.zuci_main_like:
                postLike(this.zuciEntity);
                return;
            case R.id.zuci_main_but:
                this.intent = new Intent(this, ZuciInfoActivity.class);
                this.intent.putExtra("id", this.zuciEntity.getId());
                this.intent.putExtra("zuciName", this.zuciEntity.getName());
                startActivity(this.intent);
                return;
            case R.id.zuci_main_collection:
                postCollection();
                return;
            case R.id.zuci_add:
                startActivity(new Intent(this, CreateZuciActivity.class));
                return;
            case R.id.zuci_main_locality:
                LatLng latLng = new LatLng(Double.parseDouble(AppConfig.slatitude), Double.parseDouble(AppConfig.slongitude));
                this.aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                return;
            case R.id.zuci_main_nearby:
                startActivity(new Intent(this, ZuciNearbyActivity.class));
                break;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mapView.onDestroy();
    }

    public void onMapClick(LatLng paramLatLng) {
        this.zuci_main_info_message.setVisibility(View.GONE);
    }

    public void onMapLoaded() {
        int i = 0;
        if (i < 1) {
            Object localObject = new LatLng(Double.parseDouble(((ZuciEntity) this.listData.get(i)).getLatitude()), Double.parseDouble(((ZuciEntity) this.listData.get(i)).getLongitude()));
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

    public void postCollection() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("type", "1");
        localHashMap.put("relation_id", this.zuciEntity.getId());
        localHashMap.put("customer_id", this.user.getCustomer_id());
        this.apiImp.collection(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                ZuciMainActivity.this.zuciEntity.setIs_collection(paramAnonymousString);
                zuci_main_collection.setSelected(zuciEntity.getIs_collection().equals("0") ? false : true);
                zuci_main_collection.setText(zuciEntity.getIs_collection().equals("0") ? getString(R.string.collect) : getString(R.string.collect2));
            }
        });
    }

    public void postLike(final ZuciEntity paramZuciEntity) {
        HashMap localHashMap = new HashMap();
        localHashMap.put("id", paramZuciEntity.getId());
        localHashMap.put("customer_id", this.user.getCustomer_id());
        this.apiImp.zuciLike(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String data, int paramAnonymousInt) {
                ZuciEntity localZuciEntity = paramZuciEntity;
                zuci_main_like.setSelected(data.equals("0") ? false : true);
                if (data.equals("0")) {
                    data = Integer.parseInt(paramZuciEntity.getThumb_cnt()) - 1 + "";
                    localZuciEntity.setThumb_cnt(data);
                } else {
                    data = Integer.parseInt(paramZuciEntity.getThumb_cnt()) + 1 + "";
                    localZuciEntity.setThumb_cnt(data);
                }
                ZuciMainActivity.this.zuci_main_like.setText(" " + paramZuciEntity.getThumb_cnt() + getString(R.string.praise));
            }
        });
    }

    public void requstData() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("longitude", AppConfig.slongitude);
        localHashMap.put("latitude", AppConfig.slatitude);
        this.apiImp.zuciNearby(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                ZuciMainActivity.this.listData = JSON.parseArray(paramAnonymousString, ZuciEntity.class);
                ZuciMainActivity.this.drawMarkers();
            }
        });
    }

    public void requstInfoMessage(String paramString) {
        HashMap localHashMap = new HashMap();
        localHashMap.put("id", paramString);
        localHashMap.put("longitude", AppConfig.slongitude);
        localHashMap.put("latitude", AppConfig.slatitude);
        this.apiImp.zuciInfoMessage(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                ZuciMainActivity.this.zuciEntity = ((ZuciEntity) JSON.parseObject(paramAnonymousString, ZuciEntity.class));
                ZuciMainActivity.this.zuci_main_info_message.setVisibility(View.VISIBLE);
                ZuciMainActivity.this.zuci_main_name.setText(ZuciMainActivity.this.zuciEntity.getName());
                ZuciMainActivity.this.zuci_main_loatoin.setText(ZuciMainActivity.this.zuciEntity.getDetailed_place());
                zuci_main_distance_layout.setVisibility(zuciEntity.getDistance().equals("-1") ? View.GONE : View.VISIBLE);
                ZuciMainActivity.this.zuci_main_distance.setText(ZuciMainActivity.this.zuciEntity.getDistance() + "km");
                ZuciMainActivity.this.zuci_main_like.setText(" " + ZuciMainActivity.this.zuciEntity.getThumb_cnt() + getString(R.string.praise));
                zuci_main_like.setSelected(zuciEntity.getIs_praise().equals("1") ? true : false);
                zuci_main_collection.setSelected(zuciEntity.getIs_collection().equals("1") ? true : false);
                zuci_main_collection.setText(zuciEntity.getIs_collection().equals("1") ? getString(R.string.collect2) : getString(R.string.collect));
                Glide.with(ZuciMainActivity.this.getApplication()).load(ZuciMainActivity.this.zuciEntity.getFirst_figure()).placeholder(R.mipmap.ic_default).into(ZuciMainActivity.this.zuci_main_photo);
            }
        });
    }
}
