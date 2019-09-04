package com.yuejian.meet.activities.zuci;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.bean.ZuciEntity;
import com.yuejian.meet.utils.StringUtils;

import java.io.File;
import java.net.URISyntaxException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 祖祠地图导航页面
 */
public class ZuciMapNavigationActivity extends BaseActivity {
    @Bind(R.id.zuci_map_bottom)
    LinearLayout mLayout;
    @Bind(R.id.map_distance)
    TextView map_distance;
    @Bind(R.id.map_site)
    TextView map_site;
    MapView mapView;
    private View mPoupView = null;
    protected LayoutInflater mInflater;
    private PopupWindow mPoupWindow = null;
    private AMap aMap;
    Intent intent;
    private String site;
    private String latitude;
    private String longitude;
    private String zuciName;
    private String detailed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zuci_map_navigation);
        mapView= (MapView) findViewById(R.id.zuzi_link_map);
        this.mapView.onCreate(savedInstanceState);
        if (this.aMap == null) {
            this.aMap = this.mapView.getMap();
        }
        this.aMap.getUiSettings().setZoomControlsEnabled(false);
        intent=getIntent();
        site=intent.getStringExtra("site");
        latitude=intent.getStringExtra("latitude");
        longitude=intent.getStringExtra("longitude");
        detailed=intent.getStringExtra("detailed");
        zuciName=intent.getStringExtra("zuciName");
        setTitleText(zuciName);
        initView();
    }
    public void initView(){
//        new AMap().Circle
        LatLng start = new LatLng(Double.parseDouble(AppConfig.slatitude), Double.parseDouble(AppConfig.slongitude));
        LatLng end = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
        float km= AMapUtils.calculateLineDistance(start, end);
        map_site.setText(site);
        map_distance.setText(getString(R.string.distance)+ StringUtils.formatDouble((km/1000)+"")+"km");


        LatLng localLatLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(localLatLng));
        getMyView();
    }
    protected void getMyView() {
        final View localView = getLayoutInflater().inflate(R.layout.zuci_custom_navigation_loaction_map_layout, null);

        LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        MarkerOptions markerOptions = new MarkerOptions();
        new BitmapDescriptorFactory();
        markerOptions.icon(BitmapDescriptorFactory.fromView(localView)).position(latLng).title("").draggable(true);
        aMap.addMarker(markerOptions);
    }

    @OnClick({R.id.mpa_submit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.mpa_submit:
                showBottomPopupWindow();
                break;
            case R.id.txt_dialog_album://百度
                try {
                    intent = Intent.getIntent("intent://map/direction?origin=latlng:"+AppConfig.slatitude+","+AppConfig.slongitude+"|name:&destination=latlng:"+latitude+","+longitude+"|name:"+detailed+"&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                    startActivity(intent);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.txt_dialog_save_pic://高德
                try {
                    intent = Intent.getIntent("androidamap://route?sourceApplication=softname&sname=我的位置&dlat="+latitude+"&dlon="+longitude+"&dname="+detailed+"&dev=0&m=0&t=1");
                    startActivity(intent);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.txt_dialog_cancel:
                mPoupWindow.dismiss();
                break;
        }
    }

    /**
     * 底部PopupWindow
     */
    private void showBottomPopupWindow() {
        if (mPoupView == null) {
            mInflater = LayoutInflater.from(this);
            mPoupView = mInflater.inflate(R.layout.dialog_edit_head_photo, null);
            bindPopMenuEvent(mPoupView);
            mPoupWindow = new PopupWindow(mPoupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPoupWindow.setAnimationStyle(R.style.PopupAnimation);
            mPoupWindow.setTouchable(true);
            mPoupWindow.setFocusable(true);
            mPoupWindow.setOutsideTouchable(true);
            mPoupWindow.setTouchInterceptor(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            mPoupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
            ColorDrawable dw = new ColorDrawable(0x90000000);
            mPoupWindow.setBackgroundDrawable(dw);
        }
        mPoupWindow.showAtLocation(mLayout, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f);
    }
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    /**
     * 实例化底部pop菜单项
     *
     * @param view
     */
    private void bindPopMenuEvent(View view)  {
        TextView txtToPhoto = (TextView) view.findViewById(R.id.txt_dialog_photo);
        TextView txtToAlbum = (TextView) view.findViewById(R.id.txt_dialog_album);
        TextView txtSavePic = (TextView) view.findViewById(R.id.txt_dialog_save_pic);
        TextView txtCancel = (TextView) view.findViewById(R.id.txt_dialog_cancel);
        txtToPhoto.setText(R.string.Select_a_map);
        txtToAlbum.setVisibility(View.GONE);
        txtSavePic.setVisibility(View.GONE);
        try {
            intent = Intent.getIntent("intent://map/direction?origin=latlng:"+AppConfig.slatitude+","+AppConfig.slongitude+"&destination=latlng:"+latitude+","+longitude+"|name:东郡华城广场|A座&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            if(isInstallByread("com.baidu.BaiduMap")){
                txtToAlbum.setText(R.string.Baidu_Map);
                txtToAlbum.setOnClickListener(this);
                txtToAlbum.setVisibility(View.VISIBLE);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            intent = Intent.getIntent("androidamap://route?sourceApplication=softname&sname=我的位置&dlat="+latitude+"&dlon="+longitude+"&dname="+"东郡华城广场|A座"+"&dev=0&m=0&t=1");
            if(isInstallByread("com.autonavi.minimap")){
                txtSavePic.setText(R.string.Amap);
                txtSavePic.setOnClickListener(this);
                txtSavePic.setVisibility(View.VISIBLE);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        txtToAlbum.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
    }
    /**
     * 判断是否安装目标应用
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
    protected void onDestroy() {
        super.onDestroy();
        this.mapView.onDestroy();
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
}
