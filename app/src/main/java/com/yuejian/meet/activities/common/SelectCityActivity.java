package com.yuejian.meet.activities.common;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.CitySelectAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.RegionEntity;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static android.provider.Settings.Global.putInt;

/**
 * 选择城市 */
public class SelectCityActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    @Bind(R.id.txt_titlebar_title)
    TextView title;
    @Bind(R.id.titlebar_imgBtn_back)
    ImageButton back;
    @Bind(R.id.city_list)
    ListView city_list;
    CitySelectAdapter mAdapter;
    List<RegionEntity> listData=new ArrayList<>();
    ApiImp api=new ApiImp();
    String regionName="",rank="0";
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (AppConfig.isGeLiGuide){
//            if (AppConfig.isGeLiGuide){
//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            }
//        }
        setContentView(R.layout.activity_select_city);
        Utils.settingPutInt(this);
        intent=getIntent();
        if (intent.hasExtra("regionName")){
            regionName=intent.getStringExtra("regionName");
        }
        if (intent.hasExtra("rank"))
            rank=intent.getStringExtra("rank");
        initView();
    }

    public void initView(){
        city_list.setOnItemClickListener(this);
        setTitleText("所属家族");
        back.setVisibility(View.VISIBLE);
        mAdapter=new CitySelectAdapter(city_list,listData,R.layout.item_city_select_layout);
        city_list.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        if (rank.contains("2")){
            getRequstArea();
        }else if (rank.contains("1")){
            getRequstCity();
        }else {
            getRequstProvince();
        }
    }

    /**
     * 省
     */
    public void getRequstProvince(){
        api.getProvince(this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData= JSON.parseArray(data,RegionEntity.class);
                mAdapter.refresh(listData);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    /**
     * 市
     */
    public void getRequstCity(){
        api.getCity(regionName,this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData= JSON.parseArray(data,RegionEntity.class);
                mAdapter.refresh(listData);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    /**
     * 区
     */
    public void getRequstArea(){
        api.getArea(regionName,this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData= JSON.parseArray(data,RegionEntity.class);
                mAdapter.refresh(listData);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    @OnClick({R.id.titlebar_imgBtn_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titlebar_imgBtn_back:
                finish();
                break;
        }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (resultCode==RESULT_OK){
            intent=new Intent();
            intent.putExtra("regionName",data.getExtras().getString("regionName"));
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    @Override
    public void onSomeEvent(BusCallEntity event) {
        super.onSomeEvent(event);
        if (event.getCallType()==BusEnum.FINISH_SELECTCITY){
            finish();
        }else if (event.getCallType()==BusEnum.NETWOR_CONNECTION){
            if (listData.size()==0){
                if (rank.contains("2")){
                    getRequstArea();
                }else if (rank.contains("1")){
                    getRequstCity();
                }else {
                    getRequstProvince();
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        intent=new Intent(getApplication(),SelectCityActivity.class);
        if (listData.get(position).getProvince()!=null){//省
            if (!listData.get(position).getProvincn_city_area_id().equals("0")){
                selectRegion(BusEnum.FINISH_SELECTCITY,listData.get(position).getProvince(),listData.get(position).getArea_name(),listData.get(position).getProvincn_city_area_id());
                finish();
                return;
            }
            selectRegion(BusEnum.PROVINCE,listData.get(position).getProvince(),listData.get(position).getArea_name(),listData.get(position).getProvincn_city_area_id());
            intent.putExtra("regionName",listData.get(position).getProvince());
            intent.putExtra("rank","1");
            startActivityIfNeeded(intent,3);
        }else if (listData.get(position).getCity()!=null){///市
            if (!listData.get(position).getProvincn_city_area_id().equals("0")){
                selectRegion(BusEnum.FINISH_SELECTCITY,listData.get(position).getCity(),listData.get(position).getArea_name(),listData.get(position).getProvincn_city_area_id());
                finish();
                return;
            }
            selectRegion(BusEnum.CITY,listData.get(position).getCity(),listData.get(position).getArea_name(),listData.get(position).getProvincn_city_area_id());
            intent.putExtra("regionName",listData.get(position).getCity());
            intent.putExtra("rank","2");
            startActivityIfNeeded(intent,3);
        }else if (listData.get(position).getArea()!=null){//区
            if (listData.get(position).getProvincn_city_area_id()!=null){
                selectRegion(BusEnum.FINISH_SELECTCITY,listData.get(position).getArea(),listData.get(position).getArea_name(),listData.get(position).getProvincn_city_area_id());
            }
            finish();
        }
    }
    public void selectRegion(BusEnum eBusEnum,String data,String areaName,String id){
        BusCallEntity entity=new BusCallEntity();
        entity.setCallType(eBusEnum);
        entity.setData(data);
        entity.setAreaName(areaName);
        entity.setId(id);
        Bus.getDefault().post(entity);
    }
}
