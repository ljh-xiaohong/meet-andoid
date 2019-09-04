package com.yuejian.meet.activities.mine;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.IncomeListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.IncomeListEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class IncomeListActivity extends Activity {

    ListView listView;

    ImageView iv_back;

    List<IncomeListEntity> entities;

    ApiImp apiImp = new ApiImp();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_list);
        initViews();
        init();
    }

    private void initViews() {
        listView = (ListView) this.findViewById(R.id.activity_income_listview);
        iv_back = (ImageView) this.findViewById(R.id.activity_income_cancl);
    }

    private void init() {
        String id = getIntent().getStringExtra("customer_id");
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", id);
        apiImp.doIncomeList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                entities = JSON.parseArray(data, IncomeListEntity.class);
                setAdapter(entities);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });

        iv_back.setOnClickListener(view -> this.finish());

    }

    private void setAdapter(List<IncomeListEntity> entities) {
        listView.setAdapter(new IncomeListAdapter(this, entities));
    }


}
