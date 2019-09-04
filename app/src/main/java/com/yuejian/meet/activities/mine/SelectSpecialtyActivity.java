package com.yuejian.meet.activities.mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.SurnameAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.SurnameEntity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zh02 on 2017/8/17.
 */

public class SelectSpecialtyActivity extends BaseActivity {
    @Bind(R.id.interest_grid_view)
    GridView gridView;

    private SurnameAdapter mAdapter;
    private List<SurnameEntity> listData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_specialty);
        setTitleText("特长");
        mAdapter = new SurnameAdapter(gridView, listData, R.layout.item_speciality);
        gridView.setAdapter(mAdapter);
        getInterestData();
        IntentFilter filter = new IntentFilter("selected_specialty");
        filter.addAction("unselected_specialty");
        registerReceiver(receiver, filter);
    }

    private void getInterestData() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("type", String.valueOf(2));
        apiImp.getDictData(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                try {
                    String dataJson = new JSONObject(data).getString("特长");
                    List<String> specialtys = JSON.parseArray(dataJson, String.class);
                    listData.clear();
                    for (String s : specialtys) {
                        SurnameEntity entity = new SurnameEntity();
                        entity.id = "";
                        entity.surname = s;
                        listData.add(entity);
                    }
                    mAdapter.refresh(listData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private List<String> selectedSpecialties = new ArrayList<>();
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("selected_specialty".equals(action)) {
                String specialty = intent.getStringExtra("specialty");
                if (!selectedSpecialties.contains(specialty)) {
                    selectedSpecialties.add(specialty);
                }
            } else if ("unselected_specialty".equals(action)) {
                String specialty = intent.getStringExtra("specialty");
                if (selectedSpecialties.contains(specialty)) {
                    selectedSpecialties.remove(specialty);
                }
            }
        }
    };

    @Override
    public void onClick(View v) {

    }

    @Override
    public void finish() {
        if (selectedSpecialties.size() > 0) {
            Intent intent = new Intent();
            String ss = "";
            for (String s : selectedSpecialties) {
                ss += s + ",";
            }
            ss = ss.substring(0, ss.lastIndexOf(","));
            intent.putExtra("specialty", ss);
            setResult(RESULT_OK, intent);
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
