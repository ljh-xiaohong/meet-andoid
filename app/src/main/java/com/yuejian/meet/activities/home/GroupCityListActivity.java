package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.GroupCicyListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.GroupEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 城市群
 */
public class GroupCityListActivity extends BaseActivity {

    @Bind(R.id.group_city_list)
    ListView listView;
    List<GroupEntity> listData=new ArrayList<>();
    GroupCicyListAdapter mAdapter;
    public String province,city;
    ApiImp api=new ApiImp();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_city_list);
        Intent intent=getIntent();
        province=intent.getStringExtra("province");
        city=intent.getStringExtra("city");
        intData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    public void intData(){
        setTitleText("群列表");
        mAdapter=new GroupCicyListAdapter(listView,listData,R.layout.item_group_list_parent_layout);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        requstData();
    }
    public void requstData(){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("province",province);
        params.put("city",city);
        api.getCityGroup(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData= JSON.parseArray(data,GroupEntity.class);
                mAdapter.refresh(listData);
            }
            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
