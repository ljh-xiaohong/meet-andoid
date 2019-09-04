package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.ActionMessageAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.ActionMessageEntity;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 动态消息
 */
public class ActionMessageActivity extends BaseActivity implements AdapterView.OnItemClickListener, SpringView.OnFreshListener {
    @Bind(R.id.action_message_lsit)
    ListView listView;
    ActionMessageAdapter mAdapter;
    List<ActionMessageEntity> listData = new ArrayList<>();
    private DataIdCallback<String> dataIdCallback = null;
    private SpringView springView;
    private int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ACTIVITY_NAME = getString(R.string.Dynamic_message);
        setContentView(R.layout.activity_action_message);
        initData();
    }

    public void initData() {
        setTitleText(getString(R.string.Dynamic_message));
        springView = (SpringView) findViewById(R.id.spring_view);
        springView.setListener(this);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        mAdapter = new ActionMessageAdapter(listView, listData, R.layout.item_action_message_list);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        requstData();
    }

    public void requstData() {
        dataIdCallback = new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<ActionMessageEntity> actionMessageEntities = JSON.parseArray(data, ActionMessageEntity.class);
                if (actionMessageEntities != null && !actionMessageEntities.isEmpty()) {
                    if (index == 1) {
                        if (listData.isEmpty()) {
                            listData.addAll(actionMessageEntities);
                        } else if (!actionMessageEntities.get(0).getAction_id().equals(listData.get(0).getAction_id())) {
                            listData.addAll(0, actionMessageEntities);
                        }
                    } else {
                        if (listData.isEmpty()) {
                            listData.addAll(actionMessageEntities);
                        } else {
                            ActionMessageEntity e1 = listData.get(listData.size() - 1);
                            ActionMessageEntity e2 = actionMessageEntities.get(actionMessageEntities.size() - 1);
                            if (!e1.action_id.equals(e2.action_id)) {
                                listData.addAll(actionMessageEntities);
                            }
                        }
                    }
                }
                mAdapter.refresh(listData);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                springView.onFinishFreshAndLoad();
                index = 1;
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                springView.onFinishFreshAndLoad();
            }
        };
        boolean isFromPersonAction = getIntent().getBooleanExtra("from_person_action", false);
        if (!isFromPersonAction) {
            apiImp.getActionUnredMgs(this, dataIdCallback);
        } else {
            HashMap<String, Object> params = new HashMap<>();
            params.put("customer_id", AppConfig.CustomerId);
            params.put("pageIndex", String.valueOf(index));
            apiImp.getActionMessages(params, this, dataIdCallback);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onRefresh() {
        index = 1;
        requstData();
    }

    @Override
    public void onLoadmore() {
        index++;
        requstData();
    }
}
