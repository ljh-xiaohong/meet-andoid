package com.yuejian.meet.activities.clan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerDragListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.LatLngBounds.Builder;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.NearbyLocationAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *
 */
public class ClanLocationActivity extends BaseActivity implements OnItemClickListener, PoiSearch.OnPoiSearchListener, AMap.OnMapLoadedListener, AMap.OnMapClickListener {
    private AMap aMap;
    String latitude;
    @Bind(R.id.location_list)
    ListView location_list;
    String longitude;
    NearbyLocationAdapter mAdapter;
    MapView mapView;
    private List<PoiItem> poiItems = new ArrayList();
    private PoiResult poiResult;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    private String search = "";
    @Bind(R.id.titlebar_imgBtn_search)
    ImageButton searchButton;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_clan_location);
        this.mapView = ((MapView)findViewById(R.id.location_map));
        this.mapView.onCreate(paramBundle);
        setTitleText(getString(R.string.select_address));
        this.longitude = AppConfig.slongitude;
        this.latitude = AppConfig.slatitude;
        initData();
    }

    protected void doSearchQuery() {
        this.query = new PoiSearch.Query(this.search, "", "");
        this.query.setPageSize(30);
        this.query.setPageNum(0);
        this.poiSearch = new PoiSearch(this, this.query);
        this.poiSearch.setOnPoiSearchListener(this);
        this.poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(Double.parseDouble(this.longitude), Double.parseDouble(this.latitude)), 10000, true));
        this.poiSearch.searchPOIAsyn();
    }

    public void initData() {
        if (this.aMap == null) {
            this.aMap = this.mapView.getMap();
        }
        this.aMap.setOnMapLoadedListener(this);
        this.aMap.setOnMapClickListener(this);
        this.searchButton.setVisibility(View.VISIBLE);
        this.location_list.setOnItemClickListener(this);
        this.mAdapter = new NearbyLocationAdapter(this.location_list, this.poiItems, R.layout.item_nearby_location_layout);
        this.location_list.setAdapter(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
        LatLng localLatLng = new LatLng(Double.parseDouble(AppConfig.slatitude), Double.parseDouble(AppConfig.slongitude));
        MarkerOptions localMarkerOptions = new MarkerOptions();
        new BitmapDescriptorFactory();
        localMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_dingwei_hong)).position(localLatLng).title("").draggable(true);
        this.aMap.addMarker(localMarkerOptions);
        doSearchQuery();
        this.aMap.setOnMarkerDragListener(new AMap.OnMarkerDragListener()
        {
            public void onMarkerDrag(Marker paramAnonymousMarker) {}

            public void onMarkerDragEnd(Marker paramAnonymousMarker)
            {
                ClanLocationActivity.this.longitude = (paramAnonymousMarker.getPosition().longitude + "");
                ClanLocationActivity.this.latitude = (paramAnonymousMarker.getPosition().latitude + "");
                ClanLocationActivity.this.doSearchQuery();
            }

            public void onMarkerDragStart(Marker paramAnonymousMarker) {}
        });
    }

    @OnClick({R.id.titlebar_imgBtn_search})
    public void onClick(View paramView){
        switch (paramView.getId()){
            case R.id.titlebar_imgBtn_search:
                startActivityIfNeeded(new Intent(this, SearchLocationActivity.class), 100);
                finish();
                return;
        }
    }


    protected void onDestroy()
    {
        super.onDestroy();
        this.mapView.onDestroy();
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
        BusCallEntity busCallEntity = new BusCallEntity();
        busCallEntity.setCallType(BusEnum.CLAN_LOCATION);
        busCallEntity.setObject(this.poiItems.get(paramInt));
        Bus.getDefault().post(busCallEntity);
        finish();
    }

    public void onMapClick(LatLng paramLatLng) {}

    public void onMapLoaded()
    {
        Object localObject = new LatLng(Double.parseDouble(AppConfig.slatitude), Double.parseDouble(AppConfig.slongitude));
        localObject = new LatLngBounds.Builder().include((LatLng)localObject).build();
        this.aMap.moveCamera(CameraUpdateFactory.newLatLngBounds((LatLngBounds)localObject, 10));
        this.aMap.moveCamera(CameraUpdateFactory.zoomTo(12.0F));
    }

    protected void onPause()
    {
        super.onPause();
        this.mapView.onPause();
    }

    public void onPoiItemSearched(PoiItem paramPoiItem, int paramInt) {}

    public void onPoiSearched(PoiResult paramPoiResult, int paramInt)
    {
        if ((paramInt == 1000) && (paramPoiResult != null) && (paramPoiResult.getQuery() != null))
        {
            this.poiResult = paramPoiResult;
            this.poiItems.clear();
            this.poiItems.addAll(this.poiResult.getPois());
            this.mAdapter.refresh(this.poiItems);
        }
    }

    protected void onResume()
    {
        super.onResume();
        this.mapView.onResume();
    }

    protected void onSaveInstanceState(Bundle paramBundle)
    {
        super.onSaveInstanceState(paramBundle);
        this.mapView.onSaveInstanceState(paramBundle);
    }
}
