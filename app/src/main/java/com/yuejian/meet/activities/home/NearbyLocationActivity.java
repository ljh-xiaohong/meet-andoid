package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.netease.nim.uikit.app.AppConfig;
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

/**
 *附近位置
 */
public class NearbyLocationActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener,SpringView.OnFreshListener,AdapterView.OnItemClickListener,TextView.OnEditorActionListener{

    @Bind(R.id.location_spring)
    SpringView location_spring;
    @Bind(R.id.location_list)
    ListView location_list;
    @Bind(R.id.et_location_search)
    EditText et_location_search;
    private PoiResult poiResult; // poi返回的结果
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;
    private List<PoiItem> poiItems=new ArrayList<>();// poi数据
    private int pageIndex=0;
    private String search="";
    NearbyLocationAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_location);
        initData();
    }
    public void initData(){
        setTitleText("位置");
        et_location_search.setOnEditorActionListener(this);
        location_list.setOnItemClickListener(this);
        location_spring.setHeader(new DefaultHeader(this));
        location_spring.setFooter(new DefaultFooter(this));
        location_spring.setListener(this);
        mAdapter=new NearbyLocationAdapter(location_list,poiItems,R.layout.item_nearby_location_layout);
        location_list.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        doSearchQuery();
    }

    @OnClick({R.id.location_search})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.location_search:
                postSearch();
                break;
        }
    }
/**
 * 开始进行poi搜索
 */
    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        query = new PoiSearch.Query(search, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(30);// 设置每页最多返回多少条poiitem
        query.setPageNum(pageIndex);// 设置查第一页

            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(Double.parseDouble(AppConfig.slongitude),Double.parseDouble( AppConfig.slatitude)), 10000, true));//
            // 设置搜索区域为以lp点为圆心，其周围10000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
    }
    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                    poiResult = result;
                if (pageIndex==0){
                    poiItems.clear();
                }
                poiItems.addAll(poiResult.getPois());
                mAdapter.refresh(poiItems);
//                    List<SuggestionCity> suggestionCities = poiResult
//                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                pageIndex+=1;
            } else {
            }
        } else  {
//            ToastUtil.showerror(this.getApplicationContext(), rcode);
        }
        if (location_spring!=null){
            location_spring.onFinishFreshAndLoad();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onRefresh() {
        pageIndex=0;
        doSearchQuery();
    }

    @Override
    public void onLoadmore() {
        doSearchQuery();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent();
        intent.putExtra("AdName",poiItems.get(position).getAdName());
        intent.putExtra("ProvinceName",poiItems.get(position).getProvinceName());
        intent.putExtra("CityName",poiItems.get(position).getCityName());
        intent.putExtra("Snippet",poiItems.get(position).getSnippet());
        intent.putExtra("LatLonPoint",poiItems.get(position).getLatLonPoint().toString());
        intent.putExtra("locationTitle",poiItems.get(position).getTitle());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            postSearch();
            return true;
        }
        return false;
    }
    public void postSearch(){
        search=et_location_search.getText().toString();
        Utils.hintKbTwo(this);
        pageIndex=0;
        doSearchQuery();
    }
}
