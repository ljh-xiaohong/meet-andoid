package com.yuejian.meet.activities.clan;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

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
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class SearchLocationActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener, SpringView.OnFreshListener, OnItemClickListener, OnEditorActionListener {
    @Bind(R.id.et_location_search)
    EditText et_location_search;
    @Bind(R.id.location_list)
    ListView location_list;
    @Bind(R.id.location_spring)
    SpringView location_spring;
    NearbyLocationAdapter mAdapter;
    private int pageIndex = 0;
    private List<PoiItem> poiItems = new ArrayList();
    private PoiResult poiResult;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    private String search = "";

    protected void doSearchQuery(){
        this.query = new PoiSearch.Query(this.search, "", "");
        this.query.setPageSize(30);
        this.query.setPageNum(this.pageIndex);
        this.poiSearch = new PoiSearch(this, this.query);
        this.poiSearch.setOnPoiSearchListener(this);
        this.poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(Double.parseDouble(AppConfig.slongitude), Double.parseDouble(AppConfig.slatitude)), 10000, true));
        this.poiSearch.searchPOIAsyn();
    }

    public void initData(){
        this.et_location_search.setOnEditorActionListener(this);
        this.location_list.setOnItemClickListener(this);
        this.location_spring.setHeader(new DefaultHeader(this));
        this.location_spring.setFooter(new DefaultFooter(this));
        this.location_spring.setListener(this);
        this.mAdapter = new NearbyLocationAdapter(this.location_list, this.poiItems, R.layout.item_nearby_location_layout);
        this.location_list.setAdapter(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
        doSearchQuery();
    }

    @OnClick({R.id.location_search})
    public void onClick(View paramView){
        switch (paramView.getId()){
            case R.id.location_search:
                postSearch();
                return;
        }
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_search_location);
        initData();
    }

    protected void onDestroy()
    {
        super.onDestroy();
    }

    public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
    {
        if ((paramInt == 4) || ((paramKeyEvent != null) && (paramKeyEvent.getKeyCode() == 66)))
        {
            postSearch();
            return true;
        }
        return false;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
        BusCallEntity busCallEntity= new BusCallEntity();
        busCallEntity.setCallType(BusEnum.CLAN_LOCATION);
        busCallEntity.setObject(this.poiItems.get(paramInt));
        Bus.getDefault().post(busCallEntity);
        finish();
    }

    public void onLoadmore()
    {
        doSearchQuery();
    }

    public void onPoiItemSearched(PoiItem paramPoiItem, int paramInt) {}

    public void onPoiSearched(PoiResult paramPoiResult, int paramInt)
    {
        if ((paramInt == 1000) && (paramPoiResult != null) && (paramPoiResult.getQuery() != null))
        {
            this.poiResult = paramPoiResult;
            if (this.pageIndex == 0) {
                this.poiItems.clear();
            }
            this.poiItems.addAll(this.poiResult.getPois());
            this.mAdapter.refresh(this.poiItems);
            this.pageIndex += 1;
        }
        if (this.location_spring != null) {
            this.location_spring.onFinishFreshAndLoad();
        }
    }

    public void onRefresh(){
        this.pageIndex = 0;
        doSearchQuery();
    }

    public void postSearch(){
        this.search = this.et_location_search.getText().toString();
        Utils.hintKbTwo(this);
        this.pageIndex = 0;
        doSearchQuery();
    }
}
