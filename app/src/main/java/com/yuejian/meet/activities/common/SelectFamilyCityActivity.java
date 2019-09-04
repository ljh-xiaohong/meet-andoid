package com.yuejian.meet.activities.common;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.api.utils.PreferencesMessage;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.CitySelectAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.RegionEntity;
import com.yuejian.meet.utils.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 选择所属家族
 */
public class SelectFamilyCityActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    @Bind(R.id.txt_titlebar_save)
    TextView txt_titlebar_save;
    @Bind(R.id.city_list)
    ListView city_list;
    @Bind(R.id.select1)
    TextView select1;
    @Bind(R.id.select2)
    TextView select2;
    @Bind(R.id.select3)
    TextView select3;
    CitySelectAdapter mAdapter;
    List<RegionEntity> listData=new ArrayList<>();
    ApiImp api=new ApiImp();
    String regionName="",rank="0";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_family_city);
        initView();
    }
    public void initView(){
        city_list.setOnItemClickListener(this);
        setTitleText("所在地");
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

    @OnClick({R.id.select1,R.id.select2,R.id.txt_titlebar_save})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()){
            case R.id.txt_titlebar_save:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setMessage("请再次确定您的所在地，所在地确定后不可修改。");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        submitData();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
                break;
            case R.id.select1:
                select2.setVisibility(View.GONE);
                select3.setVisibility(View.GONE);
                txt_titlebar_save.setVisibility(View.GONE);
                rank="1";
                regionName=select1.getText().toString();
                getRequstCity();
                break;
            case R.id.select2:
                rank="2";
                regionName=select2.getText().toString();
                txt_titlebar_save.setVisibility(View.GONE);
                select3.setVisibility(View.GONE);
                getRequstArea();
                break;
        }
    }
    public void submitData(){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("area_name",regionName);
        api.updateFamily(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                PreferencesMessage.put(mContext,PreferencesMessage.family_id+ AppConfig.CustomerId,"1");
                ViewInject.toast(getApplication(),"修改成功");
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (listData.get(position).getProvince()!=null) {//省
            if (!listData.get(position).getProvincn_city_area_id().equals("0")){

            }
            regionName=listData.get(position).getProvince();
            select1.setText(listData.get(position).getProvince());
            rank="1";
            getRequstCity();
        }else if (listData.get(position).getCity()!=null) {///市
            if (!listData.get(position).getProvincn_city_area_id().equals("0")){

            }
            select2.setVisibility(View.VISIBLE);
            select2.setText(listData.get(position).getCity());
            rank="2";
            regionName=listData.get(position).getCity();
            getRequstArea();

        }else if (listData.get(position).getArea()!=null) {//区
            regionName=listData.get(position).getArea_name();
            select3.setVisibility(View.VISIBLE);
            select3.setText(listData.get(position).getArea());
            txt_titlebar_save.setVisibility(View.VISIBLE);

        }
    }
}
