package com.yuejian.meet.activities.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author :
 * @time   : 2018/11/19 17:08
 * @desc   : 选择 城市
 * @version: V1.0
 * @update : 2018/11/19 17:08
 */

public class SelectMemberCityActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.txt_titlebar_title)
    TextView title;
    @Bind(R.id.titlebar_imgBtn_back)
    ImageButton back;
    @Bind(R.id.city_list)
    ListView city_list;
    @Bind(R.id.txt_location_city)
    TextView txt_location_city;
    @Bind(R.id.txt_select_all_city)
    TextView select_all_city;
    @Bind(R.id.sel_type_name)
    TextView sel_type_name;
    @Bind(R.id.region_chck)
    CheckBox region_chck;
    CitySelectAdapter mAdapter;
    List<RegionEntity> listData = new ArrayList<>();
    ApiImp api = new ApiImp();
    String regionName = "", rank = "0";
    Boolean isSovereignty=true;
    Intent intent;
    String city;
    private Boolean clanSel=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_member_city);
        intent = getIntent();
        if (intent.hasExtra("clanSel")){
            clanSel=intent.getBooleanExtra("clanSel",false);
        }
        if (intent.hasExtra("regionName")) {
            regionName = intent.getStringExtra("regionName");
        }
        if (intent.hasExtra("rank"))
            rank = intent.getStringExtra("rank");
        if (intent.hasExtra("isSovereignty"))
            isSovereignty=intent.getBooleanExtra("isSovereignty",true);
        city = intent.getStringExtra("city");
        if (StringUtils.isNotEmpty(city)) {
            txt_location_city.setText(city);
        } else {
            select_all_city.setVisibility(View.INVISIBLE);
            txt_location_city.setText(getString(R.string.nationwide));
            if (StringUtils.isNotEmpty(AppConfig.city)) {
                txt_location_city.setText(AppConfig.city);
            } else {
                Bus.getDefault().post(new BusCallEntity(null, null, null, "start_location"));
            }
        }
//        txt_location_city.setSelected(true);
        initView();
    }

    public void initView() {
        city_list.setOnItemClickListener(this);
        if (!isSovereignty){
            region_chck.setVisibility(View.VISIBLE);
        }else {
            setTitleText(getString(R.string.select_city));
        }
        back.setVisibility(View.VISIBLE);
        mAdapter = new CitySelectAdapter(city_list, listData, R.layout.item_city_select_layout);
        city_list.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        if (rank.contains("2")) {
            getRequstArea();
        } else if (rank.contains("1")) {
            getRequstCity();
        } else {
            getRequstProvince();
        }
    }

    /**
     * 省
     */
    public void getRequstProvince() {
        api.getProvince(this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData = JSON.parseArray(data, RegionEntity.class);
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
    public void getRequstCity() {
        api.getCity(regionName, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData = JSON.parseArray(data, RegionEntity.class);
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
    public void getRequstArea() {
        api.getArea(regionName, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData = JSON.parseArray(data, RegionEntity.class);
                mAdapter.refresh(listData);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @OnClick({R.id.titlebar_imgBtn_back, R.id.txt_select_all_city, R.id.txt_location_city,R.id.region_chck})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_imgBtn_back:
                finish();
                break;
            case R.id.txt_select_all_city://选择全部
                selectRegion(BusEnum.district, getString(R.string.home_all_name), "111222","1",getString(R.string.nationwide),region_chck.isChecked());
                finish();
                break;
            case R.id.txt_location_city://选择定位的城市
                if (txt_location_city.getText().toString().equals(getString(R.string.nationwide))){
                    selectRegion(BusEnum.district, getString(R.string.home_all_name), "111222","1",getString(R.string.nationwide),region_chck.isChecked());
                }else {
                    selectRegion(region_chck.isChecked()?BusEnum.PROVINCE:BusEnum.district, txt_location_city.getText().toString(), "111222","3",getString(R.string.location),region_chck.isChecked());
                }
                finish();
                break;
            case R.id.region_chck:
                    sel_type_name.setText(region_chck.isChecked()?getString(R.string.Select_province):getString(R.string.Select_province2));
                    if (!txt_location_city.getText().equals(getString(R.string.nationwide))){
                        txt_location_city.setText(region_chck.isChecked()?AppConfig.province:city);
                    }
                break;
        }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (resultCode == RESULT_OK) {
            intent = new Intent();
            intent.putExtra("regionName", data.getExtras().getString("regionName"));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onSomeEvent(BusCallEntity event) {
        super.onSomeEvent(event);
        if (event.getCallType() == BusEnum.district) {
            finish();
        } else if (event.getCallType() == BusEnum.LOCATION_UPDATE) {
            txt_location_city.setText(AppConfig.city);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        intent = new Intent(getApplication(), SelectMemberCityActivity.class);
        intent.putExtra("isSovereignty",true);
        if (listData.get(position).getProvince() != null) {//省
            if (region_chck.isChecked() || clanSel){//设置选择省份
                selectRegion(BusEnum.PROVINCE, listData.get(position).getProvince(), listData.get(position).getProvincn_city_area_id(),"1",listData.get(position).getArea_name(),region_chck.isChecked());
                finish();
                return;
            }
            if (!listData.get(position).getProvincn_city_area_id().equals("0")) {
                selectRegion(BusEnum.district, listData.get(position).getProvince(), listData.get(position).getProvincn_city_area_id(),"1",listData.get(position).getArea_name(),region_chck.isChecked());
                finish();
                return;
            }
            selectRegion(BusEnum.PROVINCE, listData.get(position).getProvince(), listData.get(position).getProvincn_city_area_id(),"1",listData.get(position).getArea_name(),region_chck.isChecked());
            intent.putExtra("regionName", listData.get(position).getProvince());
            intent.putExtra("rank", "1");
            intent.putExtra("city", city);
            startActivityIfNeeded(intent, 3);
        } else if (listData.get(position).getCity() != null) {///市
            if (!listData.get(position).getProvincn_city_area_id().equals("0") || clanSel) {
                selectRegion(BusEnum.district, listData.get(position).getCity(), listData.get(position).getProvincn_city_area_id(),"2",listData.get(position).getArea_name(),region_chck.isChecked());
                finish();
                return;
            }
            selectRegion(BusEnum.CITY, listData.get(position).getCity(), listData.get(position).getProvincn_city_area_id(),"2",listData.get(position).getArea_name(),region_chck.isChecked());
            intent.putExtra("regionName", listData.get(position).getCity());
            intent.putExtra("rank", "2");
            intent.putExtra("city", city);
            startActivityIfNeeded(intent, 3);
        } else if (listData.get(position).getArea() != null) {//区
            if (listData.get(position).getProvincn_city_area_id() != null) {
                selectRegion(BusEnum.district, listData.get(position).getArea(), listData.get(position).getProvincn_city_area_id(),"3",listData.get(position).getArea_name(),region_chck.isChecked());
            }
            finish();
        }
    }

    public void selectRegion(BusEnum eBusEnum, String data, String id,String type,String ateaName,Boolean isProvince) {
        BusCallEntity entity = new BusCallEntity();
        entity.setCallType(eBusEnum);
        entity.setData(data);
        entity.setStateType(type);
        entity.setId(id);
        entity.setAreaName(ateaName);
        entity.setSelProvince(isProvince);
        Bus.getDefault().post(entity);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        super.finish();
    }
}